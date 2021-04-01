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

    public static final String LOGIN_VIEW_PATH = "/WEB-INF/jsp/login.jsp";

    public static final String USER_ATTRIBUTE = "user";
    public static final String ERROR_ATTRIBUTE = "error";
    public static final String EMAIL_PARAMETER = "email";
    public static final String PASSWORD_PARAMETER = "password";
    public static final String REMEMBER_PARAMETER = "remember";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (RequestUtils.getSessionAttribute(request, USER_ATTRIBUTE, User.class) != null) {
            // if we somehow opened /login page while being already logged in, we just do redirect to catalog (/)
            response.sendRedirect(Path.CATALOG_PATH);
        } else {
            request.getRequestDispatcher(LOGIN_VIEW_PATH).forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (RequestUtils.getSessionAttribute(request, USER_ATTRIBUTE, User.class) != null) {
            // if we somehow opened /login page while being already logged in, we just do redirect to catalog (/)
            response.sendRedirect(Path.CATALOG_PATH);
            return;
        }

        // continue to perform login
        // retrieving parameters from login form
        String email = request.getParameter(EMAIL_PARAMETER).toLowerCase().trim();
        String password = request.getParameter(PASSWORD_PARAMETER).toLowerCase().trim();
        String remember = request.getParameter(REMEMBER_PARAMETER); // "checked" or ""

        // filling map with parameters which will be passed to the view in case of error
        Map<String, String> viewAttributes = new HashMap<>();
        viewAttributes.put(EMAIL_PARAMETER, email);
        viewAttributes.put(REMEMBER_PARAMETER, remember);

        if(!Security.isEmailValid(email)) {
            viewAttributes.put(ERROR_ATTRIBUTE, Constants.EMAIL_NOT_VALID);
            passErrorToView(request, response, viewAttributes);
            return;
        }
        if(!Security.isPasswordValid(password)) {
            viewAttributes.put(ERROR_ATTRIBUTE, Constants.PASSWORD_NOT_VALID);
            passErrorToView(request, response, viewAttributes);
            return;
        }

        // all request parameters are valid
        // trying to identify and authenticate user
        User user = UserDAO.findUserByEmail(email);
        if(user == null) {
            viewAttributes.put(ERROR_ATTRIBUTE, Constants.USER_NOT_FOUND);
            passErrorToView(request, response, viewAttributes);
            return;
        }
        try {
            if(!Security.isPasswordCorrect(password, user.getPassword())) {
                viewAttributes.put(ERROR_ATTRIBUTE, Constants.WRONG_PASSWORD);
                passErrorToView(request, response, viewAttributes);
                return;
            }
        } catch (Exception e) {
            viewAttributes.put(ERROR_ATTRIBUTE, Constants.WRONG_PASSWORD);
            passErrorToView(request, response, viewAttributes);
            return;
        }
        if(user.isBlocked()) {
            viewAttributes.put(ERROR_ATTRIBUTE, Constants.USER_BLOCKED);
            passErrorToView(request, response, viewAttributes);
            return;
        }

        // if we are here then login information was correct and user was identified
        // lets put him into session and redirect to catalog (/)
        HttpSession session = request.getSession(true);
        session.setAttribute(USER_ATTRIBUTE, user);
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