package com.sab.recipesapp.adapters;

import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.sab.recipesapp.R;
import com.sab.recipesapp.models.Recipe;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    CircleImageView categoryImage;
    TextView categoryTitle;
    OnRecipeClickListener mListener;
    RequestManager requestManager;

    public CategoryViewHolder(@NonNull View itemView,
                              OnRecipeClickListener listener,
                              RequestManager requestManager) {
        super(itemView);

        this.mListener = listener;
        this.requestManager = requestManager;
        categoryImage = itemView.findViewById(R.id.category_image);
        categoryTitle = itemView.findViewById(R.id.category_title);

        itemView.setOnClickListener(this);
    }

    public void onBind(Recipe recipe){
        Uri path = Uri.parse("android.resource://com.sab.recipesapp/drawable/" + recipe.getImage_url());
        requestManager
                .load(path)
                .into(categoryImage);

        categoryTitle.setText(recipe.getTitle());
    }

    @Override
    public void onClick(View view) {
        mListener.onCategoryClick(categoryTitle.getText().toString());
    }
}
