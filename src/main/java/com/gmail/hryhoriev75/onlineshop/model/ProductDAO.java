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

/**
 * Data access object for Product entity
 */
public class ProductDAO {

    private static final String TABLE_CATEGORY = "category";
    private static final String FIELD_ID = "id";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_BRAND = "brand";
    private static final String FIELD_DESCRIPTION = "description";
    private static final String FIELD_CREATE_TIME = "create_time";
    private static final String FIELD_PRICE = "price";
    private static final String FIELD_IMAGE_URL = "image_url";
    private static final String FIELD_POWER = "power";
    private static final String FIELD_WEIGHT = "weight";
    private static final String FIELD_COUNTRY = "country";

    // Maps sorting parameter with corresponding column name
    private static final Map<String, String> COLUMN_TO_SORT_MAP = Map.of(Constants.SORT_A_Z, "name", Constants.SORT_Z_A, "name",
            Constants.SORT_CHEAP_FIRST, "price", Constants.SORT_EXPENSIVE_FIRST, "price", Constants.SORT_NEW_FIRST, "id");

    // Maps sorting parameter with corresponding order
    private static final Map<String, String> SORT_DIRECTION_MAP = Map.of(Constants.SORT_A_Z, "ASC", Constants.SORT_Z_A, "DESC",
            Constants.SORT_CHEAP_FIRST, "ASC", Constants.SORT_EXPENSIVE_FIRST, "DESC", Constants.SORT_NEW_FIRST, "DESC");
    public static final String SQL_FIND_PRODUCT_BY_ID = "SELECT * FROM product LEFT JOIN category ON category_id=category.id WHERE product.id=?";
    public static final String SQL_GET_POPULAR_PRODUCTS = "SELECT *, COUNT(*) as count FROM order_content JOIN product ON product_id=product.id GROUP BY product_id ORDER BY count DESC LIMIT ?";
    public static final String SQL_GET_ALL_CATEGORIES = "SELECT * FROM category";
    public static final String SQL_GET_BRANDS_BY_CATEGORY = "SELECT brand FROM product WHERE category_id = ? GROUP BY brand";
    public static final String SQL_GET_CATEGORY_BY_ID = "SELECT * FROM category WHERE id=?";
    public static final String SQL_INSERT_PRODUCT = "INSERT INTO product(name,brand,description,create_time,price,image_url,category_id,power,weight,country)VALUES(?,?,?,?,?,?,?,?,?,?)";
    public static final String SQL_UPDATE_PRODUCT = "UPDATE product SET name=?,brand=?,description=?,create_time=?,price=?,image_url=?,category_id=?,power=?,weight=?,country=? WHERE id=?";

    /**
     * @param id Product identifier
     * @return Product entity or null if wasn't found
     */
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

    /**
     * Returns list of most popular products (which were bought more often)
     *
     * @param limit Maximum count of products to return
     * @return List of Product entities
     */
    public static List<Product> getPopularProducts(long limit) {
        List<Product> products = new ArrayList<>();
        try (Connection con = DBHelper.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_GET_POPULAR_PRODUCTS)) {
            pst.setLong(1, limit);
            try(ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    products.add(mapProduct(rs));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return products;
    }

    /**
     * @return List of all Category entities
     */
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


    /**
     * @param categoryId Category identifier
     * @return List of all product's brands given category contains
     */
    public static List<String> getBrandsByCategory(long categoryId) {
        List<String> brands = new ArrayList<>();
        try (Connection con = DBHelper.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_GET_BRANDS_BY_CATEGORY)) {
             pst.setLong(1, categoryId);
             try (ResultSet rs = pst.executeQuery()) {
                 while (rs.next()) {
                     brands.add(rs.getString(1));
                 }
             }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return brands;
    }

    /**
     * Returns list of products (which are satisfy all the filters given by parameters)
     *
     * @param sort Sort constant from Constants class
     * @return List of Product entities. If any present returns empty list.
     */
    public static List<Product> getAllProductsByCategory(Category category, String sort, BigDecimal priceFrom, BigDecimal priceTo, List<String> brandList, int offset, int limit) {
        List<Product> list = new ArrayList<>(limit);
        String priceCause= "";
        if(priceFrom != null)
            priceCause += " price >= " + priceFrom + " AND ";
        if(priceTo != null)
            priceCause += " price <= " + priceTo + " AND ";
        StringBuilder brandCause = new StringBuilder();
        if(!brandList.isEmpty()) {
            brandCause.append(" brand IN('").append(brandList.get(0)).append("'");
        }
        for (int i = 1; i < brandList.size(); i++) {
            brandCause.append(",'").append(brandList.get(i)).append("'");
        }
        if(!brandList.isEmpty()) {
            brandCause.append(") AND ");
        }

        try (Connection con = DBHelper.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement("SELECT * FROM product WHERE " + priceCause + brandCause + " category_id=? ORDER BY " +
                     COLUMN_TO_SORT_MAP.get(sort) + " " + SORT_DIRECTION_MAP.get(sort) + " LIMIT ?, ?")) {
            pst.setLong(1, category.getId());
            pst.setInt(2, offset);
            pst.setInt(3, limit);
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

    /**
     * @param id Category identifier
     * @return Category entity or null if wasn't found
     */
    public static Category findCategoryById(long id) {
        Category category = null;
        try (Connection con = DBHelper.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_GET_CATEGORY_BY_ID)) {
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

    /**
     * Adding or editing existing Product
     *
     * @param product Product entity. If product.getId() > 0, then product will be updated. Otherwise new Product will be created
     * @return Product id if it was successfully added or updated, 0 otherwise
     */
    public static long addOrUpdateProduct(Product product) {
        long productId = 0;
        // Since insert and update SQL queries are similar, we preparing statement in one line
        try (Connection con = DBHelper.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(product.getId() > 0 ? SQL_UPDATE_PRODUCT : SQL_INSERT_PRODUCT, Statement.RETURN_GENERATED_KEYS)) {
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

    /**
     * Extracts Product and Category from the result set row.
     * @return Product entity with Category set
     */
    private static Product mapProductAndCategory(ResultSet rs) throws SQLException {
        Product product = mapProduct(rs);
        Category category = mapCategory(rs);
        product.setCategory(category);
        return product;
    }

    /**
     * Extracts Product from the result set row.
     */
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

    /**
     * Extracts Category from the result set row.
     */
    private static Category mapCategory(ResultSet rs) throws SQLException {
        Category category = new Category();
        category.setId(rs.getLong(TABLE_CATEGORY + "." + FIELD_ID));
        category.setName(rs.getString(TABLE_CATEGORY + "." + FIELD_NAME));
        category.setDescription(rs.getString(TABLE_CATEGORY + "." + FIELD_DESCRIPTION));
        return category;
    }

}
