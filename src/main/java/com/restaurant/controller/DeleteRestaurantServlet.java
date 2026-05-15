package com.restaurant.controller;

import com.restaurant.dao.RestaurantDAO;
import com.restaurant.utils.ValidationUtils;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/restaurants/delete")
public class DeleteRestaurantServlet extends HttpServlet {
    private final RestaurantDAO restaurantDAO = new RestaurantDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            restaurantDAO.delete(ValidationUtils.parsePositiveInt(req.getParameter("id"), 0));
            resp.sendRedirect(req.getContextPath() + "/admin/restaurants?success=Restaurant deleted");
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/admin/restaurants?error=Delete failed. Remove related orders first.");
        }
    }
}
