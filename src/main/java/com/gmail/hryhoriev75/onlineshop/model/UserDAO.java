package com.gmail.hryhoriev75.onlineshop.model;

import com.gmail.hryhoriev75.onlineshop.db.DBHelper;
import com.gmail.hryhoriev75.onlineshop.model.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    private static final String SQL_FIND_USER_BY_ID =
            "SELECT user.*,role.name AS role_name FROM user LEFT JOIN role ON role_id=role.id WHERE user.id=?";
    private static final String SQL_FIND_USER_BY_EMAIL =
            "SELECT user.*,role.name AS role_name FROM user LEFT JOIN role ON role_id=role.id WHERE user.email=?";
    private static final String SQL_ADD_USER =
            "INSERT INTO user(email,password,name,phone_number,locale,role_id,blocked)VALUES(?,?,?,?,?,(SELECT id FROM role WHERE name=? LIMIT 1),?)";

    private static final String FIELD_ID = "id";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_EMAIl = "email";
    private static final String FIELD_PASSWORD = "password";
    private static final String FIELD_PHONE_NUMBER = "phone_number";
    private static final String FIELD_BLOCKED = "blocked";
    private static final String FIELD_ROLE_NAME = "role_name";

    public static User findUserById(long id) {
        User user = null;
        try (Connection con = DBHelper.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_FIND_USER_BY_ID)) {
            pst.setLong(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                user = mapResultSet(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return user;
    }

    public static User findUserByEmail(String email) {
        User user = null;
        try (Connection con = DBHelper.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_FIND_USER_BY_EMAIL)) {
            pst.setString(1, email);
            try (ResultSet rs = pst.executeQuery()) {
                user = mapResultSet(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return user;
    }

    public static boolean addUser(String email, String password, String name, String phoneNumber, String locale) {
        boolean result = false;
        try (Connection con = DBHelper.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_ADD_USER)) {
            //email,password,name,phone_number,locale,role_id,blocked
            pst.setString(1, email);
            pst.setString(2, password);
            pst.setString(3, name);
            pst.setString(4, phoneNumber);
            pst.setString(5, locale);
            pst.setString(6, "user");
            pst.setBoolean(7, false);
            try{
                result = pst.executeUpdate() > 0;
            } finally {
                pst.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    private static User mapResultSet(ResultSet rs) {
        User user = null;
        try {
            if (rs.next()) {
                user = new User();
                user.setId(rs.getLong(FIELD_ID));
                user.setName(rs.getString(FIELD_NAME));
                user.setEmail(rs.getString(FIELD_EMAIl));
                user.setPassword(rs.getString(FIELD_PASSWORD));
                user.setPhoneNumber(rs.getString(FIELD_PHONE_NUMBER));
                user.setRoleName(rs.getString(FIELD_ROLE_NAME));
                user.setBlocked(rs.getBoolean(FIELD_BLOCKED));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

}
