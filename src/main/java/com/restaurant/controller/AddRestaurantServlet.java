package com.restaurant.controller;

import java.io.IOException;

import com.restaurant.dao.RestaurantDAO;
import com.restaurant.models.Restaurant;
import com.restaurant.utils.ValidationUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin/restaurants/add")
public class AddRestaurantServlet extends HttpServlet {
    private final RestaurantDAO restaurantDAO = new RestaurantDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/admin/add-restaurant.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Restaurant restaurant = read(req);
        if (!valid(restaurant)) {
            req.setAttribute("error", "Name, address, valid phone, cuisine, and opening hours are required.");
            req.setAttribute("restaurant", restaurant);
            req.getRequestDispatcher("/WEB-INF/views/admin/add-restaurant.jsp").forward(req, resp);
            return;
        }
        try {
            restaurantDAO.add(restaurant);
            resp.sendRedirect(req.getContextPath() + "/admin/restaurants?success=Restaurant added");
        } catch (Exception e) {
            req.setAttribute("error", "Could not add restaurant.");
            req.setAttribute("restaurant", restaurant);
            req.getRequestDispatcher("/WEB-INF/views/admin/add-restaurant.jsp").forward(req, resp);
        }
    }

    static Restaurant read(HttpServletRequest req) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(ValidationUtils.clean(req.getParameter("name")));
        restaurant.setAddress(ValidationUtils.clean(req.getParameter("address")));
        restaurant.setPhone(ValidationUtils.clean(req.getParameter("phone")));
        restaurant.setCuisineType(ValidationUtils.clean(req.getParameter("cuisineType")));
        restaurant.setOpeningHours(ValidationUtils.clean(req.getParameter("openingHours")));
        restaurant.setImageUrl(ValidationUtils.clean(req.getParameter("imageUrl")));
        return restaurant;
    }

    static boolean valid(Restaurant restaurant) {
        return !ValidationUtils.isBlank(restaurant.getName())
                && !ValidationUtils.isBlank(restaurant.getAddress())
                && ValidationUtils.isValidPhone(restaurant.getPhone())
                && !ValidationUtils.isBlank(restaurant.getCuisineType())
                && !ValidationUtils.isBlank(restaurant.getOpeningHours());
    }
}
