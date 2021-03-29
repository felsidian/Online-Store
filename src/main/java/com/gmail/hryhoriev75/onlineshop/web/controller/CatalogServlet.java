package com.gmail.hryhoriev75.onlineshop.web.controller;

import com.gmail.hryhoriev75.onlineshop.model.ProductDAO;
import com.gmail.hryhoriev75.onlineshop.model.entity.Category;
import com.gmail.hryhoriev75.onlineshop.model.entity.Product;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "CatalogServlet", value = "")
public class CatalogServlet extends HttpServlet {

    private static final String CATALOG_VIEW_PATH = "/WEB-INF/jsp/index.jsp";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        List<Category> categories = ProductDAO.getAllCategories();
        request.setAttribute("categories", categories);

        // get 6 products
        List<Product> products = ProductDAO.getProducts(6);
        request.setAttribute("products", products);

        RequestDispatcher disp = request.getRequestDispatcher(CATALOG_VIEW_PATH);
        disp.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doGet(request, response);
    }

}