package com.gmail.hryhoriev75.onlineshop.controller;

import com.gmail.hryhoriev75.onlineshop.db.DBHelper;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "CatalogServlet", value = "")
public class CatalogServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        // TODO
        // some business logic

        String viewPath = "/WEB-INF/jsp/catalog.jsp";
        RequestDispatcher disp = request.getRequestDispatcher(viewPath);
        disp.forward(request, response);

//        StringBuilder sb = new StringBuilder();
//        try (Connection con = DBHelper.getInstance().getConnection();
//             PreparedStatement pst = con.prepareStatement("SELECT name, email FROM user");
//             ResultSet rs = pst.executeQuery();) {
//            while (rs.next()) {
//                sb.append(rs.getString("name"));
//                sb.append(", ");
//                sb.append(rs.getString("email"));
//                sb.append(".   ");
//            }
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//
//
//        response.setContentType("text/html");
//
//        // Hello
//        PrintWriter out = response.getWriter();
//        out.println("<html><body>");
//        out.println("<h1>" + "Hello World from HelloServlet" + "</h1>");
//        out.println("<h1>" + sb.toString() + "</h1>");
//        out.println("</body></html>");
//
//
//        HttpSession session = request.getSession();
        //User u = User.builder();
    }

    public void destroy() {
    }
}