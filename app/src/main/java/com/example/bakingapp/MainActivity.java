package com.example.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.bakingapp.model.Recipe;
import com.example.bakingapp.model.Step;
import com.example.bakingapp.network.RecipeService;
import com.example.bakingapp.network.RetrofitClientInstance;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;

import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecipeService service;

    private ArrayList<Recipe> mRecipes;
    private ArrayList<String> mThumbnailUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        service = RetrofitClientInstance.getRetrofitInstance().create(RecipeService.class);
        getRecipes();
    }

    private void getRecipes() {
        Call<ArrayList<Recipe>> call = service.getRecipes();
        call.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                if (response.isSuccessful()) {
                    mRecipes = response.body();
                    mThumbnailUrls = getRecyclerViewThumbnailUrls(mRecipes);
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
     * Iterates through list of Recipes and returns a list of urls for thumbnails corresponding to each recipe
     *
     * @param recipes
     * @return ArrayList of urls to use for RecyclerView thumbnails
     */
    private ArrayList<String> getRecyclerViewThumbnailUrls(ArrayList<Recipe> recipes) {
        ArrayList<String> recyclerViewThumbnailUrls = new ArrayList<String>();

        for (Recipe r : recipes) {
            // If an image is not included for the recipe, get the first images from the recipe's steps
            if (Strings.isNullOrEmpty(r.getImage())) {
                // Use Google Guava to find first Step with a thumbnail
                // If thumbnailURL is empty, use videoURL
                Step stepWithVideoThumbnail = Iterables.tryFind(r.getSteps(),
                        new Predicate<Step>() {
                            @Override
                            public boolean apply(Step input) {
                                return !input.getVideoUrl().isEmpty() && input.getVideoUrl() != null;
                            }
                        }).orNull();

                Step stepWithThumbnail = Iterables.tryFind(r.getSteps(),
                        new Predicate<Step>() {
                            @Override
                            public boolean apply(Step input) {
                                return !input.getThumbnailUrl().isEmpty() && input.getThumbnailUrl() != null;
                            }
                        }).orNull();

                if (stepWithThumbnail != null)
                    recyclerViewThumbnailUrls.add(stepWithThumbnail.getThumbnailUrl());
                else
                    recyclerViewThumbnailUrls.add(stepWithVideoThumbnail.getVideoUrl());
            } else {
                recyclerViewThumbnailUrls.add(r.getImage());
            }
        }
        // Make sure all recipes have a thumbnail
        if (recyclerViewThumbnailUrls.size() == recipes.size()) {
            return recyclerViewThumbnailUrls;
        } else {
            return null;
        }
    }
}
