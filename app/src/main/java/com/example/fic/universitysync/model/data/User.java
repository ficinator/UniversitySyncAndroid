package com.example.fic.universitysync.model.data;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fic on 12.9.2014.
 */
public class User extends AbstractElement {
    private String mEmail;
    private String mName;
    private String mSurname;
    private String mUniversity;
    private String mInfo;
    private int mRank;
    private Drawable mThumb;

    public User(JSONObject object) throws JSONException {
        super(object.getInt("id"));
        this.mEmail = object.getString("email");
        this.mName = object.getString("name");
        this.mSurname = object.getString("surname");
        this.mUniversity = object.getString("university");
        this.mInfo = object.getString("info");
        this.mRank = object.getInt("rank");
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeString(mName);
        out.writeString(mSurname);
        out.writeString(mEmail);
        out.writeString(mUniversity);
        out.writeString(mInfo);
        out.writeInt(mRank);
        if (mThumb != null) {
            Bitmap bmp = ((BitmapDrawable) mThumb).getBitmap();
            out.writeParcelable(bmp, flags);
        }
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getSurname() {
        return mSurname;
    }

    public void setSurname(String mSurname) {
        this.mSurname = mSurname;
    }

    public String getUniversity() {
        return mUniversity;
    }

    public void setUniversity(String mUniversity) {
        this.mUniversity = mUniversity;
    }

    public String getInfo() {
        return mInfo;
    }

    public void setInfo(String mInfo) {
        this.mInfo = mInfo;
    }

    public int getRank() {
        return mRank;
    }

    public void setRank(int mRank) {
        this.mRank = mRank;
    }

    public String getFullName() {
        return mName + " " + mSurname;
    }
    public String getThumbPath() { return "../users/" + mId + "/userPhoto.jpg"; }

    public void setThumb(Drawable thumb) { mThumb = thumb; }

    public Drawable getThumb() { return mThumb; }
}
