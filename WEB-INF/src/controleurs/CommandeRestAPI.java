package controleurs;

import java.io.*;
import java.sql.Connection;
import java.util.Collection;

import com.fasterxml.jackson.databind.ObjectMapper;

import dao.CommandeDAODatabase;
import dao.IngredientDAODatabase;
import dao.PizzaDAODatabase;
import ds.DS;
import dto.Commande;
import dto.Ingredient;
import dto.Pizza;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/commandes/*")
public class CommandeRestAPI extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        String info = req.getPathInfo();
        DS ds = new DS("/config.postgres.prop");
        try(Connection con = ds.getConnection();) {
            CommandeDAODatabase dao = new CommandeDAODatabase(con);
            if (info == null || info.equals("/")) {
                Collection<Commande> l = dao.findAll();
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
            Commande e = dao.findById(id);
            if (e == null) {
                res.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            out.print(objectMapper.writeValueAsString(e));
            return;
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
        try (Connection con = ds.getConnection();){
            CommandeDAODatabase dao = new CommandeDAODatabase(con);
            // PizzaDAODatabase daoPizza = new PizzaDAODatabase(con);

            if (info == null || info.equals("/")) {
                StringBuilder buffer = new StringBuilder();
                BufferedReader reader = req.getReader();
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                String payload = buffer.toString();
                Commande c = objectMapper.readValue(payload, Commande.class);
                boolean exist = true;
                int idx = 0;
                /*
                * while (exist && idx < c.getList().size()) {
                * for (int i = 0; i < c.getList().get(idx).getIngredients().size(); i++) {
                * if (!dao.ingredientExistInPizzaContient(c.getList().get(idx),
                * c.getList().get(idx).getIngredients().get(i))) {
                * exist = false;
                * } else {
                * idx = idx + 1;
                * }
                * }
                * 
                * }
                */
                // if (exist) {
                if (dao.save(c) == true && dao.savePizzas(c) == true) {
                    out.print(objectMapper.writeValueAsString(dao.findById(c.getId())));
                }

                System.out.println("lalala");
                // } else {
                // out.print("ingredient(s) enexistant");
                // }
            }
        } catch (Exception e) {
            out.print(e.getMessage());
        }

        
    }

}
