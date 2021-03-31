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

/**
 * Controller for all orders page
 * Available only for admin
 */
@WebServlet(name = "AllOrdersServlet", value = Path.ALL_ORDERS_PATH)
public class AllOrdersServlet extends HttpServlet {

    private static final String ORDERS_VIEW_PATH = "/WEB-INF/jsp/all_orders.jsp";

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        List<Order> orders = OrderDAO.getAllOrders();
        request.setAttribute("orders", orders);
        request.getRequestDispatcher(ORDERS_VIEW_PATH).forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doGet(request, response);
    }

}