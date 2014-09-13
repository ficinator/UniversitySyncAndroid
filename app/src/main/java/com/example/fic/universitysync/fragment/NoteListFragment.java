package com.example.fic.universitysync.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.fic.universitysync.R;
import com.example.fic.universitysync.activity.main.MainActivity;
import com.example.fic.universitysync.model.MyHttpClient;
import com.example.fic.universitysync.model.data.Group;
import com.example.fic.universitysync.model.data.Note;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NoteListFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class NoteListFragment extends ListFragment {
    private static final String GROUP = "group";
    private static final int NOTE_ITEM_LAYOUT = R.layout.item_note;

    private Group mGroup;
    private List<Note> mNotes;
    private ProgressBar mProgressBar;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param group
     * @return A new instance of fragment NoteListFragment.
     */
    public static NoteListFragment newInstance(Group group) {
        NoteListFragment fragment = new NoteListFragment();
        Bundle args = new Bundle();
        args.putParcelable(GROUP, group);
        fragment.setArguments(args);
        return fragment;
    }
    public NoteListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mGroup = getArguments().getParcelable(GROUP);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_note_list, container, false);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        new LoadGroupNotesMetaTask(mGroup.getId()).execute();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

    }

    public class LoadGroupNotesMetaTask extends AsyncTask<Void, Void, List<Note>> {

        private int mGroupId;

        public LoadGroupNotesMetaTask(int mGroupId) {
            this.mGroupId = mGroupId;
        }

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Note> doInBackground(Void... voids) {
            mNotes = new ArrayList<Note>();

            List<NameValuePair> attributes = new ArrayList<NameValuePair>();
            attributes.add(new BasicNameValuePair("group_id", Integer.toString(mGroupId)));
            JSONObject userNotesObj = MyHttpClient.getInstance().get(attributes, "getNotes.php");
            if (userNotesObj != null) {
                try {
                    JSONArray notes = userNotesObj.getJSONArray("notes");
                    if (notes.length() == 0) {
                        //TODO: show no groups text / set it in list options
                    }
                    else {
                        for (int i = 0; i < notes.length(); i++) {
                            Note note = new Note(notes.getJSONObject(i));
                            mNotes.add(note);
                        }
                        return mNotes;
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
        protected void onPostExecute(List<Note> notes) {
            mProgressBar.setVisibility(View.GONE);
            new LoadGroupNotesTask(notes).execute();
            //setListAdapter(new NoteListAdapter(mNotes));
        }
    }

    public class LoadGroupNotesTask extends AsyncTask<Void, Void, List<Note>> {

        private List<Note> mNoteMetas;

        public LoadGroupNotesTask(List<Note> noteMetas) {
            this.mNoteMetas = noteMetas;
        }

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Note> doInBackground(Void... voids) {
            for (Note noteMeta : mNoteMetas) {
                JSONObject noteDetails = MyHttpClient.getInstance().get(noteMeta.getPath());
                if (noteDetails != null) {
                    noteMeta.setDetails(noteDetails);
                }
            }
            return mNoteMetas;
        }

        @Override
        protected void onPostExecute(List<Note> notes) {
            mProgressBar.setVisibility(View.GONE);
            setListAdapter(new NoteListAdapter(notes));
        }
    }

    private class NoteListAdapter extends ArrayAdapter<Note> {

        public NoteListAdapter(List<Note> mNotes) {
            super(getActivity(), NOTE_ITEM_LAYOUT, mNotes);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(NOTE_ITEM_LAYOUT, null);
            }

            TextView titleView = (TextView) convertView.findViewById(R.id.title);
            TextView dateView = (TextView) convertView.findViewById(R.id.date);
            TextView contentView = (TextView) convertView.findViewById(R.id.content);

            Note note = getItem(position);
            if (note.getKeywords().size() > 0)
                titleView.setText(note.getKeywords().get(0));
            dateView.setText(note.getDate());
            contentView.setText(note.getContent());

            return convertView;
        }
    }
}
