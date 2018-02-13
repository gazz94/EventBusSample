package com.example.riccardogazzea.eventbussample.widget;

import android.content.Context;
import android.net.Uri;
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

import com.baseandroid.events.EventDispatcher;
import com.baseandroid.events.rx.annotations.RxSubscribe;
import com.example.riccardogazzea.eventbussample.R;
import com.example.riccardogazzea.eventbussample.events.UiRecyclerStateIdleEvent;
import com.example.riccardogazzea.eventbussample.events.UiRecyclerStateNotIdleEvent;
import com.example.riccardogazzea.eventbussample.model.VideoMediaModel;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;

/**
 * Created on 13/02/18.
 *
 * @author Umberto Marini
 */
public class VideoLayout extends RelativeLayout {

    private VideoMediaModel mMediaModel;

    private SimpleExoPlayerView mSimpleExoPlayerView;
    private SimpleExoPlayer mPlayer;
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

        // 1. Create a default TrackSelector
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        // 2. Create the player
        mPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
        mPlayer.setPlayWhenReady(false);
        mSimpleExoPlayerView = findViewById(R.id.videolayout_player);
        mSimpleExoPlayerView.setPlayer(mPlayer);

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
        ExtractorMediaSource mediaSource = new ExtractorMediaSource.Factory(buildDataSourceFactory(true)).createMediaSource(
                        Uri.parse(mMediaModel.getUrl()));
        // Prepare the player with the source.
        mPlayer.prepare(mediaSource);
    }

    private DataSource.Factory buildDataSourceFactory(boolean useBandwidthMeter){
        return buildDataSourceFactory(useBandwidthMeter ? new DefaultBandwidthMeter(): null);
    }

    private DataSource.Factory buildDataSourceFactory(DefaultBandwidthMeter bandwidthMeter){
        return new DefaultDataSourceFactory(getContext(), bandwidthMeter, buildHttpDataSourceFactory(bandwidthMeter));
    }

    public HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultHttpDataSourceFactory(Util.getUserAgent(getContext(), "EventBusSample"), bandwidthMeter);
    }

    private boolean isPlaying() {
        return mPlaying;
    }

    public void play() {
        mPlaying = true;
        if (mPlayer != null){
            mPlayer.setPlayWhenReady(true);
        }
    }

    public void pause() {
        // TODO pause video
        if (isPlaying()) {
            mPlaying = false;
            mPlayer.setPlayWhenReady(false);
        }
    }

    public void stop() {
        if (isPlaying()) {
            mPlaying = false;
            mPlayer.stop();
            mPlayer.release();
        }
    }
}
