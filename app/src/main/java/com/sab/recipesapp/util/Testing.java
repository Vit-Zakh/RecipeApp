package com.sab.recipesapp.util;

import android.util.Log;

import com.sab.recipesapp.models.Recipe;

import java.util.List;

public class Testing {
    public static void printRecipes(List<Recipe> list, String tag){
        for(Recipe recipe : list){
            Log.d(tag, "onChanged: " + recipe.getTitle());
        }
    }
}
