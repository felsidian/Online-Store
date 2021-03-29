package com.gmail.hryhoriev75.onlineshop.model;

import com.gmail.hryhoriev75.onlineshop.db.DBHelper;
import com.gmail.hryhoriev75.onlineshop.model.entity.Order;
import com.gmail.hryhoriev75.onlineshop.model.entity.Product;
import com.gmail.hryhoriev75.onlineshop.model.entity.User;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    private static final String SQL_FIND_ORDER_BY_ID =
            "SELECT * FROM `order` LEFT JOIN status ON status_id=status.id WHERE order.id=?";
    private static final String SQL_FIND_ORDERS_BY_USER_ID =
            "SELECT * FROM `order` LEFT JOIN status ON status_id=status.id WHERE user_id=?";
    private static final String SQL_ADD_ORDER = "INSERT INTO `order`(user_id,create_time,status_id)VALUES(?,?,(SELECT id FROM status WHERE name=? LIMIT 1))";
    private static final String SQL_ADD_ORDER_CONTENT =
            "INSERT INTO order_content(order_id,product_id,quantity,price)VALUES(?,?,?,(SELECT price FROM product WHERE id=? LIMIT 1))";
    private static final String SQL_GET_ORDER_CONTENT =
            "SELECT * FROM order_content INNER JOIN product ON product_id=product.id WHERE order_id=?";


    private static final String TABLE_STATUS = "status";
    private static final String TABLE_ORDER_CONTENT = "order_content";

    private static final String FIELD_ID = "id";
    private static final String FIELD_USER_ID = "user_id";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_CREATE_TIME = "create_time";
    private static final String FIELD_PRICE = "price";
    private static final String FIELD_IMAGE_URL = "image_url";
    private static final String FIELD_QUANTITY = "quantity";

    public static Order findOrderById(long id) {
        Order order = null;
        try (Connection con = DBHelper.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_FIND_ORDER_BY_ID)) {
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

    public static long createOrder(long userId, Instant createTime, List<Order.Record> orderContent) {
        long orderId = addOrder(userId, createTime, Order.Status.CREATED);
        if (orderId > 0) {
            addOrderContent(orderId, orderContent);
        }
        return orderId;
    }

    private static long addOrder(long userId, Instant createTime, Order.Status status) {
        long orderId = 0;
        try (Connection con = DBHelper.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_ADD_ORDER, Statement.RETURN_GENERATED_KEYS)) {
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

    private static boolean addOrderContent(long orderId, List<Order.Record> orderContent) {
        boolean result = false;
        try (Connection con = DBHelper.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_ADD_ORDER_CONTENT)) {
            for(Order.Record record : orderContent) {
                pst.setLong(1, orderId);
                pst.setLong(2, record.getProduct().getId());
                pst.setLong(3, record.getQuantity());
                pst.setLong(4, record.getProduct().getId());
                pst.addBatch();
            }
            result = pst.executeBatch().length > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

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

    public static List<Order> getOrdersByUserId(long orderId) {
        List<Order> orders = new ArrayList<>();
        try (Connection con = DBHelper.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_FIND_ORDERS_BY_USER_ID)) {
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

    private static Order mapOrder(ResultSet rs) {
        Order order = null;
        try {
            order = new Order();
            order.setId(rs.getLong(FIELD_ID));
            order.setUserId(rs.getLong(FIELD_USER_ID));
            order.setCreateTime(new Date(rs.getTimestamp(FIELD_CREATE_TIME).getTime()));
            order.setStatus(Order.Status.valueOf(rs.getString(TABLE_STATUS + "." + FIELD_NAME).toUpperCase()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return order;
    }

    private static Order.Record mapOrderContent(ResultSet rs) {
        Order.Record record = null;
        try {
            System.out.println("mapOrderContent start");
            record = new Order.Record();
            Product product = new Product();
            product.setId(rs.getLong(FIELD_ID));
            product.setName(rs.getString(FIELD_NAME));
            product.setImageUrl(rs.getString(FIELD_IMAGE_URL));
            record.setProduct(product);
            record.setQuantity(rs.getInt(FIELD_QUANTITY));
            record.setPrice(rs.getBigDecimal(TABLE_ORDER_CONTENT + "." + FIELD_PRICE));
            System.out.println(product.getId());
            System.out.println("mapOrderContent end");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return record;
    }

}
