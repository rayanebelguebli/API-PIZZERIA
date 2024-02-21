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
                p.setNom(rs.getString("name"));
                p.setPâte(rs.getString("pate"));
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
                p.setNom(rs.getString("name"));
                p.setPâte(rs.getString("pate"));
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
            String query = "INSERT INTO pizzas VALUES (?, ?, ?, ?)";
            java.sql.PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, p.getId());
            ps.setString(2, p.getNom());
            ps.setString(3, p.getPâte());
            ps.setInt(4, p.getPrixBase());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
