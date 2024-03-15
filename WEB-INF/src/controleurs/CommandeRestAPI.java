package controleurs;

import java.io.*;
import java.sql.Connection;
import java.time.LocalDate;

import java.time.ZoneId;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;

import dao.AuthentDAO;
import dao.CommandeDAODatabase;

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
        try (Connection con = ds.getConnection();) {
            CommandeDAODatabase dao = new CommandeDAODatabase(con);
            if (info == null || info.equals("/")) {
                Collection<Commande> l = dao.findAll();
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
            Commande c = dao.findById(id);
            if (c == null) {
                res.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            if (splits.length == 2) {
                if (c.getName() != null) {
                    out.print(objectMapper.writeValueAsString(c));
                    return;
                } else {
                    res.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    out.print("commande inexistant");
                    return;
                }
            }
            if (splits.length == 3) {
                if (splits[2].equals("prixfinal")) {
                    if (dao.findById(c.getId()).getName() != null) {
                        int prixFinal = 0;
                        for (Pizza p : c.getList()) {
                            prixFinal = prixFinal + p.getPrixBase();
                            for (Ingredient i : p.getIngredients()) {
                                prixFinal = prixFinal + i.getPrix();
                            }
                        }

                        out.print(objectMapper.writeValueAsString(c.getName() + " : " + prixFinal + " euros"));
                        return;
                    } else {
                        res.sendError(HttpServletResponse.SC_NOT_FOUND);
                        out.print("commande inexistante");
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
            CommandeDAODatabase dao = new CommandeDAODatabase(con);
            String authorization = req.getHeader("Authorization");
            if (authorization == null || !authorization.startsWith("Basic")) {
                out.print("pas autorisé");
                return;
            }

            String token = authorization.substring("Basic".length()).trim();
            byte[] base64 = Base64.getDecoder().decode(token);
            String[] lm = (new String(base64)).split(":");
            String login = lm[0];
            String pwd = lm[1];
            AuthentDAO daoAuthent = new AuthentDAO(con);
            if (daoAuthent.verifToken(login, pwd)) {

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
                    while (exist && idx < c.getList().size()) {
                        if (!dao.PizzaExist(c.getList().get(idx))) {
                            exist = false;
                        } else {
                            idx = idx + 1;
                        }
                    }
                    if (exist && dao.findById(c.getId()).getName() == null && !dao.findByName(c.getName())) {
                        if (dao.save(c) == true && dao.savePizzas(c) == true) {
                            out.print(objectMapper.writeValueAsString(dao.findById(c.getId())));
                            return;
                        }
                    } else {
                        res.sendError(HttpServletResponse.SC_CONFLICT);
                        out.print("pizzas(s) inexistant ou commande déjà existante");
                        return;
                    }
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
        String info = req.getPathInfo();
        DS ds = new DS("/config.postgres.prop");
        try (Connection con = ds.getConnection();) {
            CommandeDAODatabase dao = new CommandeDAODatabase(con);
            String authorization = req.getHeader("Authorization");
            if (authorization == null || !authorization.startsWith("Basic")) {
                out.print("pas autorisé");
                return;
            }

            String token = authorization.substring("Basic".length()).trim();
            byte[] base64 = Base64.getDecoder().decode(token);
            String[] lm = (new String(base64)).split(":");
            String login = lm[0];
            String pwd = lm[1];
            AuthentDAO daoAuthent = new AuthentDAO(con);
            if (daoAuthent.verifToken(login, pwd)) {
                String[] splits = info.split("/");
                if (splits.length > 3) {
                    res.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
                int id = Integer.parseInt(splits[1]);
                Commande c = dao.findById(id);
                if (c == null) {
                    res.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
                if (splits.length == 3) {
                    int idPizza = Integer.parseInt(splits[2]);
                    boolean delete = false;
                    int idx = 0;
                    while (!delete && idx < c.getList().size()) {
                        if (c.getList().get(idx).getId() == idPizza) {
                            dao.deletePizza(idPizza);
                            out.print("pizza supprimer : ");
                            out.print(c.getList().get(idx).getName());
                            delete = true;
                            return;
                        } else {
                            idx = idx + 1;
                        }
                    }
                    if (!delete) {
                        res.sendError(HttpServletResponse.SC_NOT_FOUND);
                        out.print("pizza non associé à cette commande ou commande inexistante");
                        return;
                    }
                }
                if (splits.length == 2) {
                    if (dao.findById(id).getName() != null) {
                        dao.delete(id);
                        out.print("commande supprimer : ");
                        out.print(c.getName());
                        return;
                    } else {
                        res.sendError(HttpServletResponse.SC_NOT_FOUND);
                        out.print("commande inexistant");
                        return;
                    }
                }
            }
        } catch (Exception e) {
            out.print(e.getMessage());
        }

    }

}
