package com.restaurant.dao;

import com.restaurant.models.MenuItem;
import com.restaurant.queries.Queries;
import com.restaurant.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MenuItemDAO {
    public boolean add(MenuItem item) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(Queries.INSERT_MENU_ITEM)) {
            fill(ps, item);
            return ps.executeUpdate() == 1;
        }
    }

    public boolean update(MenuItem item) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(Queries.UPDATE_MENU_ITEM)) {
            fill(ps, item);
            ps.setInt(7, item.getId());
            return ps.executeUpdate() == 1;
        }
    }

    public boolean delete(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(Queries.DELETE_MENU_ITEM)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }

    public MenuItem findById(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(Queries.FIND_MENU_ITEM)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public List<MenuItem> findAll() throws SQLException {
        return list(Queries.LIST_MENU_ITEMS, 0);
    }

    public List<MenuItem> findByRestaurant(int restaurantId) throws SQLException {
        return list(Queries.LIST_MENU_BY_RESTAURANT, restaurantId);
    }

    public List<MenuItem> searchByRestaurant(int restaurantId, String keyword) throws SQLException {
        String like = "%" + keyword + "%";
        String sql = "SELECT mi.*, r.name restaurant_name FROM menu_items mi "
                + "JOIN restaurants r ON r.id=mi.restaurant_id "
                + "WHERE mi.restaurant_id=? AND (mi.name LIKE ? OR mi.description LIKE ? OR mi.category LIKE ?) "
                + "ORDER BY mi.category, mi.name";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, restaurantId);
            ps.setString(2, like);
            ps.setString(3, like);
            ps.setString(4, like);
            try (ResultSet rs = ps.executeQuery()) {
                List<MenuItem> items = new ArrayList<>();
                while (rs.next()) {
                    items.add(map(rs));
                }
                return items;
            }
        }
    }

    public int countMenuItems() throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(Queries.COUNT_MENU_ITEMS);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private List<MenuItem> list(String sql, int restaurantId) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (restaurantId > 0) {
                ps.setInt(1, restaurantId);
            }
            try (ResultSet rs = ps.executeQuery()) {
                List<MenuItem> items = new ArrayList<>();
                while (rs.next()) {
                    items.add(map(rs));
                }
                return items;
            }
        }
    }

    private void fill(PreparedStatement ps, MenuItem item) throws SQLException {
        ps.setInt(1, item.getRestaurantId());
        ps.setString(2, item.getName());
        ps.setString(3, item.getDescription());
        ps.setBigDecimal(4, item.getPrice());
        ps.setString(5, item.getCategory());
        ps.setBoolean(6, item.isAvailable());
    }

    private MenuItem map(ResultSet rs) throws SQLException {
        MenuItem item = new MenuItem();
        item.setId(rs.getInt("id"));
        item.setRestaurantId(rs.getInt("restaurant_id"));
        item.setRestaurantName(rs.getString("restaurant_name"));
        item.setName(rs.getString("name"));
        item.setDescription(rs.getString("description"));
        item.setPrice(rs.getBigDecimal("price"));
        item.setCategory(rs.getString("category"));
        item.setAvailable(rs.getBoolean("availability"));
        return item;
    }
}
