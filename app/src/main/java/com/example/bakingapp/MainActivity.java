package com.example.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.bakingapp.model.Recipe;
import com.example.bakingapp.model.Step;
import com.example.bakingapp.network.RecipeService;
import com.example.bakingapp.network.RetrofitClientInstance;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.ListItemClickListener {

    // Constant for logging
    private static final String TAG = MainActivity.class.getSimpleName();

    private RecipeService service;

    @BindView(R.id.recyclerview_recipes)
    RecyclerView mRecyclerView;

    private RecipeAdapter mRecipeAdapter;

    private ArrayList<Recipe> mRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Lay out recipes in a linear layout
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecipeAdapter = new RecipeAdapter(this);
        mRecyclerView.setAdapter(mRecipeAdapter);

        service = RetrofitClientInstance.getRetrofitInstance().create(RecipeService.class);
        getRecipes();

        Recipe recipeFromPreferences = Preferences.loadRecipe(this);
    }

    private void getRecipes() {
        Call<ArrayList<Recipe>> call = service.getRecipes();
        call.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                if (response.isSuccessful()) {
                    mRecipes = response.body();
                    // Iterate through list of Recipes and assign each Recipe a thumbnail url
                    ArrayList<String> recipeNames = new ArrayList<String>();
                    ArrayList<String> thumbnailUrls = new ArrayList<String>();
                    for (Recipe r : response.body()) {
                        recipeNames.add(r.getName());
                        thumbnailUrls.add(getRecyclerViewThumbnailUrl(r));
                    }
                    // Populate RecyclerView
                    mRecipeAdapter.setmRecipeNames(recipeNames);
                    mRecipeAdapter.setmThumbnailUrls(thumbnailUrls);
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(MainActivity.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Something went wrong... Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Searches within  a Recipe for a thumbnail url to use
     *
     * @param recipe
     * @return url to use for RecyclerView thumbnails
     */
    private String getRecyclerViewThumbnailUrl(Recipe recipe) {
        // If an image is not included for the recipe, get the first image available from within the Recipe's steps
        if (Strings.isNullOrEmpty(recipe.getImage())) {
            // Use Google Guava to find first Step with a thumbnailURL
            Step stepWithVideoThumbnail = Iterables.tryFind(recipe.getSteps(),
                    new Predicate<Step>() {
                        @Override
                        public boolean apply(Step input) {
                            return !input.getVideoUrl().isEmpty() && input.getVideoUrl() != null;
                        }
                    }).orNull();
            // If there isn't a thumbnailURL, use the first available videoURL
            Step stepWithThumbnail = Iterables.tryFind(recipe.getSteps(),
                    new Predicate<Step>() {
                        @Override
                        public boolean apply(Step input) {
                            return !input.getThumbnailUrl().isEmpty() && input.getThumbnailUrl() != null;
                        }
                    }).orNull();

            if (stepWithThumbnail != null)
                return stepWithThumbnail.getThumbnailUrl();
            else
                return stepWithVideoThumbnail.getVideoUrl();
        } else {
            // If an image exists for a Recipe, return that image
            return recipe.getImage();
        }
    }

    /**
     * Use the list index to define the Recipe object that is passed as an extra to the detail activity.
     * Recipe object implements parcelable in order to be passed as extra.
     * @param clickedItemIndex
     */
    @Override
    public void onListItemClick(int clickedItemIndex) {
        Context context = MainActivity.this;
        Class destinationActivity = RecipeActivity.class;
        Intent startRecipeActivityIntent = new Intent(context, destinationActivity);
        Recipe recipeToPass = mRecipes.get(clickedItemIndex);
        startRecipeActivityIntent.putExtra("Recipe", recipeToPass);
        startActivity(startRecipeActivityIntent);
    }
}
