package controleurs;

import java.io.*;
import java.sql.Connection;
import java.util.Collection;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import dao.IngredientDAODatabase;
import dao.PizzaDAODatabase;
import ds.DS;
import dto.Ingredient;
import dto.Pizza;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/pizzas/*")
public class PizzaRestAPI extends DoPatch {

    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        String info = req.getPathInfo();
        DS ds = new DS("/config.postgres.prop");
        try (Connection con = ds.getConnection();){
            PizzaDAODatabase dao = new PizzaDAODatabase(con);
        if (info == null || info.equals("/")) {
            Collection<Pizza> l = dao.findAll();
            String jsonstring = objectMapper.writeValueAsString(l);
            out.print(jsonstring);
            return;
        }
        String[] splits = info.split("/");
        if (splits.length > 3) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        int id = Integer.parseInt(splits[1]);
        Pizza p = dao.findById(id);
        if (p == null) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        if(splits.length  == 2){
            if(p.getName() != null){
                out.print(objectMapper.writeValueAsString(p));
                return;
            }
            else{
                out.print("pizza inexistant");
                return;
            }

    }
        if(splits.length  == 3){
            if(splits[2].equals("prixfinal")){
                if(dao.findById(p.getId()).getName() != null){
                    int prixFinal = p.getPrixBase();
                for(Ingredient i : p.getIngredients()){
                    prixFinal = prixFinal + i.getPrix();
                }
                out.print(objectMapper.writeValueAsString(p.getName() + " : " + prixFinal + " euros"));
                return;
                }
                else{
                    out.print("pizza inexistante");
                }
            }
        }
        } catch (Exception e) {
            out.print(e.getMessage());
        }
        
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        String info = req.getPathInfo();
        DS ds = new DS("/config.postgres.prop");
        try (Connection con = ds.getConnection();) {
            PizzaDAODatabase dao = new PizzaDAODatabase(con);
            IngredientDAODatabase daoIngredient = new IngredientDAODatabase(con);
            if (info == null || info.equals("/")) {
                StringBuilder buffer = new StringBuilder();
                BufferedReader reader = req.getReader();
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                String payload = buffer.toString();
                Pizza p = objectMapper.readValue(payload, Pizza.class);
                boolean exist = true;
                int idx = 0;
                while (exist && idx < p.getIngredients().size()) {
                    if (!dao.ingredientExist(p.getIngredients().get(idx))) {
                        exist = false;
                    } else {
                        idx = idx + 1;
                    }
                }
                if (exist && dao.findById(p.getId()).getName() == null && !dao.findByName(p.getName())) {
                    if (dao.save(p) == true && dao.saveIngredients(p) == true) {
                        out.print(objectMapper.writeValueAsString(dao.findById(p.getId())));
                        return;
                    }
                } else {
                    out.print("ingredient(s) inexistant ou pizza déjà existante");
                    return;
                }
            }
            String[] splits = info.split("/");
            if(splits.length == 3){
                int idPizza = Integer.parseInt(splits[1]);
                Pizza p = dao.findById(idPizza);
                Ingredient i = daoIngredient.findById(Integer.parseInt(splits[2]));
                if(i.getName() != null && p.getName() != null){
                    p.getIngredients().add(daoIngredient.findById(Integer.parseInt(splits[2])));
                    for(Ingredient ingredientActuelPizza : p.getIngredients()){
                        System.out.println(dao.deleteIngredient(ingredientActuelPizza.getId()));
                    }
                    if(dao.saveIngredients(p)){
                        out.print(objectMapper.writeValueAsString(p));
                        return;
                    }
                    else{
                        out.print("ingredient déjà existant");
                        return;
                    }
                    
                }
                else{
                    out.print("pizza ou ingredient inexistant");
                    return;
                }
            }
        } catch (Exception e) {
            out.print(e.getMessage());
        }
        
    }

    public void doDelete(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        String info = req.getPathInfo();
        DS ds = new DS("/config.postgres.prop");
        try(Connection con = ds.getConnection();) {
            PizzaDAODatabase dao = new PizzaDAODatabase(con);
        String[] splits = info.split("/");
        if (splits.length > 3) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
        int id = Integer.parseInt(splits[1]);
        Pizza p = dao.findById(id);
        if (p == null) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
        if (splits.length == 3) {
            int idIngredient = Integer.parseInt(splits[2]);
            boolean delete = false;
            int idx = 0;
            while (!delete && idx < p.getIngredients().size()) {
                if (p.getIngredients().get(idx).getId() == idIngredient) {
                    dao.deleteIngredient(idIngredient);
                    out.print("ingredient supprimer : "); 
                    out.print(p.getIngredients().get(idx).getName());
                    delete = true;
                    return;
                }
                else{
                    idx = idx + 1;
                }
            }
            if(!delete){
                out.print("ingredient non associé à cette pizza ou pizza inexistante");
                return;
            }
        }
        if (splits.length == 2) {
            if(dao.findById(id).getName() != null){
                dao.delete(id);
                out.print("pizza supprimer : ");
                out.print(p.getName());
                return;
            }
            else{
                out.print("pizza inexistant");
                return;
            }
        }
        
        } catch (Exception e) {
            out.print(e.getMessage());
        }
        
    }

    @Override
    public void doPatch(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        String info = req.getPathInfo();
        DS ds = new DS("/config.postgres.prop");
        try (Connection con = ds.getConnection()) {
            PizzaDAODatabase dao = new PizzaDAODatabase(con);
        if (info == null || info.equals("/")) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String[] splits = info.split("/");
        if (splits.length != 2) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        int id = Integer.parseInt(splits[1]);

        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        if(dao.findById(id).getName() != null){
            System.out.println(dao.findById(id));
            String payload = buffer.toString();

            Map<String, String> jsonData = objectMapper.readValue(payload, Map.class);

            String prixString = jsonData.get("prix");

            int prix = Integer.parseInt(prixString);

            System.out.println(dao.modifPizza(prix, id));

            out.print(objectMapper.writeValueAsString(dao.findById(id)));
            return;
        }
        else{
            out.print("pizza inexistante");
            return;
        }
        } catch (Exception e) {
            out.print(e.getMessage());
        }

        
    }
}
