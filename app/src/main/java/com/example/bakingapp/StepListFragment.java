package com.example.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bakingapp.model.Step;

import java.util.ArrayList;

public class StepListFragment extends Fragment
        implements StepAdapter.ListItemClickListener {

    // Constant for logging
    private static final String TAG = StepListFragment.class.getSimpleName();

    OnStepClickListener mCallback;

    public interface OnStepClickListener {
        void onStepSelected(int position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                + " must implement OnStepClickListener");
        }
    }

    public StepListFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_step_list, container, false);

        ArrayList<Step> steps = getArguments().getParcelableArrayList("steps");
        StepAdapter stepAdapter = new StepAdapter(this);
        stepAdapter.setmSteps(steps);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_steps);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(stepAdapter);

        return rootView;
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        mCallback.onStepSelected(clickedItemIndex);
    }
}
