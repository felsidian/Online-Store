package com.gmail.hryhoriev75.onlineshop.web.controller;

import com.gmail.hryhoriev75.onlineshop.model.ProductDAO;
import com.gmail.hryhoriev75.onlineshop.model.entity.Category;
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

@WebServlet(name = "CategoryServlet", value = Path.CATEGORY_PATH)
public class CategoryServlet extends HttpServlet {

    private static final String CATEGORY_VIEW_PATH = "/WEB-INF/jsp/category.jsp";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Category category = null;
        try {
            long id = Long.parseLong(request.getParameter("id"));
            category = ProductDAO.findCategoryById(id);
        } catch(NumberFormatException nfe) {
            nfe.printStackTrace();
        }
        if(category != null) {
            List<Product> products = ProductDAO.getAllProductsByCategory(category);
            request.setAttribute("products", products);
            request.setAttribute("category", category);
            request.getRequestDispatcher(CATEGORY_VIEW_PATH).forward(request, response);
        } else {
            request.getRequestDispatcher(Path.NOT_FOUND_PATH).forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doGet(request, response);
    }

}