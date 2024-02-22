package dto;

import java.util.ArrayList;

public class Pizza {
    int id;
    String name;
    String pate;
    int prixBase;

    private ArrayList<dto.Ingredient> ingredients = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getpate() {
        return pate;
    }

    public void setpate(String pate) {
        this.pate = pate;
    }

    public int getPrixBase() {
        return prixBase;
    }

    public void setPrixBase(int prixBase) {
        this.prixBase = prixBase;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public String toString() {
        return "Pizza [id=" + id + ", nom=" + name + ", pate=" + pate + ", prixBase=" + prixBase + ", ingredients="
                + ingredients + "]";
    }

}
