package com.example.riccardogazzea.eventbussample.events;

import com.baseandroid.events.Event;

/**
 * Created on 14/02/2018.
 *
 * @author riccardogazzea
 */

@Event(type = Event.Type.CONTEXT)
public class ContextOnPauseEvent {
    public ContextOnPauseEvent() {
        //empty
    }
}
