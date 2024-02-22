package controleurs;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public abstract class DoPatch extends HttpServlet {
    public void service(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, java.io.IOException {
        if (req.getMethod().equalsIgnoreCase("PATCH")) {
            doPatch(req, res);
        } else {
            super.service(req, res);
        }
    }

    public abstract void doPatch(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException, java.io.IOException;
}