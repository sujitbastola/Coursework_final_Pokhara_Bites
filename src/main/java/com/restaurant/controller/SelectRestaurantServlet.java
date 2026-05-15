package com.restaurant.controller;

import com.restaurant.models.CartItem;
import com.restaurant.utils.ValidationUtils;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/select-restaurant")
public class SelectRestaurantServlet extends HttpServlet {
    @Override
    @SuppressWarnings("unchecked")
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int restaurantId = ValidationUtils.parsePositiveInt(req.getParameter("restaurantId"), 0);
        if (restaurantId == 0) {
            resp.sendRedirect(req.getContextPath() + "/restaurants?error=Please choose a restaurant");
            return;
        }
        HttpSession session = req.getSession();
        Integer cartRestaurantId = (Integer) session.getAttribute("cartRestaurantId");
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cartRestaurantId != null && cart != null && !cart.isEmpty() && cartRestaurantId != restaurantId) {
            resp.sendRedirect(req.getContextPath() + "/restaurants?error=Your cart has items from another restaurant. Clear the cart before switching.");
            return;
        }
        session.setAttribute("selectedRestaurantId", restaurantId);
        resp.sendRedirect(req.getContextPath() + "/menu");
    }
}
