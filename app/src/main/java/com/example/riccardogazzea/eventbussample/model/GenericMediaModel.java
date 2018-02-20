package com.example.riccardogazzea.eventbussample.model;

import android.os.Parcel;

/**
 * Created on 13/02/18.
 *
 * @author Umberto Marini
 */
public class GenericMediaModel extends MediaModel {

    public GenericMediaModel(int identifier) {
        super(identifier);
    }

    @Override
    public boolean isVideo() {
        return false;
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

    protected GenericMediaModel(Parcel in) {
        super(in);
    }

    public static final Creator<GenericMediaModel> CREATOR = new Creator<GenericMediaModel>() {
        @Override
        public GenericMediaModel createFromParcel(Parcel source) {
            return new GenericMediaModel(source);
        }

        @Override
        public GenericMediaModel[] newArray(int size) {
            return new GenericMediaModel[size];
        }
    };
}
