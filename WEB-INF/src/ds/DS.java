package ds;

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
        System.out.println("1");
        Properties p = new Properties();
        System.out.println("2");
        System.out.println(System.getProperty("user.dir") + properties);
        p.load(new FileInputStream(System.getProperty("user.dir") + properties));
        System.out.println("3");
        Class.forName(p.getProperty("driver"));
        System.out.println("4");
        String url = p.getProperty("url");
        String nom = p.getProperty("login");
        String mdp = p.getProperty("password");
        System.out.println("5");
        Connection con = DriverManager.getConnection(url, nom, mdp);
        System.out.println("6");
        return con;

    }
}
