package com.gmail.hryhoriev75.onlineshop.web.controller;

import com.gmail.hryhoriev75.onlineshop.model.OrderDAO;
import com.gmail.hryhoriev75.onlineshop.model.entity.Order;
import com.gmail.hryhoriev75.onlineshop.model.entity.Product;
import com.gmail.hryhoriev75.onlineshop.model.entity.User;
import com.gmail.hryhoriev75.onlineshop.web.Path;
import com.gmail.hryhoriev75.onlineshop.web.utils.RequestUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for user's orders page
 * Available only for logged in users
 * Otherwise redirects to error page
 */
@WebServlet(name = "OrdersServlet", value = Path.ORDERS_PATH)
public class OrdersServlet extends HttpServlet {

    public static final String ORDERS_VIEW_PATH = "/WEB-INF/jsp/orders.jsp";
    public static final String USER_ATTRIBUTE = "user";
    public static final String ORDERS_ATTRIBUTE = "orders";

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        User user = RequestUtils.getSessionAttribute(request, USER_ATTRIBUTE, User.class);
        if (user != null) {
            List<Order> orders = OrderDAO.getOrdersByUserId(user.getId());
            request.setAttribute(ORDERS_ATTRIBUTE, orders);
            request.getRequestDispatcher(ORDERS_VIEW_PATH).forward(request, response);
            return;
        }
        request.getRequestDispatcher(Path.NOT_FOUND_PATH).forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doGet(request, response);
    }

}