package com.sab.recipesapp.requests;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.sab.recipesapp.AppExecutors;
import com.sab.recipesapp.models.Recipe;
import com.sab.recipesapp.requests.repsonses.RecipeResponse;
import com.sab.recipesapp.requests.repsonses.RecipeSearchResponse;
import com.sab.recipesapp.util.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

import static com.sab.recipesapp.util.Constants.NETWORK_TIMEOUT;

public class RecipeApiClient {

    private static final String TAG = "RecipeApiClient";

    private static RecipeApiClient instance;
    private MutableLiveData<List<Recipe>> mRecipes;
    private RetrieveRecipesRunnable mRecipesRunnable;
    private RetrieveRecipeRunnable mRecipeRunnable;
    private MutableLiveData<Recipe> mRecipe;
    private MutableLiveData<Boolean> mRecipeRequestTimeout = new MutableLiveData<>();


    public static RecipeApiClient getInstance() {
        if (instance == null) {
            instance = new RecipeApiClient();
        }
        return instance;
    }

    private RecipeApiClient() {
        mRecipes = new MutableLiveData<>();
        mRecipe = new MutableLiveData<>();
    }

    public LiveData<List<Recipe>> getRecipes() {
        return mRecipes;
    }
    public LiveData<Recipe> getRecipe() {
        return mRecipe;
    }
    public LiveData<Boolean> isRequestTimedOut(){
        return mRecipeRequestTimeout;
    }

    public void searchRecipesApi(String query, int pagenumber) {
        if (mRecipesRunnable != null) {
            mRecipesRunnable = null;
        }
        mRecipesRunnable = new RetrieveRecipesRunnable(query, pagenumber);

        final Future handler = AppExecutors.getInstance().networkIO().submit(mRecipesRunnable);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                handler.cancel(true);
            }
        }, NETWORK_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    public void searchRecipeByIdApi(String recipeId) {
        if (mRecipeRunnable != null) {
            mRecipeRunnable = null;
        }
        mRecipeRunnable = new RetrieveRecipeRunnable(recipeId);

        final Future handler = AppExecutors.getInstance().networkIO().submit(mRecipeRunnable);

        mRecipeRequestTimeout.setValue(false);
        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                mRecipeRequestTimeout.postValue(true);
                handler.cancel(true);
            }
        }, NETWORK_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    private class RetrieveRecipesRunnable implements Runnable {

        private String query;
        private int pageNumber;
        boolean cancelRequest;

        public RetrieveRecipesRunnable(String query, int pageNumber) {
            this.query = query;
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            try {
                Response response = getRecipes(query, pageNumber).execute();
                if (cancelRequest) {
                    return;
                }
                if (response.code() == 200) {
                    List<Recipe> list = new ArrayList<>(((RecipeSearchResponse) response.body()).getRecipes());
                    if (pageNumber == 1) {
                        mRecipes.postValue(list);
                    } else {
                        List<Recipe> currentList = mRecipes.getValue();
                        currentList.addAll(list);
                        mRecipes.postValue(currentList);
                    }
                } else {
                    String error = response.errorBody().toString();
                    Log.e(TAG, "run: " + error);
                    mRecipes.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                mRecipes.postValue(null);
            }
        }

        private Call<RecipeSearchResponse> getRecipes(String query, int pageNumber) {
            return ServiceGenerator.getRecipeApi().searchRecipe(
                    Constants.API_KEY,
                    query,
                    String.valueOf(pageNumber)
            );
        }

        private void cancelRequest() {
            Log.d(TAG, "cancelRequest: cancelling the search request");
            cancelRequest = true;
        }
    }

    private class RetrieveRecipeRunnable implements Runnable {

        private String recipeId;
        boolean cancelRequest;

        public RetrieveRecipeRunnable(String recipeId) {
            this.recipeId = recipeId;
            cancelRequest = false;
        }

        @Override
        public void run() {
            try {
                Response response = getRecipe(recipeId).execute();
                if (cancelRequest) {
                    return;
                }
                if (response.code() == 200) {
                    Recipe recipe = ((RecipeResponse) response.body()).getRecipe();
                    mRecipe.postValue(recipe);
                } else {
                    String error = response.errorBody().toString();
                    Log.e(TAG, "run: " + error);
                    mRecipe.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                mRecipe.postValue(null);
            }
        }

        private Call<RecipeResponse> getRecipe(String recipeId) {
            return ServiceGenerator.getRecipeApi().getRecipe(
                    Constants.API_KEY,
                    recipeId);
        }

        private void cancelRequest() {
            Log.d(TAG, "cancelRequest: cancelling the search request");
            cancelRequest = true;
        }
    }



    public void cancelRequest(){
        if(mRecipesRunnable != null){
            mRecipesRunnable.cancelRequest();
        }
        if(mRecipeRunnable != null){
            mRecipeRunnable.cancelRequest();
        }
    }
}
