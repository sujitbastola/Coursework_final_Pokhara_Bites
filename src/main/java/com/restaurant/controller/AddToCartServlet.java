package com.restaurant.controller;

import com.restaurant.dao.MenuItemDAO;
import com.restaurant.models.CartItem;
import com.restaurant.models.MenuItem;
import com.restaurant.utils.ValidationUtils;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/add-to-cart")
public class AddToCartServlet extends HttpServlet {
    private final MenuItemDAO menuItemDAO = new MenuItemDAO();

    @Override
    @SuppressWarnings("unchecked")
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int itemId = ValidationUtils.parsePositiveInt(req.getParameter("itemId"), 0);
        int quantity = ValidationUtils.parsePositiveInt(req.getParameter("quantity"), 1);
        try {
            MenuItem item = menuItemDAO.findById(itemId);
            if (item == null || !item.isAvailable()) {
                resp.sendRedirect(req.getContextPath() + "/menu?error=Item is unavailable");
                return;
            }
            HttpSession session = req.getSession();
            Integer selectedRestaurantId = (Integer) session.getAttribute("selectedRestaurantId");
            if (selectedRestaurantId == null || selectedRestaurantId != item.getRestaurantId()) {
                resp.sendRedirect(req.getContextPath() + "/restaurants?error=Select this restaurant before ordering");
                return;
            }
            Integer cartRestaurantId = (Integer) session.getAttribute("cartRestaurantId");
            List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
            if (cart == null) {
                cart = new ArrayList<>();
                session.setAttribute("cart", cart);
            }
            if (cartRestaurantId != null && cartRestaurantId != item.getRestaurantId() && !cart.isEmpty()) {
                resp.sendRedirect(req.getContextPath() + "/cart?error=Clear your cart before ordering from another restaurant");
                return;
            }
            session.setAttribute("cartRestaurantId", item.getRestaurantId());
            boolean updated = false;
            for (CartItem cartItem : cart) {
                if (cartItem.getMenuItem().getId() == item.getId()) {
                    cartItem.setQuantity(cartItem.getQuantity() + quantity);
                    updated = true;
                    break;
                }
            }
            if (!updated) {
                cart.add(new CartItem(item, quantity));
            }
            resp.sendRedirect(req.getContextPath() + "/cart?success=Item added to cart");
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/menu?error=Could not add item");
        }
    }
}
