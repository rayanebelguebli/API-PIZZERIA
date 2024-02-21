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

    /*
     * public void doPost(HttpServletRequest req, HttpServletResponse res)
     * throws ServletException, IOException {
     * res.setContentType("application/json;charset=UTF-8");
     * PrintWriter out = res.getWriter();
     * ObjectMapper objectMapper = new ObjectMapper();
     * String info = req.getPathInfo();
     * DS ds = new DS("/config.postgres.prop");
     * Connection con = null;
     * try {
     * con = ds.getConnection();
     * } catch (Exception e) {
     * out.print(e.getMessage());
     * }
     * PizzaDAODatabase dao = new PizzaDAODatabase(con);
     * 
     * if (info == null || info.equals("/")) {
     * StringBuilder buffer = new StringBuilder();
     * BufferedReader reader = req.getReader();
     * String line;
     * while ((line = reader.readLine()) != null) {
     * buffer.append(line);
     * }
     * String payload = buffer.toString();
     * Ingredient i = objectMapper.readValue(payload, Ingredient.class);
     * out.print(dao.save(i));
     * }
     * }
     * 
     * public void doDelete(HttpServletRequest req, HttpServletResponse res)
     * throws ServletException, IOException {
     * res.setContentType("application/json;charset=UTF-8");
     * PrintWriter out = res.getWriter();
     * ObjectMapper objectMapper = new ObjectMapper();
     * String info = req.getPathInfo();
     * DS ds = new DS("/config.postgres.prop");
     * Connection con = null;
     * try {
     * con = ds.getConnection();
     * } catch (Exception e) {
     * out.print(e.getMessage());
     * }
     * IngredientDAODatabase dao = new IngredientDAODatabase(con);
     * 
     * String[] splits = info.split("/");
     * if (splits.length != 2) {
     * res.sendError(HttpServletResponse.SC_BAD_REQUEST);
     * return;
     * }
     * int id = Integer.parseInt(splits[1]);
     * Ingredient e = dao.findById(id);
     * if (e == null) {
     * res.sendError(HttpServletResponse.SC_NOT_FOUND);
     * return;
     * }
     * out.print(dao.delete(id));
     * out.print(objectMapper.writeValueAsString(e));
     * return;
     * }
     */
}
