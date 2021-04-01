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

    public static final String CATEGORY_VIEW_PATH = "/WEB-INF/jsp/category.jsp";

    public static final int PAGE_SIZE = 6;
    public static final String ID_PARAMETER = "id";
    public static final String BRAND_PARAMETER = "brand";
    public static final String PRICE_FROM_PARAMETER = "priceFrom";
    public static final String PRICE_TO_PARAMETER = "priceTo";
    public static final String SORT_PARAMETER = "sort";
    public static final String PAGE_PARAMETER = "page";
    public static final String PAGE_NUMBER_ATTRIBUTE = "pageNumber";
    public static final String NEXT_PAGE_EXISTS_ATTRIBUTE = "nextPageExists";
    public static final String QUERY_WITH_NO_PAGE_ATTRIBUTE = "queryWithNoPage";
    public static final String QUERY_WITH_NO_SORT_ATTRIBUTE = "queryWithNoSort";
    public static final String CATEGORY_ID_ATTRIBUTE = "categoryId";
    public static final String ALL_BRANDS_ATTRIBUTE = "allBrands";
    public static final String PRODUCTS_ATTRIBUTE = "products";
    public static final String CATEGORY_ATTRIBUTE = "category";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        long categoryId = RequestUtils.getLongParameter(request, ID_PARAMETER);
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
        List<String> validBrandParams = validateBrands(request.getParameterValues(BRAND_PARAMETER), allBrands);

        BigDecimal priceFrom = RequestUtils.getBigDecimalParameter(request, PRICE_FROM_PARAMETER);
        BigDecimal priceTo = RequestUtils.getBigDecimalParameter(request, PRICE_TO_PARAMETER);
        if (priceFrom != null && priceFrom.compareTo(BigDecimal.ZERO) < 0 ) {
            priceFrom = null;
        }
        if (priceTo != null && (priceTo.compareTo(BigDecimal.ZERO) < 0 || (priceFrom != null && priceTo.compareTo(priceFrom) < 0))) {
            priceTo = null;
        }

        String sort = RequestUtils.getStringParameter(request, SORT_PARAMETER);
        if (sort == null) {
            sort = Constants.SORT_NEW_FIRST;
        }

        // Pagination
        int pageNumber = RequestUtils.getIntParameter(request, PAGE_PARAMETER);
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
        parameterMap.remove(PAGE_PARAMETER);
        request.setAttribute(PAGE_NUMBER_ATTRIBUTE, pageNumber);
        request.setAttribute(NEXT_PAGE_EXISTS_ATTRIBUTE, nextPageExists);
        request.setAttribute(QUERY_WITH_NO_PAGE_ATTRIBUTE, RequestUtils.parameterMapToQuery(parameterMap));
        // Pagination ended

        // removing sort parameter so jsp can add it itself
        parameterMap.remove(SORT_PARAMETER);
        request.setAttribute(QUERY_WITH_NO_SORT_ATTRIBUTE, RequestUtils.parameterMapToQuery(parameterMap));
        request.setAttribute(SORT_PARAMETER, sort);

        request.setAttribute(CATEGORY_ID_ATTRIBUTE, categoryId);
        request.setAttribute(PRICE_FROM_PARAMETER, priceFrom);
        request.setAttribute(PRICE_TO_PARAMETER, priceTo);

        // passing list of all brands to view
        String[] brandsArr = new String[allBrands.size()];
        request.setAttribute(ALL_BRANDS_ATTRIBUTE, allBrands.toArray(brandsArr));

        request.setAttribute(PRODUCTS_ATTRIBUTE, products);
        request.setAttribute(CATEGORY_ATTRIBUTE, category);
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