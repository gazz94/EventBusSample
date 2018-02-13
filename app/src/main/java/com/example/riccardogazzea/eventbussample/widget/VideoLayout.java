package com.example.riccardogazzea.eventbussample.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.baseandroid.events.EventDispatcher;
import com.baseandroid.events.rx.annotations.RxSubscribe;
import com.example.riccardogazzea.eventbussample.R;
import com.example.riccardogazzea.eventbussample.events.UiRecyclerStateIdleEvent;
import com.example.riccardogazzea.eventbussample.events.UiRecyclerStateNotIdleEvent;
import com.example.riccardogazzea.eventbussample.model.VideoMediaModel;

/**
 * Created on 13/02/18.
 *
 * @author Umberto Marini
 */
public class VideoLayout extends RelativeLayout {

    private VideoMediaModel mMediaModel;

    private VideoView mVideoView;
    private ImageView mPlaceholderImageView;
    private TextView mTextTextView;
    private boolean mPlaying = false;

    public VideoLayout(Context context) {
        super(context);
        init(context);
    }

    public VideoLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VideoLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public VideoLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.listitem_videolayout, this, true);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.listitem_height)));
        setBackgroundColor(ContextCompat.getColor(context, android.R.color.black));

        mVideoView = findViewById(R.id.videolayout_player);
        mPlaceholderImageView = findViewById(R.id.videolayout_placeholder_image);
        mTextTextView = findViewById(R.id.videolayout_test_text);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventDispatcher.register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventDispatcher.unregister(this);
    }

    @RxSubscribe
    public void onConsumeEvent(UiRecyclerStateIdleEvent event) {
        if (mMediaModel != null) {
            if (mMediaModel.equals(event.getTag())) {
                play();
            } else {
                pause();
            }
        }
    }

    @RxSubscribe
    public void onConsumeEvent(UiRecyclerStateNotIdleEvent event) {
        pause();
    }

    public void setVideoMediaModel(VideoMediaModel mediaModel) {
        mMediaModel = mediaModel;
    }

    private boolean isPlaying() {
        return mPlaying;
    }

    public void play() {
        mPlaying = true;
        // TODO start video
        mTextTextView.setText("Playing...");
    }

    public void pause() {
        // TODO pause video
        if (isPlaying()) {
            mPlaying = false;
            mTextTextView.setText("Paused");
        }
    }

    public void stop() {
        // TODO stop video and release related resources
        if (isPlaying()) {
            mPlaying = false;
            mTextTextView.setText("Stopped");
        }
    }
}
