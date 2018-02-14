package com.example.riccardogazzea.eventbussample;

import com.example.riccardogazzea.eventbussample.model.GenericMediaModel;
import com.example.riccardogazzea.eventbussample.model.MediaModel;
import com.example.riccardogazzea.eventbussample.model.VideoMediaModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 13/02/18.
 *
 * @author Umberto Marini
 */
public class MockUtils {

    private static final String TEST_VIDEO_URL = "https://s3-eu-west-1.amazonaws.com/nowr-dev/uservideos/1004/SampleVideo_1280x720_5mb.mp4";
    public static final List<MediaModel> VIDEOS;

    static {
        VIDEOS = new ArrayList<>();
        VIDEOS.add(new VideoMediaModel(1, TEST_VIDEO_URL));
        VIDEOS.add(new VideoMediaModel(2, TEST_VIDEO_URL));
        VIDEOS.add(new GenericMediaModel(3));
        VIDEOS.add(new GenericMediaModel(4));
        VIDEOS.add(new VideoMediaModel(5, TEST_VIDEO_URL));
        VIDEOS.add(new VideoMediaModel(6, TEST_VIDEO_URL));
        VIDEOS.add(new VideoMediaModel(7, TEST_VIDEO_URL));
        VIDEOS.add(new GenericMediaModel(8));
        VIDEOS.add(new VideoMediaModel(9, TEST_VIDEO_URL));
        VIDEOS.add(new VideoMediaModel(10, TEST_VIDEO_URL));
        VIDEOS.add(new GenericMediaModel(11));
        VIDEOS.add(new GenericMediaModel(12));
        //VIDEOS.add(new GenericMediaModel(1));
        //VIDEOS.add(new GenericMediaModel(2));
        //VIDEOS.add(new GenericMediaModel(3));
        //VIDEOS.add(new GenericMediaModel(4));
        //VIDEOS.add(new GenericMediaModel(5));
        //VIDEOS.add(new GenericMediaModel(6));
        //VIDEOS.add(new GenericMediaModel(7));
        //VIDEOS.add(new GenericMediaModel(8));
        //VIDEOS.add(new GenericMediaModel(9));
        //VIDEOS.add(new GenericMediaModel(10));
        //VIDEOS.add(new GenericMediaModel(11));
        //VIDEOS.add(new GenericMediaModel(12));
    }
}
