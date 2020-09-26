package com.sab.recipesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.ForwardingListener;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sab.recipesapp.models.Recipe;
import com.sab.recipesapp.viewmodels.RecipeListViewModel;
import com.sab.recipesapp.viewmodels.RecipeViewModel;

public class RecipeActivity extends BaseActivity {

    private static final String TAG = "RecipeActivity";
    
    private AppCompatImageView mRecipeImage;
    private TextView mRecipeTitle;
    private TextView mRecipeRank;
    private LinearLayout mIngredientsContainer;
    private ScrollView mScrollView;

    private RecipeViewModel mRecipeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        mRecipeImage = findViewById(R.id.recipe_image);
        mRecipeRank = findViewById(R.id.recipe_social_score);
        mIngredientsContainer = findViewById(R.id.ingredients_container);
        mScrollView = findViewById(R.id.parent);
        mRecipeTitle = findViewById(R.id.recipe_title);
        mRecipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);

        showProgressBar(true);
        subscribeObserver();
        getIncomingIntent();
    }

    private void getIncomingIntent(){
        if(getIntent().hasExtra("recipe")){
            Recipe recipe = getIntent().getParcelableExtra("recipe");
            Log.d(TAG, "getIncomingIntent: " + recipe.getTitle());
            mRecipeViewModel.searchRecipeById(recipe.getRecipe_id());
        }
    }

    private void subscribeObserver(){
        mRecipeViewModel.getRecipe().observe(this, new Observer<Recipe>() {
            @Override
            public void onChanged(Recipe recipe) {
                if(recipe != null){
//                    for(String ingredient: recipe.getIngredients()){
//                        Log.d(TAG, "onChanged: " + ingredient);
//                    }
                    if(recipe.getRecipe_id().equals(mRecipeViewModel.getRecipeId())) {
                        setRecipeProperty(recipe);
                        mRecipeViewModel.setRecipeRetrieved(true);
                    }
                }
            }
        });
        mRecipeViewModel.isRequestTimedOut().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean && !mRecipeViewModel.isRecipeRetrieved()){
                    Log.d(TAG, "onChanged: timed out...");
                    showErrorScreen("Timed out. Check your internet connection.");
                }
            }
        });
    }

    private void showErrorScreen(String errorMessage){
        mRecipeTitle.setText("Error retrieving recipe");
        mRecipeRank.setText("");
        TextView textView = new TextView(this);
        if(errorMessage != ""){
            textView.setText(errorMessage);
        }
        else {
            textView.setText("Error");
        }
        textView.setTextSize(15);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        mIngredientsContainer.addView(textView);

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background);

        Glide.with(this)
                .setDefaultRequestOptions(requestOptions)
                .load(R.drawable.ic_launcher_background)
                .into(mRecipeImage);

        showParent();
        showProgressBar(false);

    }

    private void setRecipeProperty(Recipe recipe){
        if(recipe != null){
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.ic_launcher_background);

            Glide.with(this)
                    .setDefaultRequestOptions(requestOptions)
                    .load(recipe.getImage_url())
                    .into(mRecipeImage);

            mRecipeTitle.setText(recipe.getTitle());
            mRecipeRank.setText(String.valueOf(Math.round(recipe.getSocial_rank())));

            mIngredientsContainer.removeAllViews();
            for(String ingredient : recipe.getIngredients()){
                TextView textView = new TextView(this);
                textView.setText(ingredient);
                textView.setTextSize(15);
                textView.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
                ));
                mIngredientsContainer.addView(textView);
            }

        }
        showParent();
        showProgressBar(false);
    }
    private void showParent(){
        mScrollView.setVisibility(View.VISIBLE);
    }
}