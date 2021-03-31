package com.gmail.hryhoriev75.onlineshop.web.controller;

import com.gmail.hryhoriev75.onlineshop.Constants;
import com.gmail.hryhoriev75.onlineshop.model.UserDAO;
import com.gmail.hryhoriev75.onlineshop.model.entity.User;
import com.gmail.hryhoriev75.onlineshop.security.Security;
import com.gmail.hryhoriev75.onlineshop.web.Path;
import com.gmail.hryhoriev75.onlineshop.web.utils.RequestUtils;

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

/**
 * Login page controller
 * Retrieves credentials from request and validate them.
 * If error pass them back to JSP view. Otherwise creating session for user and redirecting him to main page
 */
@WebServlet(name = "LoginServlet", value = LOGIN_PATH)
public class LoginServlet extends HttpServlet {

    private static final String LOGIN_VIEW_PATH = "/WEB-INF/jsp/login.jsp";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (RequestUtils.getSessionAttribute(request, "user", User.class) != null) {
            // if we somehow opened /login page while being already logged in, we just do redirect to catalog (/)
            response.sendRedirect(Path.CATALOG_PATH);
        } else {
            request.getRequestDispatcher(LOGIN_VIEW_PATH).forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (RequestUtils.getSessionAttribute(request, "user", User.class) != null) {
            // if we somehow opened /login page while being already logged in, we just do redirect to catalog (/)
            response.sendRedirect(Path.CATALOG_PATH);
            return;
        }

        // continue to perform login
        // retrieving parameters from login form
        String email = request.getParameter("email").toLowerCase().trim();
        String password = request.getParameter("password").toLowerCase().trim();
        String remember = request.getParameter("remember"); // "checked" or ""

        // filling map with parameters which will be passed to the view in case of error
        Map<String, String> viewAttributes = new HashMap<>();
        viewAttributes.put("email", email);
        viewAttributes.put("remember", remember);

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

        // all request parameters are valid
        // trying to identify and authenticate user
        User user = UserDAO.findUserByEmail(email);
        if(user == null) {
            viewAttributes.put("error", Constants.USER_NOT_FOUND);
            passErrorToView(request, response, viewAttributes);
            return;
        }
        try {
            if(!Security.isPasswordCorrect(password, user.getPassword())) {
                viewAttributes.put("error", Constants.WRONG_PASSWORD);
                passErrorToView(request, response, viewAttributes);
                return;
            }
        } catch (Exception e) {
            viewAttributes.put("error", Constants.WRONG_PASSWORD);
            passErrorToView(request, response, viewAttributes);
            return;
        }

        // if we are here then login information was correct and user was identified
        // lets put him into session and redirect to catalog (/)
        HttpSession session = request.getSession(true);
        session.setAttribute("user", user);
        if("".equals(remember))
            session.setMaxInactiveInterval(1800); // 30 minutes
        else
            session.setMaxInactiveInterval(604800); // 7 days
        response.sendRedirect(Path.CATALOG_PATH);
    }

    private void passErrorToView(HttpServletRequest request, HttpServletResponse response, Map<String, String> viewAttributes) throws ServletException, IOException {
        for(Map.Entry<String, String> entry : viewAttributes.entrySet())
            request.setAttribute(entry.getKey(), entry.getValue());
        request.getRequestDispatcher(LOGIN_VIEW_PATH).forward(request, response);
    }

}