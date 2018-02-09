package com.example.riccardogazzea.eventbussample;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.MediaController;
import android.widget.VideoView;
import com.baseandroid.events.EventDispatcher;
import com.baseandroid.events.rx.annotations.RxSubscribe;

/**
 * Created by riccardogazzea on 07/02/2018.
 */

public class CustomView extends VideoView implements MediaPlayer.OnPreparedListener {

    //tag assegnato nell'adapter
    public static int TAG = R.id.view;
    //l'idea è quella di ricordare dove è stato interrotto il video, al momento non funziona
    private int currentpos = 0;

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        MediaController mediaController = new MediaController(getContext());
        mediaController.setAnchorView(this);
        setMediaController(mediaController);
        setOnPreparedListener(this);
    }

    public CustomView(Context context) {
        super(context);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventDispatcher.register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        currentpos = getCurrentPosition();
        EventDispatcher.unregister(this);
    }

    @RxSubscribe
    public void onConsumeEvent(CustomEvent customEvent){
        if (customEvent.isVideo() && getTag(TAG) != null && customEvent.getViewTag() == (int)getTag(TAG)) {
            //imposto il link alla risorsa, idealmente questa operazione deve essere fatta prima. in modo che quando passo di qui devo solo far start
            setVideoURI(customEvent.getVideoUri());
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        //faccio partire il video da dove ero arrivato
        requestFocus();
        seekTo(currentpos);
        start();
    }
}
