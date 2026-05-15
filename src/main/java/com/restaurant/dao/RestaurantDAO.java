package com.restaurant.dao;

import com.restaurant.models.Restaurant;
import com.restaurant.queries.Queries;
import com.restaurant.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RestaurantDAO {
    public boolean add(Restaurant restaurant) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(Queries.INSERT_RESTAURANT)) {
            fill(ps, restaurant);
            return ps.executeUpdate() == 1;
        }
    }

    public boolean update(Restaurant restaurant) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(Queries.UPDATE_RESTAURANT)) {
            fill(ps, restaurant);
            ps.setInt(7, restaurant.getId());
            return ps.executeUpdate() == 1;
        }
    }

    public boolean delete(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(Queries.DELETE_RESTAURANT)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }

    public Restaurant findById(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(Queries.FIND_RESTAURANT)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public List<Restaurant> findAll() throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(Queries.LIST_RESTAURANTS);
             ResultSet rs = ps.executeQuery()) {
            List<Restaurant> restaurants = new ArrayList<>();
            while (rs.next()) {
                restaurants.add(map(rs));
            }
            return restaurants;
        }
    }

    public List<Restaurant> search(String keyword) throws SQLException {
        String like = "%" + keyword + "%";
        String sql = "SELECT * FROM restaurants WHERE name LIKE ? OR address LIKE ? OR cuisine_type LIKE ? ORDER BY name";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
            try (ResultSet rs = ps.executeQuery()) {
                List<Restaurant> restaurants = new ArrayList<>();
                while (rs.next()) {
                    restaurants.add(map(rs));
                }
                return restaurants;
            }
        }
    }

    public int countRestaurants() throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(Queries.COUNT_RESTAURANTS);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private void fill(PreparedStatement ps, Restaurant restaurant) throws SQLException {
        ps.setString(1, restaurant.getName());
        ps.setString(2, restaurant.getAddress());
        ps.setString(3, restaurant.getPhone());
        ps.setString(4, restaurant.getCuisineType());
        ps.setString(5, restaurant.getOpeningHours());
        ps.setString(6, restaurant.getImageUrl());
    }

    private Restaurant map(ResultSet rs) throws SQLException {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(rs.getInt("id"));
        restaurant.setName(rs.getString("name"));
        restaurant.setAddress(rs.getString("address"));
        restaurant.setPhone(rs.getString("phone"));
        restaurant.setCuisineType(rs.getString("cuisine_type"));
        restaurant.setOpeningHours(rs.getString("opening_hours"));
        restaurant.setImageUrl(rs.getString("image_url"));
        return restaurant;
    }
}
