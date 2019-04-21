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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecipeService service;

    private Map<Recipe, String> mThumbnailUrlsMap = new HashMap<Recipe, String>();

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
                    // Iterate through list of Recipes and assign each Recipe a thumbnail url
                    for (Recipe r : response.body()) {
                        mThumbnailUrlsMap.put(r, getRecyclerViewThumbnailUrl(r));
                    }
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
}
