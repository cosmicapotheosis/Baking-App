package com.example.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bakingapp.model.Ingredient;
import com.example.bakingapp.model.Recipe;
import com.example.bakingapp.model.Step;
import com.google.common.base.Strings;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeActivity extends AppCompatActivity
    implements StepListFragment.OnStepClickListener {

    // Constant for logging
    private static final String TAG = RecipeActivity.class.getSimpleName();

    private Recipe mRecipe;

    @BindView(R.id.iv_recipe)
    ImageView mImageView;

    @BindView(R.id.recyclerview_ingredients)
    RecyclerView mIngredientRecyclerView;

    private IngredientAdapter mIngredientAdapter;

    private ArrayList<Ingredient> mIngredients;
    private ArrayList<Step> mSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        ButterKnife.bind(this);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra("Recipe")) {
            mRecipe = intentThatStartedThisActivity.getParcelableExtra("Recipe");
            setTitle(mRecipe.getName());
            mSteps = mRecipe.getSteps();

            if (Strings.isNullOrEmpty(mRecipe.getImage())) {
                Picasso.get()
                        .load(R.drawable.download)
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.error)
                        .fit()
                        .into(mImageView);
            } else {
                Picasso.get()
                        .load(mRecipe.getImage())
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.error)
                        .fit()
                        .into(mImageView);
            }

            // Lay out ingredients in a linear layout
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            mIngredientRecyclerView.setLayoutManager(layoutManager);
            mIngredientRecyclerView.setHasFixedSize(true);
            mIngredientAdapter = new IngredientAdapter();
            mIngredientRecyclerView.setAdapter(mIngredientAdapter);
            mIngredientRecyclerView.setNestedScrollingEnabled(false);
            mIngredientAdapter.setmIngredients(mRecipe.getIngredients());

            // Create steps list fragment
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("steps", mSteps);
            StepListFragment stepListFragment = new StepListFragment();
            stepListFragment.setArguments(bundle);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.step_list_container, stepListFragment)
                    .commit();
        }
    }

    /**
     * Start a new Activity when a step list item is clicked
     * @param position
     */
    @Override
    public void onStepSelected(int position) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("Step", mSteps.get(position));

        final Intent intent = new Intent(this, StepActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
