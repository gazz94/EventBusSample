package com.example.riccardogazzea.eventbussample.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created on 13/02/18.
 *
 * @author Umberto Marini
 */
public abstract class MediaModel implements Parcelable {

    private int mIdentifier;
    private String mUrl;

    public MediaModel(int identifier) {
        mIdentifier = identifier;
    }

    public MediaModel(int identifier, String url) {
        mIdentifier = identifier;
        mUrl = url;
    }

    public int getIdentifier() {
        return mIdentifier;
    }

    public String getUrl() {
        return mUrl;
    }

    public abstract boolean isVideo();

    public abstract boolean isPicture();

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        MediaModel that = (MediaModel) o;

        return mIdentifier == that.mIdentifier;
    }

    @Override
    public int hashCode() {
        return mIdentifier;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mIdentifier);
        dest.writeString(this.mUrl);
    }

    protected MediaModel(Parcel in) {
        this.mIdentifier = in.readInt();
        this.mUrl = in.readString();
    }
}
