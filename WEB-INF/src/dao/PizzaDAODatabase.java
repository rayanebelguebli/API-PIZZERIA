package dao;

import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

import dto.Ingredient;
import dto.Pizza;

public class PizzaDAODatabase {

    Connection con;

    public PizzaDAODatabase(Connection con) {
        this.con = con;
    }

    public Pizza findById(int id) {

        IngredientDAODatabase ingredientDAO = new IngredientDAODatabase(con);

        Pizza p = new Pizza();
        try {
            String query = "Select * from pizzas where id=?;";
            java.sql.PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setpate(rs.getString("pate"));
                p.setPrixBase(rs.getInt("prixBase"));
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            String query = "Select idIngredient from pizzasContient where idPizza=?;";
            java.sql.PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            ArrayList<Ingredient> list = new ArrayList<>();
            while (rs.next()) {
                list.add(ingredientDAO.findById(rs.getInt("idIngredient")));
            }

            p.setIngredients(list);
            return p;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public ArrayList<Pizza> findAll() {

        IngredientDAODatabase ingredientDAO = new IngredientDAODatabase(con);
        ArrayList<Pizza> pizzas = new ArrayList<>();
        try {
            String query = "Select * from pizzas;";
            java.sql.PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Pizza p = new Pizza();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setpate(rs.getString("pate"));
                p.setPrixBase(rs.getInt("prixBase"));
                try {
                    String query2 = "Select idIngredient from pizzasContient where idPizza=?;";
                    java.sql.PreparedStatement ps2 = con.prepareStatement(query2);
                    ps2.setInt(1, rs.getInt("id"));
                    ResultSet rs2 = ps2.executeQuery();
                    ArrayList<Ingredient> list = new ArrayList<>();
                    while (rs2.next()) {
                        list.add(ingredientDAO.findById(rs2.getInt("idIngredient")));
                    }
                    p.setIngredients(list);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
                pizzas.add(p);
            }
            return pizzas;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public boolean save(Pizza p) {
        try {
            String query = "INSERT INTO pizzas (name,pate,prixBase) VALUES ( ?, ?, ?) ;";
            java.sql.PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, p.getName());
            ps.setString(2, p.getpate());
            ps.setInt(3, p.getPrixBase());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean saveIngredients(Pizza p) {
        try {
            String query = "INSERT INTO pizzasContient (idPizza,idIngredient) VALUES (?, ?) ;";
            java.sql.PreparedStatement ps = con.prepareStatement(query);
            String query2 = "SELECT MAX(id) FROM pizzas;";
            java.sql.PreparedStatement ps2 = con.prepareStatement(query2);
            ResultSet rs = ps2.executeQuery();
            int idPizza = 0;
            if (rs.next()) {
                idPizza = rs.getInt("max");
            }
            for (Ingredient i : p.getIngredients()) {
                ps.setInt(1, idPizza);
                ps.setInt(2, i.getId());
                ps.executeUpdate();
            }
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
