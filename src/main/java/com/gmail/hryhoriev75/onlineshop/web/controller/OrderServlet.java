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
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "OrderServlet", value = Path.ORDER_PATH)
public class OrderServlet extends HttpServlet {

    private static final String ORDER_VIEW_PATH = "/WEB-INF/jsp/order.jsp";

    // showing order content
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        long orderId = RequestUtils.getLongParameter(request, "id");
        if (orderId <= 0) {
            request.getRequestDispatcher(Path.NOT_FOUND_PATH).forward(request, response);
            return;
        }
        Order order = OrderDAO.findOrderById(orderId);
        if(order == null) {
            request.getRequestDispatcher(Path.NOT_FOUND_PATH).forward(request, response);
            return;
        }
        User user = RequestUtils.getSessionAttribute(request, "user", User.class);
        if (user != null && order.getUserId() == user.getId()) {
            List<Order.Record> orderContent = OrderDAO.getOrderContent(order.getId());
            request.setAttribute("orderContent", orderContent);
            request.setAttribute("order", order);
            request.getRequestDispatcher(ORDER_VIEW_PATH).forward(request, response);
            return;
        }
        request.getRequestDispatcher(Path.NOT_FOUND_PATH).forward(request, response);
    }

    // creating order
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        List<Order.Record> orderContent = parseCartParameterString(request.getParameter("cart"));
        User user = RequestUtils.getSessionAttribute(request, "user", User.class);
        if(!orderContent.isEmpty() && user != null) {
            long orderId = OrderDAO.createOrder(user.getId(), Instant.now(), orderContent);
            response.sendRedirect(Path.ORDER_PATH + "?id=" + orderId);
        } else {
            request.getRequestDispatcher(Path.NOT_FOUND_PATH).forward(request, response);
        }
    }

    // cart parameter contains ; separated string of product ids each following by the quantity
    private List<Order.Record> parseCartParameterString(String cartParameter) {
        List<Order.Record> orderContent = new ArrayList<>();
        if(cartParameter != null && cartParameter.length() > 0) {
            String[] cartItems = cartParameter.split(";");
            for (int i = 0; i < cartItems.length - 1; i += 2) {
                long id = 0;
                int quantity = 0;
                try {
                    id = Long.parseLong(cartItems[i]);
                    quantity = Integer.parseInt(cartItems[i + 1]);
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }
                if (id > 0 && quantity > 0) {
                    Product product = new Product();
                    product.setId(id);
                    Order.Record record = new Order.Record();
                    record.setProduct(product);
                    record.setQuantity(quantity);
                    orderContent.add(record);
                }
            }
        }
        return orderContent;
    }

}