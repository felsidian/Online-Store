package com.gmail.hryhoriev75.onlineshop.web.controller.admin;

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

@WebServlet(name = "AddProductServlet", value = Path.ADD_PRODUCT_PATH)
public class AddProductServlet extends HttpServlet {

    private static final String ADD_PRODUCT_VIEW_PATH = "/WEB-INF/jsp/add_product.jsp";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        long productId = RequestUtils.getLongParameter(request, "id");
        if (productId > 0) {
            Product product = ProductDAO.findProductById(productId);
            if (product != null) {
                request.setAttribute("product", product);
            }
        }
        List<Category> categories = ProductDAO.getAllCategories();
        request.setAttribute("categories", categories);
        request.getRequestDispatcher(ADD_PRODUCT_VIEW_PATH).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long id = RequestUtils.getLongParameter(request, "id");
        String name = request.getParameter("name");
        long categoryId = RequestUtils.getLongParameter(request, "categoryId");
        BigDecimal price = RequestUtils.getBigDecimalParameter(request, "price");
        String brand = request.getParameter("brand");
        String description = request.getParameter("description");
        String imageUrl = request.getParameter("imageUrl");
        int power = RequestUtils.getIntParameter(request, "power");
        BigDecimal weight = RequestUtils.getBigDecimalParameter(request, "weight");
        String country = request.getParameter("country");

        Category category = ProductDAO.findCategoryById(categoryId);

        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setCategory(category);
        product.setPrice(price);
        product.setBrand(brand);
        product.setDescription(description);
        product.setCreateTime(new Date());
        product.setImageUrl(imageUrl != null && !imageUrl.equals("") ? imageUrl: Path.BLANK_IMAGE);
        product.setPower(power);
        product.setWeight(weight);
        product.setCountry(country);

        // passing product to view in case of error
        request.setAttribute("product", product);

        if (name == null || name.isBlank()) {
            passErrorToView(request, response, "Name cant be empty");
            return;
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            passErrorToView(request, response, "Price cant be less then 0");
            return;
        }
        if (category == null) {
            passErrorToView(request, response, "Wrong category");
            return;
        }

        long productId = ProductDAO.addOrUpdateProduct(product);
        response.sendRedirect(Path.PRODUCT_PATH + "?id=" + productId);
    }

    private void passErrorToView(HttpServletRequest request, HttpServletResponse response, String error) throws ServletException, IOException {
        request.setAttribute("error", error);
        request.getRequestDispatcher(ADD_PRODUCT_VIEW_PATH).forward(request, response);
    }

}