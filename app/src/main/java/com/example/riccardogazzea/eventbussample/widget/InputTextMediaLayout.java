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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baseandroid.events.Event;
import com.baseandroid.events.EventDispatcher;
import com.baseandroid.events.rx.annotations.RxSubscribe;
import com.example.riccardogazzea.eventbussample.ExoPlayerManager;
import com.example.riccardogazzea.eventbussample.R;
import com.example.riccardogazzea.eventbussample.events.ContextOnPauseEvent;
import com.example.riccardogazzea.eventbussample.events.ContextOnResumeEvent;
import com.example.riccardogazzea.eventbussample.model.MediaModel;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import static com.google.android.exoplayer2.Player.REPEAT_MODE_ONE;
import static com.google.android.exoplayer2.ui.AspectRatioFrameLayout.RESIZE_MODE_FILL;

/**
 * Created on 13/02/18.
 *
 * @author Umberto Marini
 */
public class InputTextMediaLayout extends RelativeLayout {

    private MediaModel mMediaModel;

    ExtractorMediaSource mMediaSource;

    private SimpleExoPlayerView mSimpleExoPlayerView;
    private ImageView mPictureView;
    private EditText mMainTextView;
    private EditText mSecondaryTextView;
    private ImageButton mVolumeImageButton;
    private ImageButton mProceedImageButton;

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

    private OnClickListener onProceedClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            stop();

            OnDoneClickEvent event = new OnDoneClickEvent(mMainTextView.getText(), mSecondaryTextView.getText(), mMediaModel);
            EventDispatcher.post(event);
        }
    };

    public InputTextMediaLayout(Context context) {
        super(context);
        init(context);
    }

    public InputTextMediaLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public InputTextMediaLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public InputTextMediaLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_videoinputtext, this, true);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setBackgroundColor(ContextCompat.getColor(context, android.R.color.black));

        mPictureView = findViewById(R.id.videoinputtextlayout_picture);
        mMainTextView = findViewById(R.id.videoinputtextlayout_main_text);
        mSecondaryTextView = findViewById(R.id.videoinputtextlayout_secondary_text);
        mProceedImageButton = findViewById(R.id.videoinputtextlayout_proceed);
        mVolumeImageButton = findViewById(R.id.videoinputtextlayout_volume);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventDispatcher.register(this);
        mVolumeImageButton.setOnClickListener(onVolumeClickListener);
        mProceedImageButton.setOnClickListener(onProceedClickListener);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventDispatcher.unregister(this);
        mVolumeImageButton.setOnClickListener(null);
        mProceedImageButton.setOnClickListener(null);

        stop();
    }

    @RxSubscribe
    public void onConsumeEvent(ContextOnResumeEvent event) {
        play();
    }

    @RxSubscribe
    public void onConsumeEvent(ContextOnPauseEvent event) {
        pause();
    }

    public void setMediaModel(MediaModel mediaModel) {
        mMediaModel = mediaModel;

        if (mMediaModel != null) {
            if (mMediaModel.isVideo()) {
                play();
                mVolumeImageButton.setVisibility(VISIBLE);
            } else if (mMediaModel.isPicture()) {
                Picasso.with(getContext())
                        .load(mMediaModel.getUrl())
                        .into(mPictureView);
            }
        }
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

    private void notifyAudioStateChange() {
        if (isAudioPlaying()) {
            mVolumeImageButton.setImageResource(R.drawable.ic_volume_up_white_24dp);
        } else {
            mVolumeImageButton.setImageResource(R.drawable.ic_volume_off_white_24dp);
        }
    }

    private boolean isAudioPlaying() {
        return ExoPlayerManager.getInstance().getPlayer().getVolume() > 0f;
    }

    private boolean prepare() {
        if (mMediaModel != null) {

            if (mSimpleExoPlayerView == null) {
                final ViewStub viewStub = findViewById(R.id.videoinputtextlayout_player_stub);
                if (viewStub != null) {
                    mSimpleExoPlayerView = (SimpleExoPlayerView) viewStub.inflate();
                } else {
                    mSimpleExoPlayerView = findViewById(R.id.videoinputtextlayout_player);
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
            ExoPlayerManager.getInstance().getPlayer().setRepeatMode(REPEAT_MODE_ONE);
            return true;
        } else {
            return false;
        }
    }

    public void play() {
        if (prepare()) {
            ExoPlayerManager.getInstance().getPlayer().setPlayWhenReady(true);
        } else {
            Toast.makeText(getContext(), "Ops... Something went wrong!", Toast.LENGTH_SHORT).show();
        }
    }

    public void pause() {
        if (ExoPlayerManager.getInstance().getPlayer() != null) {
            mLastPlaybackPosition = ExoPlayerManager.getInstance().getPlayer().getCurrentPosition();
            ExoPlayerManager.getInstance().getPlayer().setPlayWhenReady(false);
        }
    }

    public void stop() {
        if (ExoPlayerManager.getInstance().getPlayer() != null) {
            mLastPlaybackPosition = 0L;
            ExoPlayerManager.getInstance().getPlayer().stop();
        }

        if (mMediaSource != null) {
            mMediaSource.releaseSource();
            mMediaSource = null;
        }
    }

    @Event(type = Event.Type.UI)
    public class OnDoneClickEvent {

        private CharSequence mMainText;
        private CharSequence mSecondaryText;
        private MediaModel mMediaModel;

        public OnDoneClickEvent(CharSequence mainText, CharSequence secondaryText, MediaModel mediaModel) {
            mMainText = mainText;
            mSecondaryText = secondaryText;
            mMediaModel = mediaModel;
        }

        public CharSequence getMainText() {
            return mMainText;
        }

        public CharSequence getSecondaryText() {
            return mSecondaryText;
        }

        public MediaModel getMediaModel() {
            return mMediaModel;
        }
    }
}
