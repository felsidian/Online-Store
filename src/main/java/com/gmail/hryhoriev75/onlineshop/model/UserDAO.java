package com.gmail.hryhoriev75.onlineshop.model;

import com.gmail.hryhoriev75.onlineshop.db.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    private static final String SQL_FIND_USER_BY_ID =
            "SELECT * FROM users WHERE login=?";

    public User findUserById(long id) {
        User user = null;
        try (Connection con = DBHelper.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement("SELECT name, email FROM user");
             ResultSet rs = pst.executeQuery();) {

            while (rs.next()) {
                rs.getString("name");
                rs.getString("email");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return user;
    }


}
