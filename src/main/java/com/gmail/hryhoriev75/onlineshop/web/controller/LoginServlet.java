package com.gmail.hryhoriev75.onlineshop.web.controller;

import com.gmail.hryhoriev75.onlineshop.model.UserDAO;
import com.gmail.hryhoriev75.onlineshop.model.entity.User;
import com.gmail.hryhoriev75.onlineshop.security.Security;
import com.gmail.hryhoriev75.onlineshop.web.Path;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.gmail.hryhoriev75.onlineshop.web.Path.LOGIN_PATH;

@WebServlet(name = "LoginServlet", value = LOGIN_PATH + "/*")
public class LoginServlet extends HttpServlet {

    private static final String LOGIN_VIEW_PATH = "/WEB-INF/jsp/login.jsp";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(request.getPathInfo() != null) {
            // if we somehow ended up with /login/*, redirection to /login
            response.sendRedirect(Path.LOGIN_PATH);
            return;
        }
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            // if we somehow opened /login page while being already logged in, we just do redirect to catalog (/)
            response.sendRedirect(Path.CATALOG_PATH);
        } else {
            RequestDispatcher disp = request.getRequestDispatcher(LOGIN_VIEW_PATH);
            disp.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            // if we somehow opened /login page while being already logged in, we just do redirect to catalog (/)
            response.sendRedirect(Path.CATALOG_PATH);
            return;
        }

        // continue to perform login
        // retrieving parameters from login form
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String remember = request.getParameter("remember"); // "checked" or ""

        // filling map with parameters which will be passed to the view
        Map<String, String> viewAttributes = new HashMap<>();
        viewAttributes.put("email", email);
        viewAttributes.put("remember", remember);

        if(!Security.isEmailValid(email)) {
            viewAttributes.put("error", "Email isn't valid");
            passErrorToView(request, response, viewAttributes);
            return;
        }
        if(!Security.isPasswordValid(password)) {
            viewAttributes.put("error", "Password isn't valid");
            passErrorToView(request, response, viewAttributes);
            return;
        }

        // all request parameters are valid
        // trying to identify and authenticate user
        User user = UserDAO.findUserByEmail(email);
        if(user == null) {
            viewAttributes.put("error", "User with this email wasn't found");
            passErrorToView(request, response, viewAttributes);
            return;
        }
        try {
            if(!Security.isPasswordCorrect(password, user.getPassword())) {
                viewAttributes.put("error", "Wrong password");
                passErrorToView(request, response, viewAttributes);
                return;
            }
        } catch (Exception e) {
            viewAttributes.put("error", "Wrong password");
            passErrorToView(request, response, viewAttributes);
            return;
        }

        // if we are here then login information was correct and user was identified
        // lets put him into session and redirect to catalog (/)
        session = request.getSession(true);
        session.setAttribute("user", user);
        //session.setAttribute("roleName", user.getRoleName());
        if("".equals(remember))
            session.setMaxInactiveInterval(1800); // 30 minutes
        else
            session.setMaxInactiveInterval(604800); // 7 days
        response.sendRedirect(Path.CATALOG_PATH);
    }

    private void passErrorToView(HttpServletRequest request, HttpServletResponse response, Map<String, String> viewAttributes) throws ServletException, IOException {
        for(Map.Entry<String, String> entry : viewAttributes.entrySet())
            request.setAttribute(entry.getKey(), entry.getValue());
        RequestDispatcher disp = request.getRequestDispatcher(LOGIN_VIEW_PATH);
        disp.forward(request, response);
    }

}