package com.example.riccardogazzea.eventbussample;

import android.net.Uri;
import android.support.annotation.Nullable;

import com.baseandroid.events.Event;

/**
 * Created by riccardogazzea on 07/02/2018.
 */
@Event(type = Event.Type.UI)
public class CustomEvent {

    public CustomEvent(int viewTag, @Nullable Uri videoUri, boolean isVideo) {
        this.viewTag = viewTag;
        this.videoUri = videoUri;
        this.isVideo = isVideo;
    }

    private int viewTag;

    public int getViewTag() {
        return viewTag;
    }

    public void setViewTag(int viewTag) {
        this.viewTag = viewTag;
    }

    private Uri videoUri;

    @Nullable
    public Uri getVideoUri() {
        return videoUri;
    }

    public void setVideoUri(@Nullable Uri videoUri) {
        this.videoUri = videoUri;
    }

    private boolean isVideo;

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }
}
