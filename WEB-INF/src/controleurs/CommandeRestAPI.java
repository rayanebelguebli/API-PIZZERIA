package controleurs;

import java.io.*;
import java.sql.Connection;
import java.util.Collection;

import com.fasterxml.jackson.databind.ObjectMapper;

import dao.CommandeDAODatabase;
import dao.PizzaDAODatabase;
import ds.DS;
import dto.Commande;
import dto.Pizza;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/commandes/*")
public class CommandeRestAPI extends HttpServlet{
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
    }
}
