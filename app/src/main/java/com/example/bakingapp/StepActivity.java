package com.example.bakingapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepActivity extends AppCompatActivity {

    // Constant for logging
    private static final String TAG = StepActivity.class.getSimpleName();
    // TODO Refactor to pass mSteps and index instead of mStep
    ArrayList<Step> mSteps;
    int mCurrentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);


        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra("Steps") && intentThatStartedThisActivity.hasExtra("CurrentIndex")) {
            // save step info to member variable
            mSteps = intentThatStartedThisActivity.getParcelableArrayListExtra("Steps");
            mCurrentIndex = intentThatStartedThisActivity.getIntExtra("CurrentIndex", 0);

            setTitle(mSteps.get(mCurrentIndex).getShortDescription());

            // Create com.example.bakingapp.StepFragment
            Bundle bundle = new Bundle();
            bundle.putParcelable("Step", mSteps.get(mCurrentIndex));
            StepFragment stepFragment = new StepFragment();
            stepFragment.setArguments(bundle);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.step_container, stepFragment)
                    .commit();
        }
    }
}
