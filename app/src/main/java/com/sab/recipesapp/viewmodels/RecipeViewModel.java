package com.sab.recipesapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.sab.recipesapp.models.Recipe;
import com.sab.recipesapp.repositories.RecipeRepository;

public class RecipeViewModel extends ViewModel {
    private RecipeRepository mRecipeRepository;
    private String mRecipeId;
    private boolean mRecipeRetrieved;

    public RecipeViewModel() {
        mRecipeRepository = RecipeRepository.getInstance();
        mRecipeRetrieved = false;
    }

    public LiveData<Recipe> getRecipe(){
        return mRecipeRepository.getRecipe();
    }

    public void searchRecipeById(String recipeId){
        mRecipeId = recipeId;
        mRecipeRepository.searchRecipeById(recipeId);
    }

    public LiveData<Boolean> isRequestTimedOut(){
        return mRecipeRepository.isRequestTimedOut();
    }


    public String getRecipeId() {
        return mRecipeId;
    }

    public boolean isRecipeRetrieved() {
        return mRecipeRetrieved;
    }

    public void setRecipeRetrieved(boolean isRecipeRetrieved) {
        this.mRecipeRetrieved = isRecipeRetrieved;
    }
}
