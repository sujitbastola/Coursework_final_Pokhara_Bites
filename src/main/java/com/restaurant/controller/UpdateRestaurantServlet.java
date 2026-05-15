package com.restaurant.controller;

import com.restaurant.dao.RestaurantDAO;
import com.restaurant.models.Restaurant;
import com.restaurant.utils.ValidationUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/restaurants/edit")
public class UpdateRestaurantServlet extends HttpServlet {
    private final RestaurantDAO restaurantDAO = new RestaurantDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Restaurant restaurant = restaurantDAO.findById(ValidationUtils.parsePositiveInt(req.getParameter("id"), 0));
            if (restaurant == null) {
                resp.sendRedirect(req.getContextPath() + "/admin/restaurants?error=Restaurant not found");
                return;
            }
            req.setAttribute("restaurant", restaurant);
            req.getRequestDispatcher("/WEB-INF/views/admin/edit-restaurant.jsp").forward(req, resp);
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/admin/restaurants?error=Unable to edit restaurant");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Restaurant restaurant = AddRestaurantServlet.read(req);
        restaurant.setId(ValidationUtils.parsePositiveInt(req.getParameter("id"), 0));
        if (restaurant.getId() == 0 || !AddRestaurantServlet.valid(restaurant)) {
            req.setAttribute("error", "Please correct the restaurant details.");
            req.setAttribute("restaurant", restaurant);
            req.getRequestDispatcher("/WEB-INF/views/admin/edit-restaurant.jsp").forward(req, resp);
            return;
        }
        try {
            restaurantDAO.update(restaurant);
            resp.sendRedirect(req.getContextPath() + "/admin/restaurants?success=Restaurant updated");
        } catch (Exception e) {
            req.setAttribute("error", "Could not update restaurant.");
            req.setAttribute("restaurant", restaurant);
            req.getRequestDispatcher("/WEB-INF/views/admin/edit-restaurant.jsp").forward(req, resp);
        }
    }
}
