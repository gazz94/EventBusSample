package com.example.riccardogazzea.eventbussample.model;

import android.os.Parcel;

/**
 * Created on 13/02/18.
 *
 * @author Umberto Marini
 */
public class PictureMediaModel extends MediaModel {

    public PictureMediaModel(int identifier, String url) {
        super(identifier, url);
    }

    @Override
    public boolean isVideo() {
        return false;
    }

    @Override
    public boolean isPicture() {
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    protected PictureMediaModel(Parcel in) {
        super(in);
    }

    public static final Creator<PictureMediaModel> CREATOR = new Creator<PictureMediaModel>() {
        @Override
        public PictureMediaModel createFromParcel(Parcel source) {
            return new PictureMediaModel(source);
        }

        @Override
        public PictureMediaModel[] newArray(int size) {
            return new PictureMediaModel[size];
        }
    };
}
