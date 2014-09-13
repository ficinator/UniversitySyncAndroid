package com.example.fic.universitysync.model.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fic on 12.9.2014.
 */
public abstract class AbstractElement implements Parcelable {
    protected int mId;

    public AbstractElement(int mId) {
        this.mId = mId;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mId);
    }

    public int describeContents() {
        return 0;
    }
}
