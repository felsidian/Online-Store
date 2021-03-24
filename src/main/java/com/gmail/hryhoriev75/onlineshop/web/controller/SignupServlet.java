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

@WebServlet(name = "SignupServlet", value = Path.SIGNUP_PATH + "/*")
public class SignupServlet extends HttpServlet {

    private static final String SIGNUP_VIEW_PATH = "/WEB-INF/jsp/signup.jsp";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(request.getPathInfo() != null) {
            // if we somehow ended up with /signup/*, redirection to /signup
            response.sendRedirect(Path.SIGNUP_PATH);
            return;
        }
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            // if we somehow opened /signup page while being already logged in, we just do redirect to catalog (/)
            response.sendRedirect(Path.CATALOG_PATH);
        } else {
            RequestDispatcher disp = request.getRequestDispatcher(SIGNUP_VIEW_PATH);
            disp.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(request.getPathInfo() != null) {
            // if we somehow ended up with /signup/*, redirection to /signup
            response.sendRedirect(Path.SIGNUP_PATH);
            return;
        }
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            // if we somehow opened /signup page while being already logged in, we just do redirect to catalog (/)
            response.sendRedirect(Path.CATALOG_PATH);
            return;
        }

        // continue to perform signup
        // retrieving parameters from signup form
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String phoneNumber = request.getParameter("phoneNumber");


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
        if(name == null || name.isBlank()) {
            viewAttributes.put("error", "Name isn't valid");
            passErrorToView(request, response, viewAttributes);
            return;
        }
        if(phoneNumber != null && !Security.isPhoneValid(phoneNumber)) {
            viewAttributes.put("error", "Phone number isn't valid");
            passErrorToView(request, response, viewAttributes);
            return;
        }
        // all request parameters are valid

        if(UserDAO.findUserByEmail(email) != null) {
            viewAttributes.put("error", "User with this email already exists");
            passErrorToView(request, response, viewAttributes);
            return;
        }

        // lets create user in DB and make him logged in
        boolean userAdded = false;
        try {
            UserDAO.addUser(email, Security.hashPassword(password), name, phoneNumber, "uk");
            userAdded = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!userAdded) {
            viewAttributes.put("error", "Something went wrong. Please try again");
            passErrorToView(request, response, viewAttributes);
        } else {
            User user = UserDAO.findUserByEmail(email);
            // lets put him into session and redirect to catalog (/)
            session = request.getSession(true);
            session.setAttribute("user", user);
            session.setMaxInactiveInterval(86400); // 1 day
            response.sendRedirect(Path.CATALOG_PATH);
        }
    }

    private void redirect(HttpServletResponse response, String path) throws IOException {
        response.sendRedirect(path);
    }

    private void passErrorToView(HttpServletRequest request, HttpServletResponse response, Map<String, String> viewAttributes) throws ServletException, IOException {
        for(Map.Entry<String, String> entry : viewAttributes.entrySet())
            request.setAttribute(entry.getKey(), entry.getValue());
        RequestDispatcher disp = request.getRequestDispatcher(SIGNUP_VIEW_PATH);
        disp.forward(request, response);
    }

}