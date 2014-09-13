package com.example.fic.universitysync.model.data;

import android.os.Parcel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fic on 12.9.2014.
 */
public class Note extends AbstractElement {

    private int mUserId;
    private int mGroupId;
    private int mFolderId;
    private int mLikes;
    private String mPath;
    private String mDate;
    private String mCategory;
    private List<String> mKeywords;
    private List<String> mReferences;
    private String mContent;

    public Note(JSONObject object) throws JSONException {
        super(object.getInt("id"));
        mUserId = object.getInt("id_user");
        mGroupId = object.getInt("id_group");
        mFolderId = object.getInt("id_folder");
        mLikes = object.getInt("likes");
        mPath = "." + object.getString("path");
        mDate = object.getString("date");
        mCategory = object.getString("category");
        mKeywords = new ArrayList<String>();
        mReferences = new ArrayList<String>();
        mContent = "";
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int mUserId) {
        this.mUserId = mUserId;
    }

    public int getGroupId() {
        return mGroupId;
    }

    public void setGroupId(int mGroupId) {
        this.mGroupId = mGroupId;
    }

    public int getFolderId() {
        return mFolderId;
    }

    public void setFolderId(int mFolderId) {
        this.mFolderId = mFolderId;
    }

    public int getLikes() {
        return mLikes;
    }

    public void setLikes(int mLikes) {
        this.mLikes = mLikes;
    }

    public String getPath() {
        return mPath;
    }

    public void setPath(String mPath) {
        this.mPath = mPath;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getCategory() { return mCategory; }

    public void setCategory(String mCategory) { this.mCategory = mCategory; }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        this.mPath = content;
    }

    public List<String> getKeywords() { return mKeywords; }

    public void setKeywords(List<String> keywords) { mKeywords = keywords; }

    public List<String> getReferences() { return mReferences; }

    public void setReferences(List<String> references) { mReferences = references; }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeInt(mUserId);
        out.writeInt(mGroupId);
        out.writeInt(mFolderId);
        out.writeInt(mLikes);
        out.writeString(mPath);
        out.writeString(mDate);
        out.writeString(mCategory);
        out.writeStringList(mKeywords);
        out.writeStringList(mReferences);
        out.writeString(mContent);
    }

    public void setDetails(JSONObject object) {
        try {
            JSONObject details = object.getJSONObject("Note");

            mKeywords.clear();
            if (details.get("KeyWords") instanceof JSONObject) {
                JSONObject keywords = details.getJSONObject("KeyWords");
                if (!keywords.isNull("KW")) {
                    if (keywords.get("KW") instanceof JSONArray) {
                        JSONArray kws = keywords.getJSONArray("KW");
                        for (int i = 0; i < kws.length(); i++)
                            mKeywords.add(kws.getString(i));
                    }
                    else if (keywords.get("KW") instanceof String)
                        mKeywords.add(keywords.getString("KW"));
                }
            }

            mReferences.clear();
            if (details.get("References") instanceof JSONObject) {
                JSONObject references = details.getJSONObject("References");
                if (!references.isNull("Ref")) {
                    if (references.get("Ref") instanceof JSONArray) {
                        JSONArray refs = references.getJSONArray("Ref");
                        for (int i = 0; i < refs.length(); i++)
                            mReferences.add(refs.getString(i));
                    }
                    else if (references.get("Ref") instanceof String)
                        mReferences.add(references.getString("Ref"));
                }
            }

            if (details.get("Content") instanceof String)
                mContent = details.getString("Content");

        } catch (JSONException e) {
            //TODO:
            e.printStackTrace();
        }
    }
}
