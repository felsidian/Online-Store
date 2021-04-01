package com.gmail.hryhoriev75.onlineshop.web.controller;

import com.gmail.hryhoriev75.onlineshop.Constants;
import com.gmail.hryhoriev75.onlineshop.model.UserDAO;
import com.gmail.hryhoriev75.onlineshop.model.entity.User;
import com.gmail.hryhoriev75.onlineshop.security.Security;
import com.gmail.hryhoriev75.onlineshop.web.Path;
import com.gmail.hryhoriev75.onlineshop.web.filter.LangFilter;
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

    public static final String SIGNUP_VIEW_PATH = "/WEB-INF/jsp/signup.jsp";

    public static final String USER_ATTRIBUTE = "user";
    public static final String ERROR_ATTRIBUTE = "error";
    public static final String NAME_ATTRIBUTE = "name";
    public static final String PHONE_NUMBER_ATTRIBUTE = "phoneNumber";
    public static final String EMAIL_PARAMETER = "email";
    public static final String PASSWORD_PARAMETER = "password";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (RequestUtils.getSessionAttribute(request, USER_ATTRIBUTE, User.class) != null) {
            // if we somehow opened /signup page while being already logged in, we just do redirect to catalog (/)
            response.sendRedirect(Path.CATALOG_PATH);
        } else {
            request.getRequestDispatcher(SIGNUP_VIEW_PATH).forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (RequestUtils.getSessionAttribute(request, USER_ATTRIBUTE, User.class) != null) {
            // if we somehow opened /signup page while being already logged in, we just do redirect to catalog (/)
            response.sendRedirect(Path.CATALOG_PATH);
            return;
        }

        // continue to perform signup
        // retrieving parameters from signup form
        String email = request.getParameter(EMAIL_PARAMETER).toLowerCase().trim();
        String password = request.getParameter(PASSWORD_PARAMETER);
        String name = request.getParameter(NAME_ATTRIBUTE);
        String phoneNumber = request.getParameter(PHONE_NUMBER_ATTRIBUTE);

        // filling map with parameters which will be passed to the view
        Map<String, String> viewAttributes = new HashMap<>();
        viewAttributes.put(EMAIL_PARAMETER, email);
        viewAttributes.put(NAME_ATTRIBUTE, name);
        viewAttributes.put(PHONE_NUMBER_ATTRIBUTE, phoneNumber);

        if(!Security.isPasswordValid(password)) {
            viewAttributes.put(ERROR_ATTRIBUTE, Constants.PASSWORD_NOT_VALID);
            passErrorToView(request, response, viewAttributes);
            return;
        }
        if(!Security.isEmailValid(email)) {
            viewAttributes.put(ERROR_ATTRIBUTE, Constants.EMAIL_NOT_VALID);
            passErrorToView(request, response, viewAttributes);
            return;
        }
        if(name == null || name.isBlank()) {
            viewAttributes.put(ERROR_ATTRIBUTE, Constants.NAME_NOT_VALID);
            passErrorToView(request, response, viewAttributes);
            return;
        }
        if(phoneNumber != null && !Security.isPhoneValid(phoneNumber)) {
            viewAttributes.put(ERROR_ATTRIBUTE, Constants.PHONE_NOT_VALID);
            passErrorToView(request, response, viewAttributes);
            return;
        }
        // all request parameters are valid

        if(UserDAO.findUserByEmail(email) != null) {
            viewAttributes.put(ERROR_ATTRIBUTE, Constants.EMAIL_EXISTS);
            passErrorToView(request, response, viewAttributes);
            return;
        }

        // lets create user in DB and make him logged in
        boolean userAdded = false;
        try {
            userAdded = UserDAO.addUser(email, Security.hashPassword(password), name, phoneNumber, LangFilter.UK_LANG);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!userAdded) {
            viewAttributes.put(ERROR_ATTRIBUTE, Constants.OTHER_ERROR);
            passErrorToView(request, response, viewAttributes);
        } else {
            User user = UserDAO.findUserByEmail(email);
            // lets put him into session and redirect to catalog (/)
            HttpSession session = request.getSession(true);
            session.setAttribute(USER_ATTRIBUTE, user);
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