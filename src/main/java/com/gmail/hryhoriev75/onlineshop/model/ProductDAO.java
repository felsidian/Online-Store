package com.gmail.hryhoriev75.onlineshop.model;

import com.gmail.hryhoriev75.onlineshop.Constants;
import com.gmail.hryhoriev75.onlineshop.db.DBHelper;
import com.gmail.hryhoriev75.onlineshop.model.entity.Category;
import com.gmail.hryhoriev75.onlineshop.model.entity.Product;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductDAO {

    private static final String TABLE_CATEGORY = "category";

    private static final String FIELD_ID = "id";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_BRAND = "brand";
    private static final String FIELD_DESCRIPTION = "description";
    private static final String FIELD_CREATE_TIME = "create_time";
    private static final String FIELD_PRICE = "price";
    private static final String FIELD_IMAGE_URL = "image_url";
    private static final String FIELD_CATEGORY_ID = "category_id";
    private static final String FIELD_POWER = "power";
    private static final String FIELD_WEIGHT = "weight";
    private static final String FIELD_COUNTRY = "country";

    private static final Map<String, String> COLUMN_TO_SORT_MAP = Map.of(Constants.SORT_A_Z, "name", Constants.SORT_Z_A, "name",
            Constants.SORT_CHEAP_FIRST, "price", Constants.SORT_EXPENSIVE_FIRST, "price", Constants.SORT_NEW_FIRST, "id");
    private static final Map<String, String> SORT_DIRECTION_MAP = Map.of(Constants.SORT_A_Z, "ASC", Constants.SORT_Z_A, "DESC",
            Constants.SORT_CHEAP_FIRST, "ASC", Constants.SORT_EXPENSIVE_FIRST, "DESC", Constants.SORT_NEW_FIRST, "DESC");

    public static Product findProductById(long id) {
        Product product = null;
        try (Connection con = DBHelper.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement("SELECT * FROM product LEFT JOIN category " +
                     "ON category_id=category.id WHERE product.id=?")) {
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
             PreparedStatement pst = con.prepareStatement("SELECT * FROM product LEFT JOIN category " +
                     "ON category_id=category.id ORDER BY product.id DESC LIMIT ?")) {
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
             PreparedStatement pst = con.prepareStatement("SELECT * FROM category");
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
                     COLUMN_TO_SORT_MAP.get(sort) + " " + SORT_DIRECTION_MAP.get(sort) + " LIMIT ?, ?")) {
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
             PreparedStatement pst = con.prepareStatement("SELECT * FROM category WHERE id=?")) {
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

    public static long addOrUpdateProduct(Product product) {
        long productId = 0;
        String insertSql = "INSERT INTO product(name,brand,description,create_time," +
                "price,image_url,category_id,power,weight,country)VALUES(?,?,?,?,?,?,?,?,?,?)";
        String updateSql = "UPDATE product SET name=?,brand=?,description=?,create_time=?," +
                "price=?,image_url=?,category_id=?,power=?,weight=?,country=? WHERE id=?";
        try (Connection con = DBHelper.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(product.getId() > 0 ? updateSql : insertSql, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, product.getName());
            pst.setString(2, product.getBrand());
            pst.setString(3, product.getDescription());
            pst.setTimestamp(4, new Timestamp(product.getCreateTime().getTime()));
            pst.setBigDecimal(5, product.getPrice());
            pst.setString(6, product.getImageUrl());
            pst.setLong(7, product.getCategory().getId());
            pst.setInt(8, product.getPower());
            pst.setBigDecimal(9, product.getWeight());
            pst.setString(10, product.getCountry());
            if (product.getId() > 0) {
                pst.setLong(11, product.getId());
                productId = product.getId();
            }
            int affectedRows = pst.executeUpdate();
            if (affectedRows > 0) {
                try(ResultSet generatedKeys = pst.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        productId = generatedKeys.getLong(1);
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return productId;
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
        product.setCreateTime(new Date(rs.getTimestamp(FIELD_CREATE_TIME).getTime()));
        product.setPrice(rs.getBigDecimal(FIELD_PRICE));
        product.setImageUrl(rs.getString(FIELD_IMAGE_URL));
        product.setPower(rs.getInt(FIELD_POWER));
        product.setWeight(rs.getBigDecimal(FIELD_WEIGHT));
        product.setCountry(rs.getString(FIELD_COUNTRY));
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
