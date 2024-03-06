package controleurs;

import java.io.*;
import java.sql.Connection;
import java.util.Collection;

import com.fasterxml.jackson.databind.ObjectMapper;

import dao.IngredientDAODatabase;
import ds.DS;
import dto.Ingredient;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/ingredients/*")
public class IngredientRestAPI extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        String info = req.getPathInfo();
        DS ds = new DS("/config.postgres.prop");
        try (Connection con = ds.getConnection()) {
            IngredientDAODatabase dao = new IngredientDAODatabase(con);
            if (info == null || info.equals("/")) {
                Collection<Ingredient> l = dao.findAll();
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
            Ingredient e = dao.findById(id);
            if (e == null) {
                res.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            if(splits.length  == 2){
                    if(e.getName() != null){
                        out.print(objectMapper.writeValueAsString(e.getName()));
                        return;
                    }
                    else{
                        out.print("ingredient inexistant");
                        return;
                    }

            }
            if(splits.length  == 3){
                if(splits[2].equals("name")){
                    if(e.getName() != null){
                        out.print(objectMapper.writeValueAsString(e.getName()));
                        return;
                    }
                    else{
                        out.print("ingredient inexistant");
                        return;
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
        try(Connection con = ds.getConnection();) {
            IngredientDAODatabase dao = new IngredientDAODatabase(con);

            if (info == null || info.equals("/")) {
                StringBuilder buffer = new StringBuilder();
                BufferedReader reader = req.getReader();
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                String payload = buffer.toString();
                Ingredient i = objectMapper.readValue(payload, Ingredient.class);
                boolean exist = false;
                int idx = 0;
                while(!exist && idx < dao.findAll().size()){
                    if(i.getName().equals(dao.findAll().get(idx).getName())){
                        exist = true;
                    }
                    else{
                        idx = idx + 1;
                    }
                }
                if(!exist){
                    dao.save(i);
                    out.print(objectMapper.writeValueAsString(dao.findByName(i.getName())));
                    return;
                }
                else{
                    out.print("ingredient déjà existant");
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
        try (Connection con = ds.getConnection()){
            IngredientDAODatabase dao = new IngredientDAODatabase(con);

            String[] splits = info.split("/");
            if (splits.length != 2) {
                res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
            int id = Integer.parseInt(splits[1]);
            Ingredient i = dao.findById(id);
            if (i == null) {
                res.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
            if(i.getName() != null){
                dao.delete(id);
                out.print("ingredient supprimer : "); 
                out.print(i.getName());
                return;
            }
            else{
                out.print("ingredient inexistant");
                return;
            }
        } catch (Exception e) {
            out.print(e.getMessage());
        }
        
    }
}
