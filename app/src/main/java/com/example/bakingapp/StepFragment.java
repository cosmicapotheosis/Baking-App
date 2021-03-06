package com.example.bakingapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.bakingapp.model.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.common.io.Files;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepFragment extends Fragment {

    // Constant for logging
    private static final String TAG = StepFragment.class.getSimpleName();
    public static final String STEP = "step";
    public static final String PLAYER_POSITION = "playerposition";
    public static final String PLAYER_STATE = "playerstate";

    @BindView(R.id.playerView)
    SimpleExoPlayerView mPlayerView;
    private SimpleExoPlayer mExoPlayer;

    @BindView(R.id.tv_step_desc)
    TextView mDescTextView;

    Step mStep;

    Boolean mPlayWhenReady = true;
    Long mPlayerPosition = 0L;

    public StepFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step, container, false);
        ButterKnife.bind(this, rootView);

        if(savedInstanceState != null) {
            mStep = savedInstanceState.getParcelable(STEP);
            mPlayWhenReady = savedInstanceState.getBoolean(PLAYER_STATE);
            mPlayerPosition = savedInstanceState.getLong(PLAYER_POSITION);
            Log.d(TAG, "Saved position: " + mPlayerPosition);
            Log.d(TAG, "Saved state: " + mPlayWhenReady);
        } else {
            mStep = getArguments().getParcelable("Step");
        }

        mDescTextView.setText(mStep.getDescription());
        mPlayerView.setVisibility(View.INVISIBLE);

        // Initialize the player if the url is a video
        String fileExt = Files.getFileExtension(mStep.getVideoUrl());
        if(fileExt.equals("mp4")) {
            Log.d(TAG, "initializing player");
            initializePlayer(Uri.parse(mStep.getVideoUrl()));
        }

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer(Uri.parse(mStep.getVideoUrl()));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || mExoPlayer == null) {
            initializePlayer(Uri.parse(mStep.getVideoUrl()));
        }
    }

    /**
     * Initialize ExoPlayer.
     *
     * @param mediaUri The URI of the sample to play.
     */
    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);
            mPlayerView.setVisibility(View.VISIBLE);
            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.seekTo(mPlayerPosition);
            mExoPlayer.setPlayWhenReady(mPlayWhenReady);
        } else {
            mExoPlayer.seekTo(mPlayerPosition);
            mExoPlayer.setPlayWhenReady(mPlayWhenReady);
        }
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }


//    /**
//     * Release the player when the activity is destroyed.
//     */
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        releasePlayer();
//    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            mPlayerPosition = mExoPlayer.getCurrentPosition();
            mPlayWhenReady = mExoPlayer.getPlayWhenReady();
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            mPlayerPosition = mExoPlayer.getCurrentPosition();
            mPlayWhenReady = mExoPlayer.getPlayWhenReady();
            releasePlayer();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        // Save player position
        outState.putLong(PLAYER_POSITION, mPlayerPosition);
        // Save player state
        outState.putBoolean(PLAYER_STATE, mPlayWhenReady);
        // Save the step data
        outState.putParcelable(STEP, mStep);
    }
}
