package com.example.bakingapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepActivity extends AppCompatActivity {

    // Constant for logging
    private static final String TAG = StepActivity.class.getSimpleName();
    public static final String STEP_LIST = "steps";

    public static final String CURRENT_INDEX = "current_index";

    ArrayList<Step> mSteps;
    int mCurrentIndex;

    @BindView(R.id.prev_button)
    Button mPrevButton;

    @BindView(R.id.next_button)
    Button mNextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        ButterKnife.bind(this);

        Intent intentThatStartedThisActivity = getIntent();

        if (savedInstanceState != null) {
            mSteps = savedInstanceState.getParcelableArrayList(STEP_LIST);
            mCurrentIndex = savedInstanceState.getInt(CURRENT_INDEX);
        } else if (intentThatStartedThisActivity.hasExtra("Steps") && intentThatStartedThisActivity.hasExtra("CurrentIndex")) {
            // save step info to member variable
            mSteps = intentThatStartedThisActivity.getParcelableArrayListExtra("Steps");
            mCurrentIndex = intentThatStartedThisActivity.getIntExtra("CurrentIndex", 0);
        }

        setTitle(mSteps.get(mCurrentIndex).getShortDescription());

        setButtonsVis(mCurrentIndex);

        // Create StepFragment
        Bundle bundle = new Bundle();
        bundle.putParcelable("Step", mSteps.get(mCurrentIndex));
        StepFragment stepFragment = new StepFragment();
        stepFragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.step_container, stepFragment)
                .commit();
    }

    public void onPrevStepClick(View view) {
        mCurrentIndex--;
        setTitle(mSteps.get(mCurrentIndex).getShortDescription());
        setButtonsVis(mCurrentIndex);
        // Create StepFragment
        Bundle bundle = new Bundle();
        bundle.putParcelable("Step", mSteps.get(mCurrentIndex));
        StepFragment stepFragment = new StepFragment();
        stepFragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.step_container, stepFragment)
                .commit();
    }

    public void onNextStepClick(View view) {
        mCurrentIndex++;
        setTitle(mSteps.get(mCurrentIndex).getShortDescription());
        setButtonsVis(mCurrentIndex);
        // Create StepFragment
        Bundle bundle = new Bundle();
        bundle.putParcelable("Step", mSteps.get(mCurrentIndex));
        StepFragment stepFragment = new StepFragment();
        stepFragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.step_container, stepFragment)
                .commit();
    }

    public void setButtonsVis(int index) {
        mPrevButton.setVisibility(View.VISIBLE);
        mNextButton.setVisibility(View.VISIBLE);
        if (index == (mSteps.size() -1)) {
            mNextButton.setVisibility(View.INVISIBLE);
        }
        if (index == 0) {
            mPrevButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STEP_LIST, mSteps);
        outState.putInt(CURRENT_INDEX, mCurrentIndex);
    }
}
