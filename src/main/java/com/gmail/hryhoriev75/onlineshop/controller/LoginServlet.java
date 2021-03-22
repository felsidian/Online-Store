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

@WebServlet(name = "LoginServlet", value = "/singin")
public class LoginServlet extends HttpServlet {

    private static final String LOGIN_VIEW_PATH = "/WEB-INF/jsp/singin.jsp";
    private static final String CATALOG_PATH = "/";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        RequestDispatcher disp = request.getRequestDispatcher(LOGIN_VIEW_PATH);
        disp.forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if(!Security.isEmailValid(email)) {
            passErrorToView(request, response, "Email isn't valid", email);
            return;
        }
        if(!Security.isPasswordValid(password)) {
            passErrorToView(request, response, "Password isn't valid", email);
            return;
        }
        User user = UserDAO.findUserByEmail(email);
        if(user == null) {
            passErrorToView(request, response, "User with this email wasn't found", email);
            return;
        }
        try {
            if(!Security.isPasswordCorrect(password, user.getPassword())) {
                passErrorToView(request, response, "Wrong password", email);
                return;
            }
        } catch (Exception e) {
            passErrorToView(request, response, "Wrong password", email);
            return;
        }

        HttpSession session = request.getSession(true);
        session.setAttribute("email", user.getEmail());
        response.sendRedirect(CATALOG_PATH);
    }

    private void passErrorToView(HttpServletRequest request, HttpServletResponse response, String error, String email) throws ServletException, IOException {
        request.setAttribute("email", email);
        request.setAttribute("error", error);
        RequestDispatcher disp = request.getRequestDispatcher(LOGIN_VIEW_PATH);
        disp.forward(request, response);
    }

    public void destroy() {
    }
}