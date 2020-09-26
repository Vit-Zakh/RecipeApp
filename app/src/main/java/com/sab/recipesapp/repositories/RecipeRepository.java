package com.sab.recipesapp.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.sab.recipesapp.models.Recipe;
import com.sab.recipesapp.requests.RecipeApiClient;

import java.util.List;

public class RecipeRepository {

    private static RecipeRepository instance;
    private RecipeApiClient mRecipeApiClient;
    private String mQuery;
    private int mPageNumber;
    private MutableLiveData<Boolean> mQueryExhausted = new MutableLiveData<>();
    private MediatorLiveData<List<Recipe>> mRecipes = new MediatorLiveData<>();

    public static RecipeRepository getInstance(){
        if(instance == null){
            instance = new RecipeRepository();
        }
        return instance;
    }

    private RecipeRepository() {
        mRecipeApiClient = RecipeApiClient.getInstance();
        initMediators();
    }
    public LiveData<List<Recipe>> getRecipes(){
        return mRecipes;
    }
    public LiveData<Recipe> getRecipe(){return mRecipeApiClient.getRecipe();}
    public LiveData<Boolean> isRequestTimedOut(){
        return mRecipeApiClient.isRequestTimedOut();
    }
    public LiveData<Boolean> isQueryExhausted(){
        return mQueryExhausted;
    }

    private void initMediators(){
        LiveData<List<Recipe>> recipeListApiSource = mRecipeApiClient.getRecipes();
        mRecipes.addSource(recipeListApiSource, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                if(recipes != null){
                    mRecipes.setValue(recipes);
                    doneQuery(recipes);
                }
                else {
                    //search db cache
                    doneQuery(null);
                }
            }
        });
    }

    private void doneQuery(List<Recipe> list){
        if(list != null){
            if(list.size() % 30 != 0){
                mQueryExhausted.setValue(true);
            }
        }
        else mQueryExhausted.setValue(true);
    }


    public void searchRecipeById(String recipeId){
        mRecipeApiClient.searchRecipeByIdApi(recipeId);
    }

    public void searchRecipesApi(String query, int pageNumber){
       if(pageNumber == 0){
           pageNumber = 1;
       }
       mQuery = query;
       mPageNumber = pageNumber;
       mQueryExhausted.setValue(false);
       mRecipeApiClient.searchRecipesApi(query, pageNumber);
    }

    public void searchNextPage(){
        searchRecipesApi(mQuery, mPageNumber + 1);
    }

    public void cancelRequest(){
        mRecipeApiClient.cancelRequest();
    }

}

