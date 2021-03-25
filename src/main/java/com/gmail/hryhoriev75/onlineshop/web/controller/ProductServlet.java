package com.gmail.hryhoriev75.onlineshop.web.controller;

import com.gmail.hryhoriev75.onlineshop.model.ProductDAO;
import com.gmail.hryhoriev75.onlineshop.model.entity.Product;
import com.gmail.hryhoriev75.onlineshop.web.Path;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ProductServlet", value = Path.PRODUCT_PATH)
public class ProductServlet extends HttpServlet {

    private static final String PRODUCT_VIEW_PATH = "/WEB-INF/jsp/product.jsp";

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Product product = null;
        try {
            long id = Long.parseLong(request.getParameter("id"));
            product = ProductDAO.findProductById(id);
        } catch(NumberFormatException nfe) {
            nfe.printStackTrace();
        }
        if(product != null) {
            request.setAttribute("product", product);
            request.getRequestDispatcher(PRODUCT_VIEW_PATH).forward(request, response);
        } else {
            request.getRequestDispatcher(Path.NOT_FOUND_PATH).forward(request, response);
        }

    }

}