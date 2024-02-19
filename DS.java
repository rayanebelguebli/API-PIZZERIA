package controleurs;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DS {
    private String properties;
    
    public DS(String properties) {
        this.properties = properties;
    }

    public Connection getConnection() throws Exception {
        Properties p = new Properties();
        p.load(new FileInputStream(System.getProperty("user.dir")+properties));
        Class.forName(p.getProperty("driver"));
        String url = p.getProperty("url");
        String nom = p.getProperty("login");
        String mdp = p.getProperty("password");
        Connection con = DriverManager.getConnection(url, nom, mdp);
        return con;
    
    }
}
