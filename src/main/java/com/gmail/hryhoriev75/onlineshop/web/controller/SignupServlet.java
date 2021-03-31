package com.gmail.hryhoriev75.onlineshop.web.controller;

import com.gmail.hryhoriev75.onlineshop.Constants;
import com.gmail.hryhoriev75.onlineshop.model.UserDAO;
import com.gmail.hryhoriev75.onlineshop.model.entity.User;
import com.gmail.hryhoriev75.onlineshop.security.Security;
import com.gmail.hryhoriev75.onlineshop.web.Path;
import com.gmail.hryhoriev75.onlineshop.web.utils.RequestUtils;

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

/**
 * Signup page controller
 * Retrieves credentials from request and validate them.
 * If error pass them back to JSP view. Otherwise adding user to Db, creating session and redirecting him to main page
 */
@WebServlet(name = "SignupServlet", value = Path.SIGNUP_PATH)
public class SignupServlet extends HttpServlet {

    private static final String SIGNUP_VIEW_PATH = "/WEB-INF/jsp/signup.jsp";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (RequestUtils.getSessionAttribute(request, "user", User.class) != null) {
            // if we somehow opened /signup page while being already logged in, we just do redirect to catalog (/)
            response.sendRedirect(Path.CATALOG_PATH);
        } else {
            RequestDispatcher disp = request.getRequestDispatcher(SIGNUP_VIEW_PATH);
            disp.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (RequestUtils.getSessionAttribute(request, "user", User.class) != null) {
            // if we somehow opened /signup page while being already logged in, we just do redirect to catalog (/)
            response.sendRedirect(Path.CATALOG_PATH);
            return;
        }

        // continue to perform signup
        // retrieving parameters from signup form
        String email = request.getParameter("email").toLowerCase().trim();
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String phoneNumber = request.getParameter("phoneNumber");

        // filling map with parameters which will be passed to the view
        Map<String, String> viewAttributes = new HashMap<>();
        viewAttributes.put("email", email);
        viewAttributes.put("name", name);
        viewAttributes.put("phoneNumber", phoneNumber);

        if(!Security.isEmailValid(email)) {
            viewAttributes.put("error", Constants.EMAIL_NOT_VALID);
            passErrorToView(request, response, viewAttributes);
            return;
        }
        if(!Security.isPasswordValid(password)) {
            viewAttributes.put("error", Constants.PASSWORD_NOT_VALID);
            passErrorToView(request, response, viewAttributes);
            return;
        }
        if(name == null || name.isBlank()) {
            viewAttributes.put("error", Constants.NAME_NOT_VALID);
            passErrorToView(request, response, viewAttributes);
            return;
        }
        if(phoneNumber != null && !Security.isPhoneValid(phoneNumber)) {
            viewAttributes.put("error", Constants.PHONE_NOT_VALID);
            passErrorToView(request, response, viewAttributes);
            return;
        }
        // all request parameters are valid

        if(UserDAO.findUserByEmail(email) != null) {
            viewAttributes.put("error", Constants.EMAIL_EXISTS);
            passErrorToView(request, response, viewAttributes);
            return;
        }

        // lets create user in DB and make him logged in
        boolean userAdded = false;
        try {
            userAdded = UserDAO.addUser(email, Security.hashPassword(password), name, phoneNumber, "uk");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!userAdded) {
            viewAttributes.put("error", Constants.OTHER_ERROR);
            passErrorToView(request, response, viewAttributes);
        } else {
            User user = UserDAO.findUserByEmail(email);
            // lets put him into session and redirect to catalog (/)
            HttpSession session = request.getSession(true);
            session.setAttribute("user", user);
            session.setMaxInactiveInterval(86400); // 1 day
            response.sendRedirect(Path.CATALOG_PATH);
        }
    }

    private void passErrorToView(HttpServletRequest request, HttpServletResponse response, Map<String, String> viewAttributes) throws ServletException, IOException {
        for(Map.Entry<String, String> entry : viewAttributes.entrySet())
            request.setAttribute(entry.getKey(), entry.getValue());
        request.getRequestDispatcher(SIGNUP_VIEW_PATH).forward(request, response);
    }

}