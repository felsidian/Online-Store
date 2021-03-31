package com.gmail.hryhoriev75.onlineshop.web.filter;

import com.gmail.hryhoriev75.onlineshop.model.entity.User;
import com.gmail.hryhoriev75.onlineshop.web.Path;
import com.gmail.hryhoriev75.onlineshop.web.utils.RequestUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Access restriction filter
 */
@WebFilter("/*")
public class LoginFilter implements Filter {

    private static final List<String> ADMIN_PATHS = Arrays.asList(Path.ADD_PRODUCT_PATH, Path.USERS_PATH,
            Path.ALL_ORDERS_PATH, Path.UPDATE_PATH);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest req = (HttpServletRequest) request;

            // if we trying to open admin page but we are not admin ourself then go to 404
            if(ADMIN_PATHS.contains(req.getServletPath())) {
                User user = RequestUtils.getSessionAttribute(req, "user", User.class);
                if(user == null || user.getRole() != User.Role.ADMIN) {
                    request.getRequestDispatcher(Path.NOT_FOUND_PATH).forward(request, response);
                    return;
                }
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}


