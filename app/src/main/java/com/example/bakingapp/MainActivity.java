package com.example.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.bakingapp.model.Recipe;
import com.example.bakingapp.network.RecipeService;
import com.example.bakingapp.network.RetrofitClientInstance;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecipeService service;

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
                    for (Recipe r : response.body()) {
                        Log.d("MainActivity", r.getName());
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
}
