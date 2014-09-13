package com.example.fic.universitysync.model.data;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fic on 11.9.2014.
 */
public class Group extends AbstractElement {
    private String mName;
    private String mUniversity;
    private String mInfo;
    private int mIsPublic;
    private String mMemberInfo;
    private Drawable mThumb;

    public Group(JSONObject object) throws JSONException {
        super(object.getInt("id"));
        this.mName = object.getString("name");
        this.mUniversity = object.getString("university");
        this.mInfo = object.getString("info");
        this.mIsPublic = object.getInt("public");
        this.mMemberInfo = object.getString("member_info");
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

    public int isPublic() {
        return mIsPublic;
    }

    public void setPublic(int mIsPublic) {
        this.mIsPublic = mIsPublic;
    }

    public String getMemberInfo() {
        return mMemberInfo;
    }

    public void setMemberInfo(String mMemberInfo) {
        this.mMemberInfo = mMemberInfo;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getThumbPath() { return "../groups/" + mId + "/groupPhoto.jpg"; }

    public void setThumb(Drawable thumb) { mThumb = thumb; }

    public Drawable getThumb() { return mThumb; }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeString(mName);
        out.writeString(mUniversity);
        out.writeString(mInfo);
        out.writeInt(mIsPublic);
        out.writeString(mMemberInfo);
        if (mThumb != null) {
            Bitmap bmp = ((BitmapDrawable) mThumb).getBitmap();
            out.writeParcelable(bmp, flags);
        }
    }
}
