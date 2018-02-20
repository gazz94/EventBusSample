package com.example.riccardogazzea.eventbussample.model;

import android.os.Parcel;

/**
 * Created on 13/02/18.
 *
 * @author Umberto Marini
 */
public class VideoMediaModel extends MediaModel {

    public VideoMediaModel(int identifier, String url) {
        super(identifier, url);
    }

    @Override
    public boolean isVideo() {
        return true;
    }

    @Override
    public boolean isPicture() {
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    protected VideoMediaModel(Parcel in) {
        super(in);
    }

    public static final Creator<VideoMediaModel> CREATOR = new Creator<VideoMediaModel>() {
        @Override
        public VideoMediaModel createFromParcel(Parcel source) {
            return new VideoMediaModel(source);
        }

        @Override
        public VideoMediaModel[] newArray(int size) {
            return new VideoMediaModel[size];
        }
    };
}
