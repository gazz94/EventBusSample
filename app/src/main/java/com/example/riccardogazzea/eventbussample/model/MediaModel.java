package com.example.riccardogazzea.eventbussample.model;

/**
 * Created on 13/02/18.
 *
 * @author Umberto Marini
 */
public abstract class MediaModel {

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
}
