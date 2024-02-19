package dao;

import java.util.ArrayList;

import dto.Ingredient;

public class IngredientDAOList implements DAOIngredient {
    ArrayList<Ingredient> ingredients = new ArrayList<>();

    public IngredientDAOList() {
        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId(1);
        ingredient1.setName("chorizo");
        ingredient1.setPrix(10);

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId(2);
        ingredient2.setName("lardons");
        ingredient2.setPrix(20);

        ingredients.add(ingredient1);
        ingredients.add(ingredient2);

    }

    public ArrayList<Ingredient> findAll() {
        return ingredients;
    }

    public Ingredient findById(int id) {
        for (Ingredient element : ingredients) {
            if (element.getId() == id) {
                return element;
            }
        }
        return null;
    }

    public boolean save(int id, String nom, int prix) {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(id);
        ingredient.setName(nom);
        ingredient.setPrix(prix);
        return ingredients.add(ingredient);
    }

}
