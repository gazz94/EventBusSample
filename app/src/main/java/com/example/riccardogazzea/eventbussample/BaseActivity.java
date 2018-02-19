package com.example.riccardogazzea.eventbussample;

import android.support.v7.app.AppCompatActivity;
import com.baseandroid.events.EventDispatcher;
import com.example.riccardogazzea.eventbussample.events.ContextOnPauseEvent;

/**
 * Created on 13/02/18.
 *
 * @author Riccardo Gazzea
 */

public class BaseActivity extends AppCompatActivity {

    private CharSequence mEventDispatcherTag;

    @Override
    protected void onResume() {
        super.onResume();
        EventDispatcher.loadPoint(this, String.valueOf(mEventDispatcherTag));
        EventDispatcher.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //when pausing the activity send a message to pause the video playback
        EventDispatcher.post(new ContextOnPauseEvent());

        mEventDispatcherTag = EventDispatcher.savePoint(this);
        EventDispatcher.unregister(this);
    }
}
