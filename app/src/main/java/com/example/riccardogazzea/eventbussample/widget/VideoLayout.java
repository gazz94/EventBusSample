package com.example.riccardogazzea.eventbussample.widget;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baseandroid.events.EventDispatcher;
import com.baseandroid.events.rx.annotations.RxSubscribe;
import com.example.riccardogazzea.eventbussample.NetworkUtility;
import com.example.riccardogazzea.eventbussample.R;
import com.example.riccardogazzea.eventbussample.events.ContextOnPauseEvent;
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
    private ImageButton mPlaybackImageButton, mVolumeImageButton;
    private boolean mPlaying = false;
    //hold track of the audio state
    private boolean mAudioPlaying = false;

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
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        // 2. Create the player
        mPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
        mPlayer.setPlayWhenReady(false);
        mSimpleExoPlayerView = findViewById(R.id.videolayout_player);
        mSimpleExoPlayerView.setPlayer(mPlayer);
        //hide the video controller
        mSimpleExoPlayerView.setUseController(false);
        //set volume off
        //mPlayer.setVolume(0);
        mPlaceholderImageView = findViewById(R.id.videolayout_placeholder_image);
        mTextTextView = findViewById(R.id.videolayout_test_text);
        mPlaybackImageButton = findViewById(R.id.videolayout_play);
        mVolumeImageButton = findViewById(R.id.videolayout_volume);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventDispatcher.register(this);
        // TODO: 14/02/2018 Gazz -> non sono sicuro della scelta del listener
        mVolumeImageButton.setOnClickListener(onVolumeClickListener);
        mPlaybackImageButton.setOnClickListener(onPlayClickListener);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventDispatcher.unregister(this);
        mVolumeImageButton.setOnClickListener(null);
        mPlaybackImageButton.setOnClickListener(null);
    }

    @RxSubscribe
    public void onConsumeEvent(UiRecyclerStateIdleEvent event) {
        if (mMediaModel != null) {
            if (mMediaModel.equals(event.getTag())) {
                if (NetworkUtility.getNetworkType(getContext()) == NetworkUtility.TYPE_WIFI){
                    play();
                }
                showControllers(true);
            } else {
                pause();
                showControllers(false);
            }
        }
    }

    @RxSubscribe
    public void onConsumeEvent(UiRecyclerStateNotIdleEvent event) {
        pause();
    }

    @RxSubscribe
    public void onConsumeEvent(ContextOnPauseEvent event) {
        pause();
    }

    public void setVideoMediaModel(VideoMediaModel mediaModel) {
        mMediaModel = mediaModel;
        ExtractorMediaSource mediaSource = new ExtractorMediaSource.Factory(buildDataSourceFactory(true)).createMediaSource(
                Uri.parse(mMediaModel.getUrl()));
        // Prepare the player with the source.
        mPlayer.prepare(mediaSource);
    }

    private DataSource.Factory buildDataSourceFactory(boolean useBandwidthMeter) {
        return buildDataSourceFactory(useBandwidthMeter ? new DefaultBandwidthMeter() : null);
    }

    private DataSource.Factory buildDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultDataSourceFactory(getContext(), bandwidthMeter, buildHttpDataSourceFactory(bandwidthMeter));
    }

    public HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultHttpDataSourceFactory(Util.getUserAgent(getContext(), "EventBusSample"), bandwidthMeter);
    }

    private boolean isPlaying() {
        return mPlaying;
    }

    private boolean isAudioPlaying() {
        return mAudioPlaying;
    }

    public void play() {
        if (mPlayer != null) {
            mPlaying = true;
            mPlayer.setPlayWhenReady(true);
            //change the icon
            mPlaybackImageButton.setImageResource(R.drawable.exo_controls_pause);
        }
    }

    public void pause() {
        if (isPlaying()) {
            mPlaying = false;
            mPlayer.setPlayWhenReady(false);
            //change the icon
            mPlaybackImageButton.setImageResource(R.drawable.exo_controls_play);
        }
    }

    public void stop() {
        if (isPlaying()) {
            mPlaying = false;
            mPlayer.stop();
            mPlayer.release();
        }
    }

    private OnClickListener onVolumeClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isAudioPlaying()){
                mAudioPlaying = false;
                mPlayer.setVolume(0);
                //change the volume icon
                mVolumeImageButton.setImageResource(android.R.drawable.ic_lock_silent_mode);
            }else{
                mAudioPlaying = true;
                mPlayer.setVolume(1);
                //change the volume icon
                mVolumeImageButton.setImageResource(android.R.drawable.ic_lock_silent_mode_off);
            }
        }
    };

    private OnClickListener onPlayClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isPlaying()){
                pause();
            }else{
                play();
            }
        }
    };

    private void showControllers(boolean show){
        //show the controllers on a video at time
        if (show){
            mPlaybackImageButton.setVisibility(VISIBLE);
            mVolumeImageButton.setVisibility(VISIBLE);
        }else{
            mPlaybackImageButton.setVisibility(INVISIBLE);
            mVolumeImageButton.setVisibility(INVISIBLE);
        }
    }
}
