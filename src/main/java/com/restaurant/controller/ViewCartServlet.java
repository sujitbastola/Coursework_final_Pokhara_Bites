package com.restaurant.controller;

import com.restaurant.models.CartItem;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

@WebServlet("/cart")
public class ViewCartServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        attachTotal(req);
        req.getRequestDispatcher("/WEB-INF/views/user/cart.jsp").forward(req, resp);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        String action = req.getParameter("action");
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if ("clear".equals(action)) {
            session.removeAttribute("cart");
            session.removeAttribute("cartRestaurantId");
            resp.sendRedirect(req.getContextPath() + "/restaurants?success=Cart cleared. Choose a restaurant.");
            return;
        }
        if ("remove".equals(action) && cart != null) {
            int itemId = com.restaurant.utils.ValidationUtils.parsePositiveInt(req.getParameter("itemId"), 0);
            for (Iterator<CartItem> it = cart.iterator(); it.hasNext();) {
                if (it.next().getMenuItem().getId() == itemId) {
                    it.remove();
                    break;
                }
            }
            if (cart.isEmpty()) {
                session.removeAttribute("cartRestaurantId");
            }
        }
        resp.sendRedirect(req.getContextPath() + "/cart");
    }

    @SuppressWarnings("unchecked")
    static BigDecimal attachTotal(HttpServletRequest req) {
        List<CartItem> cart = (List<CartItem>) req.getSession().getAttribute("cart");
        BigDecimal total = BigDecimal.ZERO;
        if (cart != null) {
            for (CartItem item : cart) {
                total = total.add(item.getSubtotal());
            }
        }
        req.setAttribute("cartTotal", total);
        return total;
    }
}
