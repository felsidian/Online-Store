package com.gmail.hryhoriev75.onlineshop.web.controller;

import com.gmail.hryhoriev75.onlineshop.model.OrderDAO;
import com.gmail.hryhoriev75.onlineshop.model.ProductDAO;
import com.gmail.hryhoriev75.onlineshop.model.entity.Category;
import com.gmail.hryhoriev75.onlineshop.model.entity.Order;
import com.gmail.hryhoriev75.onlineshop.model.entity.Product;
import com.gmail.hryhoriev75.onlineshop.model.entity.User;
import com.gmail.hryhoriev75.onlineshop.web.Path;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "OrderServlet", value = Path.ORDER_PATH + "/*")
public class OrderServlet extends HttpServlet {

    private static final String ORDER_VIEW_PATH = "/WEB-INF/jsp/order.jsp";

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        Order order = null;
        try {
            long id = Long.parseLong(request.getParameter("id"));
            order = OrderDAO.findOrderById(id);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            if (order != null && order.getUserId() == user.getId()){
                List<Order.Record> orderContent = OrderDAO.getOrderContent(order.getId());
                request.setAttribute("orderContent", orderContent);
                request.setAttribute("order", order);
                request.getRequestDispatcher(ORDER_VIEW_PATH).forward(request, response);
                return;
            }
        }
        request.getRequestDispatcher(Path.NOT_FOUND_PATH).forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        List<Order.Record> orderContent = parseParameterString(request.getParameter("cart"));
        HttpSession session = request.getSession(false);
        System.out.println("doPost req=" + request.getParameter("cart"));
        System.out.println("doPost orderContent size=" + orderContent.size());
        if(!orderContent.isEmpty() && session != null && session.getAttribute("user") != null) {
            // creating new Order
            User user = (User) session.getAttribute("user");
            long orderId = OrderDAO.createOrder(user.getId(), Instant.now(), orderContent);
            response.sendRedirect(Path.ORDER_PATH + "?id=" + orderId);
        } else {
            request.getRequestDispatcher(Path.NOT_FOUND_PATH).forward(request, response);
        }
    }

    // cart parameter contains '+' separated string of product ids each following by the quantity
    private List<Order.Record> parseParameterString(String cartParameter) {
        List<Order.Record> orderContent = new ArrayList<>();
        if(cartParameter != null && cartParameter.length() > 0) {
            String[] cartItems = cartParameter.split(";");
            for (int i = 0; i < cartItems.length; i += 2) {
                try {
                    Product product = new Product();
                    product.setId(Long.parseLong(cartItems[i]));
                    Order.Record record = new Order.Record();
                    record.setProduct(product);
                    record.setQuantity(Integer.parseInt(cartItems[i + 1]));
                    orderContent.add(record);
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }
            }
        }
        return orderContent;
    }

}