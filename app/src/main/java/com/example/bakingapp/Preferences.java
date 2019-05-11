package com.example.bakingapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.bakingapp.model.Recipe;
import com.google.gson.Gson;

public class Preferences {

    // Constant for logging
    private static final String TAG = Preferences.class.getSimpleName();

    public static final String PREFERENCES_NAME = "preferences";

    static Gson gson = new Gson();

    public static void saveRecipe(Context context, Recipe recipe) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE).edit();

        String recipeAsJson = gson.toJson(recipe, Recipe.class);
        Log.d(TAG, "Saved recipe as json: " + recipeAsJson);

        prefs.putString(context.getString(R.string.widget_recipe_key), recipeAsJson);

        prefs.apply();
    }

    public static Recipe loadRecipe(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);

        String recipeAsJson = prefs.getString(context.getString(R.string.widget_recipe_key), "");
        Log.d(TAG, "Loaded recipe as json: " + recipeAsJson);

        Recipe recipeFromJson = gson.fromJson(recipeAsJson, Recipe.class);
        Log.d(TAG, "Recipe from JSON name: " + recipeFromJson.getName());
        Log.d(TAG, "Recipe from JSON first ingredient name: " + recipeFromJson.getIngredients().get(0).getIngredient());
        return recipeFromJson;
    }

}
