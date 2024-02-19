package dao;

import java.util.ArrayList;

import dto.Ingredient;

public interface DAOIngredient {
    public ArrayList<Ingredient> findAll();

    public Ingredient findById(int id);

    public boolean save(int id, String nom, int prix);
}
