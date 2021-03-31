package com.gmail.hryhoriev75.onlineshop.web.controller;

import com.gmail.hryhoriev75.onlineshop.web.Path;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Logout controller
 * Just invalidates session and redirect to main page
 */
@WebServlet(name = "LogoutServlet", value = Path.LOGOUT_PATH)
public class LogoutServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession(false);
        if (session != null)
            session.invalidate();
        response.sendRedirect(Path.CATALOG_PATH);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}