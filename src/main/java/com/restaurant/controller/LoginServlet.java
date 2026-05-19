package com.restaurant.controller;

import com.restaurant.dao.UserDAO;
import com.restaurant.models.User;
import com.restaurant.utils.PasswordUtils;
import com.restaurant.utils.ValidationUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = ValidationUtils.clean(req.getParameter("email"));
        String password = req.getParameter("password");
        try {
            User user = userDAO.findByEmail(email);
            if (user == null || ValidationUtils.isBlank(password) || !PasswordUtils.verifyPassword(password, user.getPasswordHash())) {
                req.setAttribute("error", "Invalid email or password.");
                req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, resp);
                return;
            }
            HttpSession oldSession = req.getSession();
            Object cart = oldSession.getAttribute("cart");
            Object cartRestaurantId = oldSession.getAttribute("cartRestaurantId");
            Object selectedRestaurantId = oldSession.getAttribute("selectedRestaurantId");
            oldSession.invalidate();

            HttpSession newSession = req.getSession(true);
            newSession.setAttribute("user", user);
            if (cart != null) {
                newSession.setAttribute("cart", cart);
            }
            if (cartRestaurantId != null) {
                newSession.setAttribute("cartRestaurantId", cartRestaurantId);
            }
            if (selectedRestaurantId != null) {
                newSession.setAttribute("selectedRestaurantId", selectedRestaurantId);
            }
            String redirect = ValidationUtils.clean(req.getParameter("redirect"));
            if (!user.isAdmin() && isCustomerRedirect(redirect)) {
                resp.sendRedirect(req.getContextPath() + redirect);
                return;
            }
            resp.sendRedirect(req.getContextPath() + (user.isAdmin() ? "/admin/dashboard" : "/restaurants"));
        } catch (Exception e) {
            req.setAttribute("error", "Login failed. Please try again.");
            req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, resp);
        }
    }

    private boolean isCustomerRedirect(String redirect) {
        return "/checkout".equals(redirect) || "/place-order".equals(redirect) || "/orders".equals(redirect);
    }
}
