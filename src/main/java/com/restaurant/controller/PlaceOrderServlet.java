package com.restaurant.controller;

import com.restaurant.dao.OrderDAO;
import com.restaurant.models.CartItem;
import com.restaurant.models.User;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/place-order")
public class PlaceOrderServlet extends HttpServlet {
    private final OrderDAO orderDAO = new OrderDAO();

    @Override
    @SuppressWarnings("unchecked")
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        Integer restaurantId = (Integer) session.getAttribute("cartRestaurantId");
        if (user == null || cart == null || cart.isEmpty() || restaurantId == null) {
            resp.sendRedirect(req.getContextPath() + "/cart?error=Cart is empty");
            return;
        }
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : cart) {
            if (item.getMenuItem().getRestaurantId() != restaurantId || !item.getMenuItem().isAvailable()) {
                resp.sendRedirect(req.getContextPath() + "/cart?error=Cart contains invalid or unavailable items");
                return;
            }
            total = total.add(item.getSubtotal());
        }
        try {
            int orderId = orderDAO.placeOrder(user.getId(), restaurantId, cart, total);
            session.removeAttribute("cart");
            session.removeAttribute("cartRestaurantId");
            resp.sendRedirect(req.getContextPath() + "/orders?success=Order #" + orderId + " placed successfully");
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/checkout?error=Could not place order");
        }
    }
}
