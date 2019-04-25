package com.example.bakingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;

import com.example.bakingapp.model.Ingredient;
import com.example.bakingapp.model.Recipe;
import com.google.common.base.Strings;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeActivity extends AppCompatActivity {

    // Constant for logging
    private static final String TAG = RecipeActivity.class.getSimpleName();

    private Recipe mRecipe;

    @BindView(R.id.iv_recipe)
    ImageView mImageView;

    @BindView(R.id.recyclerview_ingredients)
    RecyclerView mIngredientRecyclerView;

    private IngredientAdapter mIngredientAdapter;

    private ArrayList<Ingredient> mIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        ButterKnife.bind(this);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra("Recipe")) {
            // save movie info to member variable
            mRecipe = intentThatStartedThisActivity.getParcelableExtra("Recipe");
            setTitle(mRecipe.getName());

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

            // Lay out recipes in a linear layout
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            mIngredientRecyclerView.setLayoutManager(layoutManager);
            mIngredientRecyclerView.setHasFixedSize(true);
            mIngredientAdapter = new IngredientAdapter();
            mIngredientRecyclerView.setAdapter(mIngredientAdapter);
            mIngredientAdapter.setmIngredients(mRecipe.getIngredients());

            Log.d(TAG, "First ingredient" + mRecipe.getIngredients().get(0));
        }
    }
}
