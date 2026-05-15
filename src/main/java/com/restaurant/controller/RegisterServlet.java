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
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = ValidationUtils.clean(req.getParameter("name"));
        String email = ValidationUtils.clean(req.getParameter("email")).toLowerCase();
        String password = req.getParameter("password");
        String role = ValidationUtils.clean(req.getParameter("role"));

        if (ValidationUtils.isBlank(name) || !ValidationUtils.isValidEmail(email) || ValidationUtils.isBlank(password) || password.length() < 6) {
            req.setAttribute("error", "Enter a name, valid email, and password with at least 6 characters.");
            req.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(req, resp);
            return;
        }
        if (!"admin".equals(role)) {
            role = "customer";
        }
        try {
            if (userDAO.findByEmail(email) != null) {
                req.setAttribute("error", "Email is already registered.");
                req.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(req, resp);
                return;
            }
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPasswordHash(PasswordUtils.hashPassword(password));
            user.setRole(role);
            userDAO.register(user);
            resp.sendRedirect(req.getContextPath() + "/login?success=Registration complete. Please login.");
        } catch (Exception e) {
            req.setAttribute("error", "Registration failed: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(req, resp);
        }
    }
}
