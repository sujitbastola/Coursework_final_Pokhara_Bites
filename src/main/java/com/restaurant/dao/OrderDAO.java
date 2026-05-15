package com.restaurant.dao;

import com.restaurant.models.CartItem;
import com.restaurant.models.Order;
import com.restaurant.models.OrderItem;
import com.restaurant.queries.Queries;
import com.restaurant.utils.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    public int placeOrder(int userId, int restaurantId, List<CartItem> cart, BigDecimal total) throws SQLException {
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);
            int orderId;
            try (PreparedStatement ps = con.prepareStatement(Queries.INSERT_ORDER, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, userId);
                ps.setInt(2, restaurantId);
                ps.setBigDecimal(3, total);
                ps.setString(4, "PLACED");
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (!keys.next()) {
                        throw new SQLException("Order id was not generated");
                    }
                    orderId = keys.getInt(1);
                }
            }
            try (PreparedStatement ps = con.prepareStatement(Queries.INSERT_ORDER_ITEM)) {
                for (CartItem cartItem : cart) {
                    ps.setInt(1, orderId);
                    ps.setInt(2, cartItem.getMenuItem().getId());
                    ps.setInt(3, cartItem.getQuantity());
                    ps.setBigDecimal(4, cartItem.getMenuItem().getPrice());
                    ps.addBatch();
                }
                ps.executeBatch();
            }
            con.commit();
            return orderId;
        } catch (SQLException e) {
            if (con != null) {
                con.rollback();
            }
            throw e;
        } finally {
            if (con != null) {
                con.setAutoCommit(true);
                con.close();
            }
        }
    }

    public List<Order> findForUser(int userId) throws SQLException {
        return list(Queries.LIST_ORDERS_FOR_USER, userId);
    }

    public List<Order> findAll() throws SQLException {
        return list(Queries.LIST_ALL_ORDERS, 0);
    }

    public int countOrders() throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(Queries.COUNT_ORDERS);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private List<Order> list(String sql, int userId) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (userId > 0) {
                ps.setInt(1, userId);
            }
            try (ResultSet rs = ps.executeQuery()) {
                List<Order> orders = new ArrayList<>();
                while (rs.next()) {
                    Order order = mapOrder(rs);
                    order.setItems(findItems(con, order.getId()));
                    orders.add(order);
                }
                return orders;
            }
        }
    }

    private List<OrderItem> findItems(Connection con, int orderId) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(Queries.LIST_ORDER_ITEMS)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                List<OrderItem> items = new ArrayList<>();
                while (rs.next()) {
                    OrderItem item = new OrderItem();
                    item.setId(rs.getInt("id"));
                    item.setOrderId(rs.getInt("order_id"));
                    item.setMenuItemId(rs.getInt("menu_item_id"));
                    item.setItemName(rs.getString("item_name"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setPrice(rs.getBigDecimal("price"));
                    items.add(item);
                }
                return items;
            }
        }
    }

    private Order mapOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getInt("id"));
        order.setUserId(rs.getInt("user_id"));
        order.setRestaurantId(rs.getInt("restaurant_id"));
        order.setRestaurantName(rs.getString("restaurant_name"));
        order.setTotalAmount(rs.getBigDecimal("total_amount"));
        order.setStatus(rs.getString("status"));
        order.setOrderDate(rs.getTimestamp("order_date"));
        return order;
    }
}
