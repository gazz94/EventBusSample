package com.example.riccardogazzea.eventbussample.events;

import com.baseandroid.events.Event;

/**
 * Created on 13/02/18.
 *
 * @author Umberto Marini
 */
@Event(type = Event.Type.UI)
public class UiRecyclerStateIdleEvent {

    private Object mTag;

    public UiRecyclerStateIdleEvent(Object tag) {
        mTag = tag;
    }

    public Object getTag() {
        return mTag;
    }
}
