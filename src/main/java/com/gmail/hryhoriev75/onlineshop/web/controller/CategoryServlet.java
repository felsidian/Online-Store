package com.gmail.hryhoriev75.onlineshop.web.controller;

import com.gmail.hryhoriev75.onlineshop.Constants;
import com.gmail.hryhoriev75.onlineshop.model.ProductDAO;
import com.gmail.hryhoriev75.onlineshop.model.entity.Category;
import com.gmail.hryhoriev75.onlineshop.model.entity.Product;
import com.gmail.hryhoriev75.onlineshop.web.Path;
import com.gmail.hryhoriev75.onlineshop.web.utils.RequestUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@WebServlet(name = "CategoryServlet", value = Path.CATEGORY_PATH)
public class CategoryServlet extends HttpServlet {

    private static final String CATEGORY_VIEW_PATH = "/WEB-INF/jsp/category.jsp";

    private static final int PAGE_SIZE = 3;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        long categoryId = RequestUtils.getLongParameter(request, "id");
        if (categoryId <= 0) {
            response.sendRedirect(Path.CATALOG_PATH);
            return;
        }
        Category category = ProductDAO.findCategoryById(categoryId);
        if(category == null) {
            request.getRequestDispatcher(Path.NOT_FOUND_PATH).forward(request, response);
            return;
        }

        Map<String, String[]> mapp = request.getParameterMap();
        System.out.println(Arrays.toString(mapp.get("brand")));


        String sort = RequestUtils.getStringParameter(request, "sort");
        if (sort == null) {
            sort = Constants.SORT_NEW_FIRST;
        }

        // Pagination
        long pageNumber = RequestUtils.getLongParameter(request, "page");
        if (pageNumber <= 1) {
            pageNumber = 1;
        }
        // we querying +1 product to check if there are something to show on next page
        List<Product> products = ProductDAO.getAllProductsByCategory(category, sort, (pageNumber - 1) * PAGE_SIZE, PAGE_SIZE + 1);
        boolean nextPageExists = false;
        if(products.size() > PAGE_SIZE) {
            nextPageExists = true;
            products.remove(products.size() - 1);
        }
        Map<String, String> parameterMap = RequestUtils.getParameterMap(request);
        // removing page parameter so jsp can add it itself
        parameterMap.remove("page");
        request.setAttribute("pageNumber", pageNumber);
        request.setAttribute("nextPageExists", nextPageExists);
        request.setAttribute("queryWithNoPage", RequestUtils.parameterMapToQuery(parameterMap));
        // Pagination ended

        // removing sort parameter so jsp can add it itself
        parameterMap.remove("sort");
        request.setAttribute("queryWithNoSort", RequestUtils.parameterMapToQuery(parameterMap));
        request.setAttribute("sort", sort);



        //List<Product> products = ProductDAO.getAllProductsByCategory(category, 0, PAGE_SIZE);
        request.setAttribute("products", products);
        request.setAttribute("category", category);
        request.getRequestDispatcher(CATEGORY_VIEW_PATH).forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doGet(request, response);
    }

}