package com.gmail.hryhoriev75.onlineshop.web.controller;

import com.gmail.hryhoriev75.onlineshop.model.ProductDAO;
import com.gmail.hryhoriev75.onlineshop.model.entity.Product;
import com.gmail.hryhoriev75.onlineshop.web.Path;
import com.gmail.hryhoriev75.onlineshop.web.utils.RequestUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Controller for product page
 * Product specifying via request parameter (?id=XXX)
 * Shows product information and add to cart button
 */
@WebServlet(name = "ProductServlet", value = Path.PRODUCT_PATH)
public class ProductServlet extends HttpServlet {

    public static final String PRODUCT_VIEW_PATH = "/WEB-INF/jsp/product.jsp";
    public static final String ID_PARAMETER = "id";
    public static final String PRODUCT_ATTRIBUTE = "product";

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        long productId = RequestUtils.getLongParameter(request, ID_PARAMETER);
        if (productId <= 0) {
            request.getRequestDispatcher(Path.NOT_FOUND_PATH).forward(request, response);
            return;
        }
        Product product = ProductDAO.findProductById(productId);
        if(product == null) {
            request.getRequestDispatcher(Path.NOT_FOUND_PATH).forward(request, response);
            return;
        }
        request.setAttribute(PRODUCT_ATTRIBUTE, product);
        request.getRequestDispatcher(PRODUCT_VIEW_PATH).forward(request, response);
    }

}