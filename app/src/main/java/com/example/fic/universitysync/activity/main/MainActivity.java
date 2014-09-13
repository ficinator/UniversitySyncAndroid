package com.example.fic.universitysync.activity.main;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.fic.universitysync.R;
import com.example.fic.universitysync.fragment.GroupFragment;
import com.example.fic.universitysync.fragment.HomeFragment;
import com.example.fic.universitysync.model.MyHttpClient;
import com.example.fic.universitysync.model.data.Group;
import com.example.fic.universitysync.model.data.User;
import com.example.fic.universitysync.model.drawer.DrawerCategoryItem;
import com.example.fic.universitysync.model.drawer.DrawerGroupItem;
import com.example.fic.universitysync.model.drawer.DrawerItem;
import com.example.fic.universitysync.model.drawer.DrawerUserItem;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private static final int RESULT_LOGIN = 0;
    private static int DRAWER_ITEM_LAYOUT = R.layout.item_drawer;
    private static MyHttpClient mHttpClient;

    private DrawerLayout mDrawerLayout;
    private ListView mSideMenuListView;
    private ActionBarDrawerToggle mDrawerToggle;
    private List<DrawerItem> mDrawerItems;
    private ProgressBar mProgressBar;

    private User mUser;
    private List<Group> mGroups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHttpClient = MyHttpClient.getInstance();
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mSideMenuListView = (ListView) findViewById(R.id.left_drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.abc_ic_ab_back_holo_dark, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerItems = new ArrayList<DrawerItem>();
        mGroups = new ArrayList<Group>();

        // check if the user is signed in
        // if not, start login screen
        int userId = getUserId();
        if (userId == -1) {
            new UserLoginTask("h", "h").execute();
            //Intent intent = new Intent(this, LoginActivity.class);
            //startActivityForResult(intent, RESULT_LOGIN);
        }
        else {
            //TODO:
        }
    }

    private int getUserId() {
        return MySharedPreferences.loadUserId(MainActivity.this);
    }

    private void showGroups(List<Group> groups) {
        mDrawerItems.add(new DrawerCategoryItem(getResources().getString(R.string.my_groups)));
        for (Group group : groups) {
             mDrawerItems.add(new DrawerGroupItem(group));
        }
        mSideMenuListView.setAdapter(new DrawerListAdapter(getApplicationContext(), mDrawerItems));
        mSideMenuListView.setOnItemClickListener(new DrawerItemClickListener());

        //TODO: check in shared preferences the last opened page
        selectItem(0);
    }

    public void selectItem(int position) {
        // Create a new fragment and specify the planet to show based on position
        if (position == 0) {
            HomeFragment homeFragment = HomeFragment.newInstance(mUser);

            // Insert the fragment by replacing any existing fragment
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content, homeFragment)
                    .commit();

            setTitle(mUser.getFullName());
            getActionBar().setSubtitle(mUser.getUniversity());
        }
        else {
            Group group = mGroups.get(position - 2);
            GroupFragment groupFragment = GroupFragment.newInstance(group);

            // Insert the fragment by replacing any existing fragment
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content, groupFragment)
                    .commit();

            setTitle(group.getName());
            getActionBar().setSubtitle(group.getUniversity());
        }
        // Highlight the selected item, update the title, and close the drawer
        mSideMenuListView.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mSideMenuListView);
    }

    private int showFragment(Fragment fragment) {
        return getSupportFragmentManager().beginTransaction().add(R.id.content, fragment).commit();
    }

    @Override
     public void setTitle(CharSequence title) {
        super.setTitle(title);
//        mTitle = title;
//        getActionBar().setTitle(mTitle);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mSideMenuListView);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, User> {

        private String mName;
        private String mPassword;

        public UserLoginTask(String mName, String mPassword) {
            this.mName = mName;
            this.mPassword = mPassword;
        }

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected User doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            List<NameValuePair> attributes = new ArrayList<NameValuePair>();
            attributes.add(new BasicNameValuePair("email", mName));
            attributes.add(new BasicNameValuePair("password", mPassword));
            JSONObject usersObj = MyHttpClient.getInstance().post(attributes, "login.php");
            if (usersObj != null) {
                try {
                    JSONArray users = usersObj.getJSONArray("users");
                    if (users.length() == 1) {
                        mUser = new User(users.getJSONObject(0));
                        mUser.setThumb(
                                MyHttpClient.getInstance().getImage(mUser.getThumbPath()));
                        return mUser;
                    }
                    else {
                        //TODO: no user / wrong credentials
                    }
                } catch (JSONException e) {
                    //TODO: wrong json
                }
            }
            else {
                //TODO: server problem
            }
            return null;
        }

        @Override
        protected void onPostExecute(User user) {
            mProgressBar.setVisibility(View.GONE);
            mDrawerItems.add(new DrawerUserItem(user));
            new UserGroupsTask(user.getId()).execute();
        }
    }

    public class UserGroupsTask extends AsyncTask<Void, Void, List<Group>> {

        private int mUserId;

        public UserGroupsTask(int mUserId) {
            this.mUserId = mUserId;
        }

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Group> doInBackground(Void... voids) {
            List<NameValuePair> attributes = new ArrayList<NameValuePair>();
            attributes.add(new BasicNameValuePair("id", Integer.toString(mUserId)));
            JSONObject groupsObj = MyHttpClient.getInstance().get(attributes, "home.php");
            if (groupsObj != null) {
                try {
                    JSONArray groups = groupsObj.getJSONArray("groups");
                    if (groups.length() == 0) {
                        //TODO: show no groups text / set it in list options
                    }
                    else {
                        for (int i = 0; i < groups.length(); i++) {
                            Group group = new Group(groups.getJSONObject(i));
                            group.setThumb(
                                    MyHttpClient.getInstance().getImage(group.getThumbPath()));
                            mGroups.add(group);
                        }
                        return mGroups;
                    }
                } catch (JSONException e) {
                    //TODO: wrong json
                }
            }
            else {
                //TODO: server problem
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Group> groups) {
            mProgressBar.setVisibility(View.GONE);
            if (groups != null)
                showGroups(groups);
            else {
                //TODO:
            }
        }
    }

//    @Override
//     public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case RESULT_LOGIN:
//                if (resultCode == Activity.RESULT_OK) {
//                    Toast.makeText(MainActivity.this, "Loginlo", Toast.LENGTH_SHORT).show();
//                }
//        }
//    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        /** Swaps fragments in the main content view */
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            switch (position) {
                case 1:
                    break;
                case 0:
                default:
                    selectItem(position);
            }
        }
    }

    public class DrawerListAdapter extends ArrayAdapter<DrawerItem> {

        public DrawerListAdapter(Context ctx, List<DrawerItem> drawerItems) {
            super(ctx, DRAWER_ITEM_LAYOUT, drawerItems);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(DRAWER_ITEM_LAYOUT, null);
            }

            ImageView thumbView = (ImageView) convertView.findViewById(R.id.thumb);
            TextView titleView = (TextView) convertView.findViewById(R.id.title);
            TextView subtitleView = (TextView) convertView.findViewById(R.id.subtitle);

            DrawerItem item = getItem(position);
            titleView.setText(item.getTitle());
            subtitleView.setText(item.getSubTitle());

            if (item instanceof DrawerCategoryItem) {
                titleView.setAllCaps(true);
                thumbView.setVisibility(View.GONE);
                subtitleView.setVisibility(View.GONE);
                convertView.setEnabled(false);
            }
            else if (item instanceof DrawerGroupItem ||
                     item instanceof DrawerUserItem) {
                thumbView.setImageDrawable(item.getIcon());
            }

            return convertView;
        }
    }
}
