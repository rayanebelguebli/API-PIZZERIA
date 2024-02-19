package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import dto.Ingredient;

public class IngredientDAODatabase {

    Connection con;

    public IngredientDAODatabase(Connection con) {
        this.con = con;
    }

    public Ingredient findById(int id) {
        try {
            String query = "Select * from ingredients where id=?;";
            java.sql.PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            Ingredient i = new Ingredient();

            if (rs.next()) {
                i.setId(rs.getInt("id"));
                i.setName(rs.getString("name"));
                i.setPrix(rs.getInt("prix"));
            }

            return i;

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public ArrayList<Ingredient> findAll() {
        try {
            String query = "Select * from ingredients;";
            java.sql.PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            ArrayList<Ingredient> ingredients = new ArrayList<>();
            while (rs.next()) {
                Ingredient i = new Ingredient();
                i.setPrix(rs.getInt("prix"));
                i.setId(rs.getInt("id"));
                i.setName(rs.getString("name"));
                ingredients.add(i);
            }
            return ingredients;

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;

    }

    public boolean save(Ingredient i) {
        try {
            String query = "INSERT INTO ingredients (name,prix) VALUES ( ?, ?)";
            java.sql.PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, i.getName());
            ps.setInt(2, i.getPrix());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

}
