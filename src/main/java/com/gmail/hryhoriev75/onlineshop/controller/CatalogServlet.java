package com.gmail.hryhoriev75.onlineshop.controller;

import com.gmail.hryhoriev75.onlineshop.model.entity.User;

import java.io.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet(name = "CatalogServlet", value = "")
public class CatalogServlet extends HttpServlet {

    private static final String CATALOG_VIEW_PATH = "/WEB-INF/jsp/catalog.jsp";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        HttpSession session = request.getSession(false);

        if(session != null) {
            User user = (User) session.getAttribute("user");
            request.setAttribute("user", user);
        }


        RequestDispatcher disp = request.getRequestDispatcher(CATALOG_VIEW_PATH);
        disp.forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doGet(request, response);
    }

}