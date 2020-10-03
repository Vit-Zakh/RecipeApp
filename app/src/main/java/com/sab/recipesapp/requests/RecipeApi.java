package com.sab.recipesapp.requests;

import androidx.lifecycle.LiveData;

import com.sab.recipesapp.requests.repsonses.ApiResponse;
import com.sab.recipesapp.requests.repsonses.RecipeResponse;
import com.sab.recipesapp.requests.repsonses.RecipeSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipeApi {

    @GET("api/search")
    LiveData<ApiResponse<RecipeSearchResponse>> searchRecipe(
            @Query("key") String key,
            @Query("q") String query,
            @Query("page") String page
    );

    @GET("api/get")
    LiveData<ApiResponse<RecipeResponse>> getRecipe(
            @Query("key") String key,
            @Query("rId") String recipeId
    );
}
