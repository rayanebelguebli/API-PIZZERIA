package controleurs;

import java.io.*;
import java.sql.Connection;
import java.util.Collection;

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
public class PizzaRestAPI extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        String info = req.getPathInfo();
        DS ds = new DS("/config.postgres.prop");
        Connection con = null;
        try {
            con = ds.getConnection();
        } catch (Exception e) {
            out.print(e.getMessage());

        }
        PizzaDAODatabase dao = new PizzaDAODatabase(con);
        if (info == null || info.equals("/")) {
            Collection<Pizza> l = dao.findAll();
            String jsonstring = objectMapper.writeValueAsString(l);
            out.print(jsonstring);
            return;
        }
        String[] splits = info.split("/");
        if (splits.length != 2) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        int id = Integer.parseInt(splits[1]);
        Pizza e = dao.findById(id);
        if (e == null) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        out.print(objectMapper.writeValueAsString(e));
        return;
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        String info = req.getPathInfo();
        DS ds = new DS("/config.postgres.prop");
        Connection con = null;
        try {
            con = ds.getConnection();
        } catch (Exception e) {
            out.print(e.getMessage());
        }
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
                if(!dao.ingredientExist(p.getIngredients().get(idx))){
                    exist = false;
                }
                else{
                    idx = idx + 1;
                }
            }
            if(exist){
                if(dao.save(p) == true && dao.saveIngredients(p) == true){
                    out.print(objectMapper.writeValueAsString(p));
                }
            }
            else{
                out.print("ingredient(s) enexistant");
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
                    dao.deleteIngredient(ingredientActuelPizza.getId());
                }
                dao.saveIngredients(p);
                out.print(objectMapper.writeValueAsString(p));
            }
            else{
                out.print("pizza ou ingredient enexistant");
            }
        }
    }

    public void doDelete(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        String info = req.getPathInfo();
        DS ds = new DS("/config.postgres.prop");
        Connection con = null;
        try {
            con = ds.getConnection();
        } catch (Exception e) {
            out.print(e.getMessage());
        }
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
        if(splits.length  == 3){
            int idIngredient = Integer.parseInt(splits[2]);
            for(Ingredient i : p.getIngredients()){
                if(i.getId() == idIngredient){
                    dao.deleteIngredient(i.getId());
                }
            }
            out.print(objectMapper.writeValueAsString(p));
        }
        if(splits.length == 2){
            dao.delete(id); 
            out.print(objectMapper.writeValueAsString(p));
        }
    }
}
