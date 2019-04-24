package com.example.bakingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.bakingapp.model.Recipe;

public class RecipeActivity extends AppCompatActivity {

    // Constant for logging
    private static final String TAG = RecipeActivity.class.getSimpleName();

    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra("Recipe")) {
            // save movie info to member variable
            mRecipe = intentThatStartedThisActivity.getParcelableExtra("Recipe");
            Log.d(TAG, mRecipe.getName());
        }
    }
}
