package com.example.riccardogazzea.eventbussample.events;

import com.baseandroid.events.Event;

/**
 * Created on 16/02/2018.
 *
 * @author riccardogazzea
 */

@Event(type = Event.Type.CONTEXT)
public class ContextOnResumeEvent {
    public ContextOnResumeEvent() {
        //empty
    }
}
