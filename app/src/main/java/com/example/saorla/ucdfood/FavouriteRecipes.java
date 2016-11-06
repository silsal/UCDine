package com.example.saorla.ucdfood;

//This creates instances of a user's favourite recipes
public class FavouriteRecipes {

    private int _recipeid;
    private int _uid;
    private String recipe_url;

    public FavouriteRecipes(){

    }

    public FavouriteRecipes(int _recipeid) {
        this._recipeid = _recipeid;
    }

    public int get_recipeid() {
        return _recipeid;
    }

    public int get_uid() {
        return _uid;
    }

    public String getRecipe_url() {
        return recipe_url;
    }

    public void set_recipeid(int _recipeid) {
        this._recipeid = _recipeid;
    }

    public void set_uid(int _uid) {
        this._uid = _uid;
    }

    public void setRecipe_url(String recipe_url) {
        this.recipe_url = recipe_url;
    }
}


