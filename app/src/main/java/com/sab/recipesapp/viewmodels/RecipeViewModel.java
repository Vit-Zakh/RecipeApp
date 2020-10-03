package com.sab.recipesapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.sab.recipesapp.models.Recipe;
import com.sab.recipesapp.repositories.RecipeRepository;
import com.sab.recipesapp.util.Resource;

public class RecipeViewModel extends AndroidViewModel {
    private RecipeRepository mRecipeRepository;


    public RecipeViewModel(@NonNull Application application) {
        super(application);
        mRecipeRepository = RecipeRepository.getInstance(application);
    }

    public LiveData<Resource<Recipe>> searchRecipeApi(String recipeApi){
        return mRecipeRepository.searchRecipesApi(recipeApi);
    }
}
