package dao;

import java.sql.Connection;
import java.sql.ResultSet;
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
                i.setName(rs.getString("name").toLowerCase());
                i.setPrix(rs.getInt("prix"));
            }

            return i;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
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
                i.setName(rs.getString("name").toLowerCase());
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
            String query = "INSERT INTO ingredients (name,prix) VALUES (?, ?)";
            java.sql.PreparedStatement ps = con.prepareStatement(query);
            ArrayList<Ingredient> test = this.findAll();
            int idx = 0;
            while (idx < test.size()) {
                if (test.get(idx).getName().toLowerCase().equals(i.getName().toLowerCase())) {
                    return false;
                }
                idx = idx + 1;
            }
            ps.setString(1, i.getName().toLowerCase());
            ps.setInt(2, i.getPrix());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean delete(int id) {
        try {
            String query = "DELETE FROM ingredients WHERE id=?";
            java.sql.PreparedStatement ps = con.prepareStatement(query);
            if (this.findById(id).getName() != null) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public Ingredient findByName(String name) {
        try {
            String query = "SELECT * FROM ingredients WHERE name = ?";
            java.sql.PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            Ingredient i = new Ingredient();
            if (rs.next()) {
                i.setId(rs.getInt("id"));
                i.setName(rs.getString("name").toLowerCase());
                i.setPrix(rs.getInt("prix"));
            }
            return i;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public boolean modifIngredient(int prix, int id) {
        try {
            String query = "UPDATE ingredients set prix= ? where id = ?;";
            java.sql.PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, prix);
            ps.setInt(2, id);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

}
