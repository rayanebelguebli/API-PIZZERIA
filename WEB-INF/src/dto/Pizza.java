package dto;

import java.util.ArrayList;

public class Pizza {
    int id;
    String nom;
    String pâte;
    int prixBase;
    ArrayList<Ingredient> ingredients = new ArrayList<>();
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public String getPâte() {
        return pâte;
    }
    public void setPâte(String pâte) {
        this.pâte = pâte;
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


}
