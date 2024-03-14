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
            out.println("  <style>\n" + //
                    "        body {\n" + //
                    "            font-family: Arial, sans-serif;\n" + //
                    "            background-color: #f4f4f4;\n" + //
                    "            margin: 0;\n" + //
                    "            padding: 0;\n" + //
                    "            display: flex;\n" + //
                    "            align-items: center;\n" + //
                    "            justify-content: center;\n" + //
                    "            height: 100vh;\n" + //
                    "        }\n" + //
                    "\n" + //
                    "        form {\n" + //
                    "            background-color: #fff;\n" + //
                    "            padding: 20px;\n" + //
                    "            border-radius: 8px;\n" + //
                    "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" + //
                    "        }\n" + //
                    "\n" + //
                    "        label {\n" + //
                    "            display: block;\n" + //
                    "            margin-bottom: 8px;\n" + //
                    "        }\n" + //
                    "\n" + //
                    "        input {\n" + //
                    "            width: 100%;\n" + //
                    "            padding: 8px;\n" + //
                    "            margin-bottom: 16px;\n" + //
                    "            box-sizing: border-box;\n" + //
                    "        }\n" + //
                    "\n" + //
                    "        button {\n" + //
                    "            background-color: #4caf50;\n" + //
                    "            color: #fff;\n" + //
                    "            padding: 10px;\n" + //
                    "            border: none;\n" + //
                    "            border-radius: 4px;\n" + //
                    "            cursor: pointer;\n" + //
                    "        }\n" + //
                    "\n" + //
                    "        button:hover {\n" + //
                    "            background-color: #45a049;\n" + //
                    "        }\n" + //
                    "    </style>");
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
