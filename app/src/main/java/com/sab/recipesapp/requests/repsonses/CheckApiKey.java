package com.sab.recipesapp.requests.repsonses;

public class CheckApiKey {

    protected static boolean isApiKeyValid(RecipeSearchResponse response){
        return response.getError() == null;
    }

    protected static boolean isApiKeyValid(RecipeResponse response){
        return response.getError() == null;
    }

}
