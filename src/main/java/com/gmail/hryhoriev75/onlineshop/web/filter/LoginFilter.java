package com.gmail.hryhoriev75.onlineshop.web.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebFilter("/*")
public class LoginFilter implements Filter {

    private List<String> adminPaths = new ArrayList<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        adminPaths.add("/all-orders");
        adminPaths.add("/add-product");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {

        HttpServletRequest req = (HttpServletRequest) request;
        System.out.println("===========================================");
        System.out.println("getContextPath = " + req.getContextPath());
        System.out.println("getServletPath = " + req.getServletPath());
        System.out.println("getPathTranslated = " + req.getPathTranslated());
        System.out.println("getPathInfo = " + req.getPathInfo());
        System.out.println("getRequestURI = " + req.getRequestURI());
        System.out.println("getRequestURL = " + req.getRequestURL());
        System.out.println("getQueryString = " + req.getQueryString());
        System.out.println("===========================================");
        chain.doFilter(request, response);

        // Example implementation
//        HttpServletRequest request = (HttpServletRequest) req;
//        HttpServletResponse response = (HttpServletResponse) res;
//        HttpSession session = request.getSession(false);
//        String loginURI = request.getContextPath() + "/login";
//
//        boolean loggedIn = session != null && session.getAttribute("user") != null;
//        boolean loginRequest = request.getRequestURI().equals(loginURI);
//
//        if (loggedIn || loginRequest) {
//            chain.doFilter(request, response);
//        } else {
//            response.sendRedirect(loginURI);
//        }
    }

}


