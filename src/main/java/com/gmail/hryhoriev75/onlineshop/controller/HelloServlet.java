package com.gmail.hryhoriev75.onlineshop.controller;

import java.io.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "HelloServlet", value = "")
public class HelloServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello World from HelloServlet";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

//        String address = "/WEB-INF/jsp/page1.jsp";
//        RequestDispatcher disp = request.getRequestDispatcher(address);
//        disp.forward(request, response);

        response.setContentType("text/html");

        // Hello
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + message + "</h1>");
        out.println("</body></html>");
    }

    public void destroy() {
    }
}