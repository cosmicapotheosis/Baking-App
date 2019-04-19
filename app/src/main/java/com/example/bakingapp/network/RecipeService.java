package com.example.bakingapp.network;

import com.example.bakingapp.model.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipeService {

    @GET("/")
    Call<ArrayList<Recipe>> getRecipes();

}
