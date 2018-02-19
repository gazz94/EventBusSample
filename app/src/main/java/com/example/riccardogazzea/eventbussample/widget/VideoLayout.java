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
import android.view.ViewStub;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baseandroid.events.EventDispatcher;
import com.baseandroid.events.rx.annotations.RxSubscribe;
import com.example.riccardogazzea.eventbussample.ExoPlayerManager;
import com.example.riccardogazzea.eventbussample.NetworkUtility;
import com.example.riccardogazzea.eventbussample.R;
import com.example.riccardogazzea.eventbussample.events.ContextOnPauseEvent;
import com.example.riccardogazzea.eventbussample.events.UiRecyclerStateIdleEvent;
import com.example.riccardogazzea.eventbussample.events.UiRecyclerStateNotIdleEvent;
import com.example.riccardogazzea.eventbussample.model.VideoMediaModel;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;

import static com.google.android.exoplayer2.ui.AspectRatioFrameLayout.RESIZE_MODE_FILL;

/**
 * Created on 13/02/18.
 *
 * @author Umberto Marini
 */
public class VideoLayout extends RelativeLayout {

    private VideoMediaModel mMediaModel;

    ExtractorMediaSource mMediaSource;

    private SimpleExoPlayerView mSimpleExoPlayerView;
    private ImageView mPlaceholderImageView;
    private ImageButton mPlaybackImageButton, mVolumeImageButton;

    private boolean mPlaying = false;

    private long mLastPlaybackPosition = 0;

    private OnClickListener onVolumeClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isAudioPlaying()) {
                ExoPlayerManager.getInstance().getPlayer().setVolume(0);

                notifyAudioStateChange();
            } else {
                ExoPlayerManager.getInstance().getPlayer().setVolume(1);

                notifyAudioStateChange();
            }
        }
    };

    private OnClickListener onPlayClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mPlaying) {
                pause();
            } else {
                play();
            }
        }
    };

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

        mPlaceholderImageView = findViewById(R.id.videolayout_placeholder_image);
        mPlaybackImageButton = findViewById(R.id.videolayout_play);
        mVolumeImageButton = findViewById(R.id.videolayout_volume);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventDispatcher.register(this);
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
                if (NetworkUtility.getNetworkType(getContext()) == NetworkUtility.TYPE_WIFI) {
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

    private void notifyPlayStateChange() {
        if (mPlaying) {
            mPlaceholderImageView.animate().alpha(0f).setDuration(150).start();
            mPlaybackImageButton.setImageResource(R.drawable.ic_pause_white_24dp);
            //show volume btn
            mVolumeImageButton.setVisibility(VISIBLE);
        } else {
            mPlaceholderImageView.animate().alpha(1f).setDuration(150).start();
            mPlaybackImageButton.setImageResource(R.drawable.ic_play_arrow_white_24dp);
            //hide volume btn
            mVolumeImageButton.setVisibility(INVISIBLE);
        }
    }

    private void notifyAudioStateChange() {
        if (isAudioPlaying()) {
            mVolumeImageButton.setImageResource(R.drawable.ic_volume_up_white_24dp);
        } else {
            mVolumeImageButton.setImageResource(R.drawable.ic_volume_off_white_24dp);
        }
    }

    private boolean isPlaying() {
        return mPlaying;
    }

    private boolean isAudioPlaying() {
        return ExoPlayerManager.getInstance().getPlayer().getVolume() > 0f;
    }

    private boolean prepare() {
        if (mMediaModel != null) {

            if (mSimpleExoPlayerView == null) {
                final ViewStub viewStub = findViewById(R.id.videolayout_player_stub);
                if (viewStub != null) {
                    mSimpleExoPlayerView = (SimpleExoPlayerView) viewStub.inflate();
                } else {
                    mSimpleExoPlayerView = findViewById(R.id.videolayout_player);
                }
                //mSimpleExoPlayerView.setPlayer(mPlayer);
            }
            ExoPlayerManager.getInstance().switchTargetView(mSimpleExoPlayerView);
            mSimpleExoPlayerView.setUseController(false);
            mSimpleExoPlayerView.setResizeMode(RESIZE_MODE_FILL);
            // Prepare the player with the source.
            if (mMediaSource != null) {
                mMediaSource.releaseSource();
                mMediaSource = null;
            }
            mMediaSource = new ExtractorMediaSource.Factory(buildDataSourceFactory(true)).createMediaSource(Uri.parse(mMediaModel.getUrl()));
            ExoPlayerManager.getInstance().getPlayer().prepare(mMediaSource);
            ExoPlayerManager.getInstance().getPlayer().seekTo(mLastPlaybackPosition);
            return true;
        } else {
            return false;
        }
    }

    public void play() {
        if (prepare()) {

            mPlaying = true;
            ExoPlayerManager.getInstance().getPlayer().setPlayWhenReady(true);

            notifyPlayStateChange();
        } else {
            Toast.makeText(getContext(), "Ops... Something went wrong!", Toast.LENGTH_SHORT).show();
        }
    }

    public void pause() {
        if (ExoPlayerManager.getInstance().getPlayer() != null) {
            if (mPlaying) {
                mLastPlaybackPosition = ExoPlayerManager.getInstance().getPlayer().getCurrentPosition();
                mPlaying = false;
                ExoPlayerManager.getInstance().getPlayer().setPlayWhenReady(false);

                notifyPlayStateChange();
            }
        }
    }

    public void stop() {
        if (ExoPlayerManager.getInstance().getPlayer() != null) {
            mLastPlaybackPosition = 0L;
            mPlaying = false;

            ExoPlayerManager.getInstance().getPlayer().stop();
            //mPlayer.release();
            //mPlayer = null;
        }

        if (mMediaSource != null) {
            mMediaSource.releaseSource();
            mMediaSource = null;
        }

        notifyPlayStateChange();
    }

    private void showControllers(boolean show) {
        //show the controllers on a video at time
        if (show) {
            mPlaybackImageButton.setVisibility(VISIBLE);
            mVolumeImageButton.setVisibility(isPlaying()? VISIBLE : INVISIBLE);
            notifyAudioStateChange();
        } else {
            mPlaybackImageButton.setVisibility(INVISIBLE);
            mVolumeImageButton.setVisibility(INVISIBLE);
        }
    }
}
