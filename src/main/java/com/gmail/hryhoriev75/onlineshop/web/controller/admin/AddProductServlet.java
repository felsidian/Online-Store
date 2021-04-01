package com.gmail.hryhoriev75.onlineshop.web.controller.admin;

import com.gmail.hryhoriev75.onlineshop.Constants;
import com.gmail.hryhoriev75.onlineshop.model.ProductDAO;
import com.gmail.hryhoriev75.onlineshop.model.entity.Category;
import com.gmail.hryhoriev75.onlineshop.model.entity.Product;
import com.gmail.hryhoriev75.onlineshop.model.entity.User;
import com.gmail.hryhoriev75.onlineshop.web.Path;
import com.gmail.hryhoriev75.onlineshop.web.utils.RequestUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Add or edit product page controller
 * Available only for admin
 * In case of get request method shows either a blank form ot filled form for editing
 * Product specifying via request parameter (?id=XXX)
 * In case of post request method validating parameters and updating product info or acreating a new one
 * If error pass them back to JSP view
 */
@WebServlet(name = "AddProductServlet", value = Path.ADD_PRODUCT_PATH)
public class AddProductServlet extends HttpServlet {

    public static final String ADD_PRODUCT_VIEW_PATH = "/WEB-INF/jsp/add_product.jsp";

    public static final String PRODUCT_ATTRIBUTE = "product";
    public static final String CATEGORIES_ATTRIBUTE = "categories";
    public static final String ERROR_ATTRIBUTE = "error";

    public static final String NAME_PARAMETER = "name";
    public static final String CATEGORY_ID_PARAMETER = "categoryId";
    public static final String PRICE_PARAMETER = "price";
    public static final String BRAND_PARAMETER = "brand";
    public static final String DESCRIPTION_PARAMETER = "description";
    public static final String IMAGE_URL_PARAMETER = "imageUrl";
    public static final String POWER_PARAMETER = "power";
    public static final String WEIGHT_PARAMETER = "weight";
    public static final String COUNTRY_PARAMETER = "country";
    public static final String ID_PARAMETER = "id";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        long productId = RequestUtils.getLongParameter(request, "id");
        if (productId > 0) {
            Product product = ProductDAO.findProductById(productId);
            if (product != null) {
                request.setAttribute(PRODUCT_ATTRIBUTE, product);
            }
        }
        List<Category> categories = ProductDAO.getAllCategories();
        request.setAttribute(CATEGORIES_ATTRIBUTE, categories);
        request.getRequestDispatcher(ADD_PRODUCT_VIEW_PATH).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Product product = retrieveProductFromRequest(request);

        // passing product back to view in case of error
        request.setAttribute(PRODUCT_ATTRIBUTE, product);

        if (product.getName() == null || product.getName().isBlank()) {
            passErrorToView(request, response, Constants.NAME_EMPTY_ERROR);
            return;
        }
        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            passErrorToView(request, response, Constants.PRICE_NOT_VALID);
            return;
        }
        if (product.getCategory() == null) {
            passErrorToView(request, response, Constants.WRONG_CATEGORY);
            return;
        }

        long productId = ProductDAO.addOrUpdateProduct(product);
        response.sendRedirect(Path.PRODUCT_PATH + "?id=" + productId);
    }

    private Product retrieveProductFromRequest(HttpServletRequest request) {
        Product product = new Product();
        product.setId(RequestUtils.getLongParameter(request, ID_PARAMETER));
        product.setName(request.getParameter(NAME_PARAMETER));

        Category category = ProductDAO.findCategoryById(RequestUtils.getLongParameter(request, CATEGORY_ID_PARAMETER));
        product.setCategory(category);

        product.setPrice(RequestUtils.getBigDecimalParameter(request, PRICE_PARAMETER));
        product.setBrand(request.getParameter(BRAND_PARAMETER));
        product.setDescription(request.getParameter(DESCRIPTION_PARAMETER));
        product.setCreateTime(new Date());

        String imageUrl = request.getParameter(IMAGE_URL_PARAMETER);
        product.setImageUrl(imageUrl != null && !imageUrl.equals("") ? imageUrl: Path.BLANK_IMAGE);
        product.setPower(RequestUtils.getIntParameter(request, POWER_PARAMETER));
        product.setWeight(RequestUtils.getBigDecimalParameter(request, WEIGHT_PARAMETER));
        product.setCountry(request.getParameter(COUNTRY_PARAMETER));
        return product;
    }

    private void passErrorToView(HttpServletRequest request, HttpServletResponse response, String error) throws ServletException, IOException {
        request.setAttribute(ERROR_ATTRIBUTE, error);
        request.getRequestDispatcher(ADD_PRODUCT_VIEW_PATH).forward(request, response);
    }

}