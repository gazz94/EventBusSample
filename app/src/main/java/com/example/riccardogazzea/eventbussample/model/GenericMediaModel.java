package com.example.riccardogazzea.eventbussample.model;

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

}
