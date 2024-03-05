package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;

import dto.Commande;
import dto.Ingredient;
import dto.Pizza;

public class CommandeDAODatabase {

    Connection con;

    public CommandeDAODatabase(Connection con) {
        this.con = con;
    }

    public Commande findById(int id) {

        PizzaDAODatabase pizzaDAO = new PizzaDAODatabase(con);

        Commande c = new Commande();
        try {
            String query = "Select * from commandes where id=?;";
            java.sql.PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                c.setDate((rs.getDate("date")));
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            String query = "Select idPizza from commandesContient where idCommande=?;";
            java.sql.PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            ArrayList<Pizza> list = new ArrayList<>();
            while (rs.next()) {
                list.add(pizzaDAO.findById(rs.getInt("idPizza")));
            }

            c.setList(list);
            return c;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public ArrayList<Commande> findAll() {

        PizzaDAODatabase pizzaDAO = new PizzaDAODatabase(con);
        ArrayList<Commande> commandes = new ArrayList<>();
        try {
            String query = "Select * from commandes;";
            java.sql.PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Commande c = new Commande();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                c.setDate(rs.getDate("date"));
                try {
                    String query2 = "Select idPizza from commandesContient where idCommande=?;";
                    java.sql.PreparedStatement ps2 = con.prepareStatement(query2);
                    ps2.setInt(1, rs.getInt("id"));
                    ResultSet rs2 = ps2.executeQuery();
                    ArrayList<Pizza> list = new ArrayList<>();
                    while (rs2.next()) {
                        list.add(pizzaDAO.findById(rs2.getInt("idPizza")));
                    }
                    c.setList(list);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
                commandes.add(c);
            }
            return commandes;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public boolean save(Commande p) {
        try {
            String query = "INSERT INTO commandes (id, name,date) VALUES (?, ?, ?) ;";
            java.sql.PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, p.getId());
            ps.setString(2, p.getName());
            java.util.Date utilDate = p.getDate();
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            ps.setDate(3, sqlDate);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean savePizzas(Commande p) {
        try {
            String query = "INSERT INTO commandesContient (idCommande,idPizza) VALUES (?, ?) ;";
            java.sql.PreparedStatement ps = con.prepareStatement(query);
            for (Pizza i : p.getList()) {
                ps.setInt(1, p.getId());
                ps.setInt(2, i.getId());
                ps.executeUpdate();
            }
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean ingredientExistInPizzaContient(Pizza p, Ingredient i) {
        try {
            String query = "Select * from pizzasContient where idPizza= ? and idIngredient = ? ;";
            java.sql.PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, p.getId());
            ps.setInt(2, i.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
            return false;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
