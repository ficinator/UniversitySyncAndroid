package com.example.fic.universitysync.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fic.universitysync.R;
import com.example.fic.universitysync.model.data.Group;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GroupFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GroupFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class GroupFragment extends Fragment {

    //TODO: more
    private static final int NUM_TABS = 1;
    private static final int TAB_NOTES = 0;
    private static final int TAB_ARTICLES = 1;
    private static final int TAB_FILES = 2;
    private static final CharSequence[] TAB_TITLES = { "Notes", "Articles", "Files" };

    private static int PAGER_ID = R.id.pager;
    private static String GROUP = "group";

    private CharSequence mTitle;
    private Group mGroup;

    private ViewPager mGroupPager;
    private GroupPagerAdapter mGroupPagerAdapter;

    //private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param group
     * @return A new instance of fragment GroupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GroupFragment newInstance(Group group) {
        GroupFragment fragment = new GroupFragment();
        Bundle args = new Bundle();
        args.putParcelable(GROUP, group);
        fragment.setArguments(args);
        return fragment;
    }
    public GroupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mGroup = getArguments().getParcelable(GROUP);
            showTabs();
        }
    }

    private void showTabs() {
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

            }
        };
        for (CharSequence title : TAB_TITLES)
            actionBar.addTab(actionBar.newTab().setText(title).setTabListener(tabListener));
    }

    private void hideTabs() {
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.removeAllTabs();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group, container, false);

        mGroupPagerAdapter = new GroupPagerAdapter(
                getActivity().getSupportFragmentManager(), mGroup);
        mGroupPager = (ViewPager) view.findViewById(PAGER_ID);
        mGroupPager.setAdapter(mGroupPagerAdapter);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }

    @Override
    public void onDestroy() {
        hideTabs();

        super.onDestroy();
    }

    public CharSequence getTitle() {
        return mTitle;
    }

    public void setTitle(CharSequence mTitle) {
        this.mTitle = mTitle;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    public class GroupPagerAdapter extends FragmentStatePagerAdapter {

        public GroupPagerAdapter(FragmentManager fm, Group group) {
            super(fm);
            mGroup = group;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case TAB_NOTES:
                    return NoteListFragment.newInstance(mGroup);
                case TAB_ARTICLES:
                    return null;
                case TAB_FILES:
                    return null;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return NUM_TABS;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TAB_TITLES[position];
        }
    }
}
