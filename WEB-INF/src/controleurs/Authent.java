package controleurs;

import java.io.*;
import java.sql.Connection;
import java.util.Base64;
import dao.AuthentDAO;

import ds.DS;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/users/token")
public class Authent extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("text/html;charset=UTF-8");
        PrintWriter out = res.getWriter();
        DS ds = new DS("/config.postgres.prop");
        try (Connection con = ds.getConnection();) {
            out.println("<body>\n" + //
                    "    <form action=\"\" method=\"post\">\n" + //
                    "        <label>login&nbsp;:</label>\n" + //
                    "        <input type=\"text\" name=\"login\" />\n" + //
                    "        <label>password&nbsp;:</label>\n" + //
                    "        <input type=\"text\" name=\"mdp\" />\n" + //
                    "        <input type=\"submit\" value=\"Valider\">;\n" + //
                    "    </form>\n" + //
                    "</body>\n");
        } catch (Exception e) {
            out.print(e.getMessage());

        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("text/html;charset=UTF-8");
        PrintWriter out = res.getWriter();
        DS ds = new DS("/config.postgres.prop");
        try (Connection con = ds.getConnection();) {
            String login = req.getParameter("login");
            String mdp = req.getParameter("mdp");
            AuthentDAO dao = new AuthentDAO(con);
            if (dao.verifToken(login, mdp)) {
                String str = login + ":" + mdp;
                String token = Base64.getEncoder().encodeToString(str.getBytes());
                out.println("Utilisateur connu : token : " + token);
            } else {
                out.println("<h1> Utilisateur inconnu </h1>");
            }
        } catch (Exception e) {
            out.print(e.getMessage());
        }
    }
}
