package com.gmail.hryhoriev75.onlineshop.web.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Locale;

@WebFilter("/*")
public class LangFilter implements Filter {

    public static final String EN_LANG = "en";
    public static final String UK_LANG = "uk";
    public static final String LANG_COOKIE_NAME = "lang";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest req = (HttpServletRequest) request;
            if(!req.getServletPath().contains("css") && !req.getServletPath().contains("js")) {
                request.setCharacterEncoding("UTF-8");
                response.setContentType("text/html; charset=UTF-8");
                response.setCharacterEncoding("UTF-8");
            }
        }
        HttpServletRequest req = (HttpServletRequest) request;
        String lang = getCookieValue(LANG_COOKIE_NAME, req);
        if(lang != null && (lang.equals(EN_LANG) || lang.equals(UK_LANG)))
            request.setAttribute("lang", lang);
        else
            request.setAttribute("lang", UK_LANG);
        chain.doFilter(request, response);
    }

    private String getCookieValue(String name, HttpServletRequest request) {
        if(request != null) {
            Cookie[] cookies = request.getCookies();
            if(cookies != null)
                for (Cookie cookie : cookies) {
                    if (name.equals(cookie.getName().toLowerCase())) {
                        return cookie.getValue();
                    }
                }
        }
        return null;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}

