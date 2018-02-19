package com.example.riccardogazzea.eventbussample;

import android.content.Context;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;

/**
 * Created on 14/02/18.
 *
 * @author Umberto Marini
 */
public class ExoPlayerManager {

    private static ExoPlayerManager sInstance;
    private SimpleExoPlayer mPlayer;
    private SimpleExoPlayerView mSimpleExoPlayerView;

    public static void initInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ExoPlayerManager(context);
        }
    }

    public static ExoPlayerManager getInstance() {
        return sInstance;
    }

    private ExoPlayerManager(Context context) {
        // 1. Create a default TrackSelector
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        // 2. Create the player
        mPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
        mPlayer.setPlayWhenReady(false);
        mPlayer.setVolume(0);
    }

    public SimpleExoPlayer getPlayer() {
        return mPlayer;
    }

    public void release() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    public void switchTargetView(SimpleExoPlayerView exoPlayerView){
        if (mSimpleExoPlayerView == null){
            exoPlayerView.setPlayer(mPlayer);
        }else{
            SimpleExoPlayerView.switchTargetView(mPlayer, mSimpleExoPlayerView, exoPlayerView);
        }
        mSimpleExoPlayerView = exoPlayerView;
    }
}
