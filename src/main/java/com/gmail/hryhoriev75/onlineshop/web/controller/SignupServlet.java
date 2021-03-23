package com.gmail.hryhoriev75.onlineshop.web.controller;

import com.gmail.hryhoriev75.onlineshop.model.UserDAO;
import com.gmail.hryhoriev75.onlineshop.model.entity.User;
import com.gmail.hryhoriev75.onlineshop.security.Security;

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

@WebServlet(name = "SignupServlet", value = "/signup")
public class SignupServlet extends HttpServlet {

    private static final String LOGIN_VIEW_PATH = "/WEB-INF/jsp/signup.jsp";
    private static final String CATALOG_PATH = "/";
    private static final String LOGIN_PATH = "/login";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            // if we somehow opened /signup page while being already logged in, we just do redirect to catalog (/)
            response.sendRedirect(CATALOG_PATH);
        } else {
            RequestDispatcher disp = request.getRequestDispatcher(LOGIN_VIEW_PATH);
            disp.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            // if we somehow opened /signup page while being already logged in, we just do redirect to catalog (/)
            response.sendRedirect(CATALOG_PATH);
            return;
        }

        // continue to perform signup
        // retrieving parameters from login form
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String name = request.getParameter("password");
        String phoneNumber = request.getParameter("password");


        // filling map with parameters which will be passed to the view
        Map<String, String> viewAttributes = new HashMap<>();
        viewAttributes.put("email", email);
        viewAttributes.put("name", name);
        viewAttributes.put("phoneNumber", phoneNumber);

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
        if(name == null || name.length() < 1) {
            viewAttributes.put("error", "Name isn't valid");
            passErrorToView(request, response, viewAttributes);
            return;
        }
        if(phoneNumber != null && !Security.isPhoneValid(phoneNumber)) {
            viewAttributes.put("error", "Phone number isn't valid");
            passErrorToView(request, response, viewAttributes);
            return;
        }
        User user = UserDAO.findUserByEmail(email);
        if(user != null) {
            viewAttributes.put("error", "User with this email already exists");
            passErrorToView(request, response, viewAttributes);
            return;
        }

        // if we are here then signup information was correct
        // lets create user order in DB and let him login
        // TODO create user order in DB
        response.sendRedirect(LOGIN_PATH);
    }

    private void redirect(HttpServletResponse response, String path) throws IOException {
        response.sendRedirect(path);
    }

    private void passErrorToView(HttpServletRequest request, HttpServletResponse response, Map<String, String> viewAttributes) throws ServletException, IOException {
        for(Map.Entry<String, String> entry : viewAttributes.entrySet())
            request.setAttribute(entry.getKey(), entry.getValue());
        RequestDispatcher disp = request.getRequestDispatcher(LOGIN_VIEW_PATH);
        disp.forward(request, response);
    }

}