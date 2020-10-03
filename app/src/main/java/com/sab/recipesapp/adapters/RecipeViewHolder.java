package com.sab.recipesapp.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.util.ViewPreloadSizeProvider;
import com.sab.recipesapp.R;
import com.sab.recipesapp.models.Recipe;

public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView title;
    TextView publisher;
    TextView socialScore;
    AppCompatImageView image;
    OnRecipeClickListener onRecipeClickListener;
    RequestManager requestManager;
    ViewPreloadSizeProvider preloadSizeProvider;

    public RecipeViewHolder(@NonNull View itemView,
                            OnRecipeClickListener onRecipeClickListener,
                            RequestManager requestManager,
                            ViewPreloadSizeProvider preloadSizeProvider) {
        super(itemView);
        title = itemView.findViewById(R.id.recipe_title);
        publisher = itemView.findViewById(R.id.recipe_publisher);
        socialScore = itemView.findViewById(R.id.recipe_social_score);
        image = itemView.findViewById(R.id.recipe_image);
        this.onRecipeClickListener = onRecipeClickListener;
        this.requestManager = requestManager;
        this.preloadSizeProvider = preloadSizeProvider;

        itemView.setOnClickListener(this);
    }

    public void onBind(Recipe recipe){
        requestManager.load(recipe.getImage_url())
                .into(image);
        title.setText(recipe.getTitle());
        publisher.setText(recipe.getPublisher());
        socialScore.setText(String.valueOf(Math.round(recipe.getSocial_rank())));

        preloadSizeProvider.setView(image);

    }

    @Override
    public void onClick(View view) {
        onRecipeClickListener.onRecipeClick(getAdapterPosition());
    }
}
