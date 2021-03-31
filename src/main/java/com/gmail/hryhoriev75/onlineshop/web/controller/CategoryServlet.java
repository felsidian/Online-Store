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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for products page
 * Only shows products from specified category (via get parameter ?id=XXX)
 * Allows sorting, pagination and filtering product list
 */
@WebServlet(name = "CategoryServlet", value = Path.CATEGORY_PATH)
public class CategoryServlet extends HttpServlet {

    private static final String CATEGORY_VIEW_PATH = "/WEB-INF/jsp/category.jsp";

    private static final int PAGE_SIZE = 6;

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

        List<String> allBrands = ProductDAO.getBrandsByCategory(categoryId);
        List<String> validBrandParams = validateBrands(request.getParameterValues("brand"), allBrands);

        BigDecimal priceFrom = RequestUtils.getBigDecimalParameter(request, "priceFrom");
        BigDecimal priceTo = RequestUtils.getBigDecimalParameter(request, "priceTo");
        if (priceFrom != null && priceFrom.compareTo(BigDecimal.ZERO) < 0 ) {
            priceFrom = null;
        }
        if (priceTo != null && (priceTo.compareTo(BigDecimal.ZERO) < 0 || (priceFrom != null && priceTo.compareTo(priceFrom) < 0))) {
            priceTo = null;
        }

        String sort = RequestUtils.getStringParameter(request, "sort");
        if (sort == null) {
            sort = Constants.SORT_NEW_FIRST;
        }

        // Pagination
        int pageNumber = RequestUtils.getIntParameter(request, "page");
        if (pageNumber <= 1) {
            pageNumber = 1;
        }
        // we querying +1 product to check if there are something to show on next page
        List<Product> products = ProductDAO.getAllProductsByCategory(category, sort, priceFrom, priceTo, validBrandParams, (pageNumber - 1) * PAGE_SIZE, PAGE_SIZE + 1);
        boolean nextPageExists = false;
        if(products.size() > PAGE_SIZE) {
            nextPageExists = true;
            products.remove(products.size() - 1);
        }

        Map<String, String[]> parameterMap = new HashMap<>(request.getParameterMap());
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

        request.setAttribute("categoryId", categoryId);
        request.setAttribute("priceFrom", priceFrom);
        request.setAttribute("priceTo", priceTo);

        // passing list of all brands to view
        String[] brandsArr = new String[allBrands.size()];
        request.setAttribute("allBrands", allBrands.toArray(brandsArr));

        request.setAttribute("products", products);
        request.setAttribute("category", category);
        request.getRequestDispatcher(CATEGORY_VIEW_PATH).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doGet(request, response);
    }

    // return list of brands from brands array if each contains in allBrands or empty list
    private List<String> validateBrands(String[] brands, List<String> allBrands) {
        List<String> validBrandList = new ArrayList<>();
        if(brands != null && brands.length > 0) {
            for (String brand : brands) {
                if(allBrands.contains(brand)) {
                    validBrandList.add(brand);
                }
            }
        }
        return validBrandList;
    }

}