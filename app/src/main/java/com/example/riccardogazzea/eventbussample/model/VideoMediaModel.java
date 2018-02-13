package com.example.riccardogazzea.eventbussample.model;

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
}
