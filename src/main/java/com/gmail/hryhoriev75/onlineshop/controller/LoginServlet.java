package com.gmail.hryhoriev75.onlineshop.controller;

import com.gmail.hryhoriev75.onlineshop.model.UserDAO;
import com.gmail.hryhoriev75.onlineshop.model.entity.User;
import com.gmail.hryhoriev75.onlineshop.security.Security;
import org.apache.commons.validator.Validator;
import org.apache.commons.validator.routines.EmailValidator;

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

@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet {

    private static final String LOGIN_VIEW_PATH = "/WEB-INF/jsp/login.jsp";
    private static final String CATALOG_PATH = "/";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("email") != null) {
            response.sendRedirect(CATALOG_PATH);
        } else {
            RequestDispatcher disp = request.getRequestDispatcher(LOGIN_VIEW_PATH);
            disp.forward(request, response);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("email") != null) {
            response.sendRedirect(CATALOG_PATH);
            return;
        }

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String remember = "on".equals(request.getParameter("remember")) ? "checked" : "";

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

        session = request.getSession(true);
        session.setAttribute("email", user.getEmail());
        session.setAttribute("rolename", user.getRoleName());
        session.setAttribute("user", user);
        if("".equals(remember))
            session.setMaxInactiveInterval(1800); // 30 minutes
        else
            session.setMaxInactiveInterval(604800); // 7 days
        response.sendRedirect(CATALOG_PATH);
    }

    private void passErrorToView(HttpServletRequest request, HttpServletResponse response, Map<String, String> viewAttributes) throws ServletException, IOException {
        for(Map.Entry<String, String> entry : viewAttributes.entrySet())
            request.setAttribute(entry.getKey(), entry.getValue());
        RequestDispatcher disp = request.getRequestDispatcher(LOGIN_VIEW_PATH);
        disp.forward(request, response);
    }

}