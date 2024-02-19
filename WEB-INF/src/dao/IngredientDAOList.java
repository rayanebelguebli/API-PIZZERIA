package dao;

import java.util.ArrayList;

import dto.Ingredient;

public class IngredientDAOList implements DAOIngredient {
    private ArrayList<Ingredient> list = new ArrayList<>();

    

    public IngredientDAOList() {
        Ingredient i1 = new Ingredient();
        Ingredient i2 = new Ingredient();
        i1.setName("poivons");
        i1.setId(1);
        i1.setPrix(2);
        i2.setName("merguez");
        i2.setId(2);
        i2.setPrix(5);
        list.add(i1);
        list.add(i2);
    }

    public ArrayList<Ingredient> findAll(){
        return this.list;
    }

    public Ingredient findById(int id){
        for(Ingredient i : this.list){
            if(i.getId() == id){
                return i;
            }
        }
        return null;
    }

    //public boolean save(int id, String name, int prix){
    //    if(this.findById(id) != null){
    //        return false;
    //    }
    //    else{
    //        list.add(new Ingredient(id, name, prix));
    //        return true;
    //    }
    //}

    public boolean save(Ingredient i){
        if(this.findById(i.getId()) != null){
            return false;
        }
        else{
            list.add(i);
            return true;
        }
    }

}
