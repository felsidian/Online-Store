package com.gmail.hryhoriev75.onlineshop.model;

import com.gmail.hryhoriev75.onlineshop.Constants;
import com.gmail.hryhoriev75.onlineshop.db.DBHelper;
import com.gmail.hryhoriev75.onlineshop.model.entity.Category;
import com.gmail.hryhoriev75.onlineshop.model.entity.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductDAO {

    private static final String SQL_GET_ALL_PRODUCTS = "SELECT * FROM product LEFT JOIN category ON category_id=category.id";
    private static final String SQL_GET_ALL_PRODUCTS_ORDERED = SQL_GET_ALL_PRODUCTS + " ORDER BY product.id DESC";
    private static final String SQL_GET_LIMIT_PRODUCTS = SQL_GET_ALL_PRODUCTS_ORDERED + " LIMIT ?";
    private static final String SQL_FIND_PRODUCT_BY_ID = SQL_GET_ALL_PRODUCTS + " WHERE product.id=?";
    private static final String SQL_GET_ALL_PRODUCTS_BY_CATEGORY = "SELECT * FROM product WHERE category_id=? LIMIT ?, ?";
    private static final String SQL_GET_ALL_CATEGORIES = "SELECT * FROM category";
    private static final String SQL_FIND_CATEGORY_BY_ID ="SELECT * FROM category WHERE id=?";

    private static final String TABLE_CATEGORY = "category";

    private static final String FIELD_ID = "id";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_BRAND = "brand";
    private static final String FIELD_DESCRIPTION = "description";
    private static final String FIELD_CREATE_TIME = "create_time";
    private static final String FIELD_PRICE = "price";
    private static final String FIELD_IMAGE_URL = "image_url";
    private static final String FIELD_CATEGORY_ID = "category_id";

    private static final Map<String, String> COLUMN_TO_SORT_MAP = Map.of(Constants.SORT_A_Z, "name", Constants.SORT_Z_A, "name",
            Constants.SORT_CHEAP_FIRST, "price", Constants.SORT_EXPENSIVE_FIRST, "price", Constants.SORT_NEW_FIRST, "id");
    private static final Map<String, String> SORT_DIRECTION_MAP = Map.of(Constants.SORT_A_Z, "ASC", Constants.SORT_Z_A, "DESC",
            Constants.SORT_CHEAP_FIRST, "ASC", Constants.SORT_EXPENSIVE_FIRST, "DESC", Constants.SORT_NEW_FIRST, "DESC");

    public static Product findProductById(long id) {
        Product product = null;
        try (Connection con = DBHelper.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_FIND_PRODUCT_BY_ID)) {
            pst.setLong(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    product = mapProductAndCategory(rs);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return product;
    }

    public static List<Product> getProducts(long limit) {
        List<Product> products = new ArrayList<>();
        try (Connection con = DBHelper.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_GET_LIMIT_PRODUCTS)) {
            pst.setLong(1, limit);
            try(ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    products.add(mapProductAndCategory(rs));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return products;
    }

    public static List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        try (Connection con = DBHelper.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_GET_ALL_CATEGORIES);
             ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    categories.add(mapCategory(rs));
                }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return categories;
    }

    public static List<Product> getAllProductsByCategory(Category category, String sort, long offset, long limit) {
        List<Product> list = new ArrayList<>((int)limit);
        try (Connection con = DBHelper.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement("SELECT * FROM product WHERE category_id=? ORDER BY " +
                     COLUMN_TO_SORT_MAP.getOrDefault(sort,"") + " " + SORT_DIRECTION_MAP.getOrDefault(sort,"") + " LIMIT ?, ?")) {
            pst.setLong(1, category.getId());
            pst.setLong(2, offset);
            pst.setLong(3, limit);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Product product = mapProduct(rs);
                    product.setCategory(category);
                    list.add(product);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public static Category findCategoryById(long id) {
        Category category = null;
        try (Connection con = DBHelper.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_FIND_CATEGORY_BY_ID)) {
            pst.setLong(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    category = mapCategory(rs);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return category;
    }

    private static Product mapProductAndCategory(ResultSet rs) throws SQLException {
        Product product = mapProduct(rs);
        Category category = mapCategory(rs);
        product.setCategory(category);
        return product;
    }

    private static Product mapProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getLong(FIELD_ID));
        product.setName(rs.getString(FIELD_NAME));
        product.setBrand(rs.getString(FIELD_BRAND));
        product.setDescription(rs.getString(FIELD_DESCRIPTION));
        product.setCreateTime(rs.getTimestamp(FIELD_CREATE_TIME).toInstant());
        product.setPrice(rs.getBigDecimal(FIELD_PRICE));
        product.setImageUrl(rs.getString(FIELD_IMAGE_URL));
        return product;
    }

    private static Category mapCategory(ResultSet rs) throws SQLException {
        Category category = new Category();
        category.setId(rs.getLong(TABLE_CATEGORY + "." + FIELD_ID));
        category.setName(rs.getString(TABLE_CATEGORY + "." + FIELD_NAME));
        category.setDescription(rs.getString(TABLE_CATEGORY + "." + FIELD_DESCRIPTION));
        return category;
    }

}
