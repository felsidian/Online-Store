package com.gmail.hryhoriev75.onlineshop.model;

import com.gmail.hryhoriev75.onlineshop.db.DBHelper;
import com.gmail.hryhoriev75.onlineshop.model.entity.Order;
import com.gmail.hryhoriev75.onlineshop.model.entity.Product;
import com.gmail.hryhoriev75.onlineshop.model.entity.User;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Data access object for Order entity
 */
public class OrderDAO {

    public static final String SQL_ORDER_BY_ID = "SELECT * FROM `order` LEFT JOIN status ON status_id=status.id WHERE order.id=?";
    public static final String SQL_CREATE_ORDER = "INSERT INTO `order`(user_id,create_time,status_id)" +
            "VALUES(?,?,(SELECT id FROM status WHERE name=? LIMIT 1))";
    public static final String SQL_ADD_ORDER_CONTENT = "INSERT INTO order_content(order_id,product_id,quantity,price)" +
            "VALUES(?,?,?,(SELECT price FROM product WHERE id=? LIMIT 1))";
    public static final String SQL_GET_ORDER_CONTENT = "SELECT * FROM order_content INNER JOIN product ON product_id=product.id WHERE order_id=?";
    public static final String SQL_ORDERS_BY_USER_ID = "SELECT `order`.*, status.name FROM `order` LEFT JOIN status ON status_id=status.id WHERE user_id=?";
    public static final String SQL_GET_ALL_ORDERS = "SELECT `order`.*, status.name FROM `order` LEFT JOIN status ON status_id=status.id";
    public static final String SQL_UPDATE_STATUS = "UPDATE `order` SET status_id=? WHERE id=?";

    public static final String TABLE_STATUS = "status";
    public static final String TABLE_ORDER_CONTENT = "order_content";
    public static final String FIELD_ID = "id";
    public static final String FIELD_USER_ID = "user_id";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_CREATE_TIME = "create_time";
    public static final String FIELD_PRICE = "price";
    public static final String FIELD_IMAGE_URL = "image_url";
    public static final String FIELD_QUANTITY = "quantity";

    /**
     * @param id Order identifier
     * @return Order entity or null if wasn't found
     */
    public static Order findOrderById(long id) {
        Order order = null;
        try (Connection con = DBHelper.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_ORDER_BY_ID)) {
            pst.setLong(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if(rs.next())
                    order = mapOrder(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return order;
    }

    /**
     * Creating new order made by user with given id
     *
     * @param userId User identifier
     * @param createTime Time when order was created
     * @param orderContent List of records order consists of
     * @return Order id if order was successfully created, 0 otherwise
     */
    public static long createOrder(long userId, Instant createTime, List<Order.Record> orderContent) {
        long orderId = addOrder(userId, createTime, Order.Status.CREATED);
        if (orderId > 0) {
            addOrderContent(orderId, orderContent);
        }
        return orderId;
    }

    /**
     * Creating new order
     *
     * @return Order id if order was successfully created, 0 otherwise
     */
    private static long addOrder(long userId, Instant createTime, Order.Status status) {
        long orderId = 0;
        try (Connection con = DBHelper.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_CREATE_ORDER, Statement.RETURN_GENERATED_KEYS)) {
            pst.setLong(1, userId);
            pst.setTimestamp(2, Timestamp.from(createTime));
            pst.setString(3, status.toString());

            int affectedRows = pst.executeUpdate();
            if (affectedRows > 0) {
                try(ResultSet generatedKeys = pst.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        orderId = generatedKeys.getLong(1);
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return orderId;
    }

    /**
     * Adding list of records order consists of to DB
     */
    private static void addOrderContent(long orderId, List<Order.Record> orderContent) {
        try (Connection con = DBHelper.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_ADD_ORDER_CONTENT)) {
            for(Order.Record record : orderContent) {
                pst.setLong(1, orderId);
                pst.setLong(2, record.getProduct().getId());
                pst.setLong(3, record.getQuantity());
                pst.setLong(4, record.getProduct().getId());
                pst.addBatch();
            }
            pst.executeBatch();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Returns list of records order with given id consists of
     *
     * @return List of Order.Record items. If any present returns empty list.
     */
    public static List<Order.Record> getOrderContent(long orderId) {
        List<Order.Record> orderContent = new ArrayList<>();
        try (Connection con = DBHelper.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_GET_ORDER_CONTENT)) {
            pst.setLong(1, orderId);
            try(ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    orderContent.add(mapOrderContent(rs));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return orderContent;
    }

    /**
     * Returns list of orders user with given id made
     *
     * @return List of Order entities. If any present returns empty list.
     */
    public static List<Order> getOrdersByUserId(long orderId) {
        List<Order> orders = new ArrayList<>();
        try (Connection con = DBHelper.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_ORDERS_BY_USER_ID)) {
            pst.setLong(1, orderId);
            try(ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapOrder(rs));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return orders;
    }

    /**
     * Returns list of all orders
     *
     * @return List of Order entities. If any present returns empty list.
     */
    public static List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        try (Connection con = DBHelper.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_GET_ALL_ORDERS)) {
            try(ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapOrder(rs));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return orders;
    }

    /**
     * Updates status of order with given id
     */
    public static void updateStatus(long orderId, int statusId) {
        try (Connection con = DBHelper.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_UPDATE_STATUS)) {
            pst.setInt(1, statusId);
            pst.setLong(2, orderId);
            pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Extracts Order entity from the result set row.
     */
    private static Order mapOrder(ResultSet rs) {
        Order order = null;
        try {
            order = new Order();
            order.setId(rs.getLong(FIELD_ID));
            order.setUserId(rs.getLong(FIELD_USER_ID));
            order.setCreateTime(new Date(rs.getTimestamp(FIELD_CREATE_TIME).getTime()));
            System.out.println(rs.getString(TABLE_STATUS + "." + FIELD_NAME).toUpperCase());
            order.setStatus(Order.Status.valueOf(rs.getString(TABLE_STATUS + "." + FIELD_NAME).toUpperCase()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return order;
    }

    /**
     * Extracts Order.Record from the result set row.
     */
    private static Order.Record mapOrderContent(ResultSet rs) {
        Order.Record record = null;
        try {
            record = new Order.Record();
            Product product = new Product();
            product.setId(rs.getLong(FIELD_ID));
            product.setName(rs.getString(FIELD_NAME));
            product.setImageUrl(rs.getString(FIELD_IMAGE_URL));
            record.setProduct(product);
            record.setQuantity(rs.getInt(FIELD_QUANTITY));
            record.setPrice(rs.getBigDecimal(TABLE_ORDER_CONTENT + "." + FIELD_PRICE));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return record;
    }

}
