package com.gmail.hryhoriev75.onlineshop.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ProductServlet", value = "/product/*")
public class ProductServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        // TODO
        // some business logic

        String viewPath = "/WEB-INF/jsp/product.jsp";
        RequestDispatcher disp = request.getRequestDispatcher(viewPath);
        disp.forward(request, response);

    }

    public void destroy() {
    }
}