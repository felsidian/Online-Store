package com.gmail.hryhoriev75.onlineshop.web.controller.admin;

import com.gmail.hryhoriev75.onlineshop.model.OrderDAO;
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

@WebServlet(name = "UsersServlet", value = Path.USERS_PATH)
public class UsersServlet extends HttpServlet {

    private static final String ORDERS_VIEW_PATH = "/WEB-INF/jsp/all_users.jsp";

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        User user = RequestUtils.getSessionAttribute(request, "user", User.class);
        if (user != null) {
            List<Order> orders = OrderDAO.getOrdersByUserId(user.getId());
            request.setAttribute("orders", orders);
            request.getRequestDispatcher(ORDERS_VIEW_PATH).forward(request, response);
            return;
        }
        request.getRequestDispatcher(Path.NOT_FOUND_PATH).forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doGet(request, response);
    }

}