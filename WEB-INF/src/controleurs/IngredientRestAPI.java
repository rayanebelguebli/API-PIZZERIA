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
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/ingredients/*")
public class IngredientRestAPI extends DoPatch {
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
            if (splits.length == 2) {
                if (e.getName() != null) {
                    out.print(objectMapper.writeValueAsString(e));
                    return;
                } else {
                    res.sendError(HttpServletResponse.SC_NOT_FOUND);
                    out.print("ingrédient inexistant");
                    return;
                }

            }
            if (splits.length == 3) {
                if (splits[2].equals("name")) {
                    if (e.getName() != null) {
                        out.print(objectMapper.writeValueAsString(e.getName()));
                        return;
                    } else {
                        res.sendError(HttpServletResponse.SC_NOT_FOUND);
                        out.print("ingrédient inexistant");
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
        try (Connection con = ds.getConnection();) {
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
                while (!exist && idx < dao.findAll().size()) {
                    if (i.getName().equals(dao.findAll().get(idx).getName())) {
                        exist = true;
                    } else {
                        idx = idx + 1;
                    }
                }
                if (!exist) {
                    dao.save(i);
                    out.print(objectMapper.writeValueAsString(dao.findByName(i.getName())));
                    return;
                } else {
                    res.sendError(HttpServletResponse.SC_CONFLICT);
                    out.print("ingrédient déjà existant");
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
        try (Connection con = ds.getConnection()) {
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
            if (i.getName() != null) {
                dao.delete(id);
                out.print("ingredient supprimer : ");
                out.print(i.getName());
                return;
            } else {
                res.sendError(HttpServletResponse.SC_NOT_FOUND);
                out.print("Ingrédient inexistant");
                return;
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
            IngredientDAODatabase dao = new IngredientDAODatabase(con);
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
            if (dao.findById(id).getName() != null) {
                System.out.println(dao.findById(id));
                String payload = buffer.toString();

                Map<String, String> jsonData = objectMapper.readValue(payload, Map.class);

                String prixString = jsonData.get("prix");

                int prix = Integer.parseInt(prixString);

                System.out.println(dao.modifIngredient(prix, id));

                out.print(objectMapper.writeValueAsString(dao.findById(id)));
            } else {
                res.sendError(HttpServletResponse.SC_NOT_FOUND);
                out.print("ingredient inexistant");
            }
        } catch (Exception e) {
            out.print(e.getMessage());
        }

    }
}
