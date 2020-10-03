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
import com.sab.recipesapp.util.Resource;
import com.sab.recipesapp.viewmodels.RecipeListViewModel;
import com.sab.recipesapp.viewmodels.RecipeViewModel;
//import com.sab.recipesapp.viewmodels.RecipeViewModel;

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
//        mRecipeViewModel = new ViewModelProvider
//                (this).get(RecipeViewModel.class);
        mRecipeViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(RecipeViewModel.class);

        showProgressBar(true);
        getIncomingIntent();
    }

    private void getIncomingIntent(){
        if(getIntent().hasExtra("recipe")){
            Recipe recipe = getIntent().getParcelableExtra("recipe");
            Log.d(TAG, "getIncomingIntent: " + recipe.getTitle());
            subscribeObservers(recipe.getRecipe_id());
        }
    }

    private void subscribeObservers(final String recipeId){
        mRecipeViewModel.searchRecipeApi(recipeId).observe(this, new Observer<Resource<Recipe>>() {
            @Override
            public void onChanged(Resource<Recipe> recipeResource) {
                if(recipeResource != null){
                    if(recipeResource.data != null){
                        switch (recipeResource.status){
                            case ERROR:{
                                Log.e(TAG, "onChanged: Error! " + recipeResource.message);
                                showParent();
                                showProgressBar(false);
                                setRecipeProperty(recipeResource.data);
                                break;
                            }
                            case LOADING:{
                            showProgressBar(true);
                                break;
                            }
                            case SUCCESS:{
                                Log.d(TAG, "onChanged: cache has been refreshed.");
                                showParent();
                                showProgressBar(false);
                                setRecipeProperty(recipeResource.data);
                                break;
                            }

                        }
                    }
                }
            }
        });
    }

    private void setRecipeProperty(Recipe recipe){
        if(recipe != null){
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.white_background);

            Glide.with(this)
                    .setDefaultRequestOptions(requestOptions)
                    .load(recipe.getImage_url())
                    .into(mRecipeImage);

            mRecipeTitle.setText(recipe.getTitle());
            mRecipeRank.setText(String.valueOf(Math.round(recipe.getSocial_rank())));
            setIngredientsList(recipe);
        }
        showParent();
        showProgressBar(false);
    }

    private void setIngredientsList(Recipe recipe){
        mIngredientsContainer.removeAllViews();
        if(recipe.getIngredients() != null){
            for(String ingredient : recipe.getIngredients()){
                TextView textView = new TextView(this);
                textView.setText(ingredient);
                textView.setTextSize(15);
                textView.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                mIngredientsContainer.addView(textView);
            }
        }
        else {
            TextView textView = new TextView(this);
            textView.setText("Error retrieving ingredients.\nCheck network connection.");
            mIngredientsContainer.addView(textView);
        }
    }

    private void showParent(){
        mScrollView.setVisibility(View.VISIBLE);
    }
}