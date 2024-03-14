package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AuthentDAO {
    Connection con;

    public AuthentDAO(Connection con) {
        this.con = con;
    }

    public boolean verifToken(String login, String mdp) {

        try {
            String query = "Select * from users where login = ? and mdp = ? ;";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, login);
            ps.setString(2, mdp);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}
