package com.gmail.hryhoriev75.onlineshop.web.controller.admin;

import com.gmail.hryhoriev75.onlineshop.model.OrderDAO;
import com.gmail.hryhoriev75.onlineshop.model.UserDAO;
import com.gmail.hryhoriev75.onlineshop.model.entity.Order;
import com.gmail.hryhoriev75.onlineshop.model.entity.User;
import com.gmail.hryhoriev75.onlineshop.web.Path;
import com.gmail.hryhoriev75.onlineshop.web.utils.RequestUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Controller for all users page
 * Available only for admin
 */
@WebServlet(name = "UsersServlet", value = Path.USERS_PATH)
public class UsersServlet extends HttpServlet {

    public static final String USERS_VIEW_PATH = "/WEB-INF/jsp/users.jsp";
    public static final String USERS_ATTRIBUTE = "users";

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        List<User> users = UserDAO.getAllUsers();
        request.setAttribute(USERS_ATTRIBUTE, users);
        request.getRequestDispatcher(USERS_VIEW_PATH).forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doGet(request, response);
    }

}