package com.sab.recipesapp.requests.repsonses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sab.recipesapp.models.Recipe;

public class RecipeResponse {

    @SerializedName("recipe")
    @Expose() //serializing/deserializing data
    private Recipe recipe;

    public Recipe getRecipe(){
        return recipe;
    }

    @SerializedName("error")
    @Expose
    private String error;

    public String getError(){
        return error;
    }

    @Override
    public String toString() {
        return "RecipeResponse{" +
                "recipe=" + recipe +
                ", error='" + error + '\'' +
                '}';
    }
}
