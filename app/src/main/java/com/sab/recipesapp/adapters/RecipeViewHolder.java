package com.sab.recipesapp.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.sab.recipesapp.R;

public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView title;
    TextView publisher;
    TextView socialScore;
    AppCompatImageView image;
    OnRecipeClickListener onRecipeClickListener;

    public RecipeViewHolder(@NonNull View itemView, OnRecipeClickListener onRecipeClickListener) {
        super(itemView);
        title = itemView.findViewById(R.id.recipe_title);
        publisher = itemView.findViewById(R.id.recipe_publisher);
        socialScore = itemView.findViewById(R.id.recipe_social_score);
        image = itemView.findViewById(R.id.recipe_image);
        this.onRecipeClickListener = onRecipeClickListener;

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        onRecipeClickListener.onRecipeClick(getAdapterPosition());
    }
}
