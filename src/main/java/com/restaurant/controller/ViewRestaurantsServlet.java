package com.restaurant.controller;

import com.restaurant.dao.RestaurantDAO;
import com.restaurant.utils.ValidationUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet({"/restaurants", "/admin/restaurants"})
public class ViewRestaurantsServlet extends HttpServlet {
    private final RestaurantDAO restaurantDAO = new RestaurantDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String q = ValidationUtils.clean(req.getParameter("q"));
            req.setAttribute("q", q);
            req.setAttribute("restaurants", ValidationUtils.isBlank(q) ? restaurantDAO.findAll() : restaurantDAO.search(q));
            if (req.getServletPath().startsWith("/admin")) {
                req.getRequestDispatcher("/WEB-INF/views/admin/restaurants.jsp").forward(req, resp);
            } else {
                req.getRequestDispatcher("/WEB-INF/views/user/restaurants.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            req.setAttribute("error", "Unable to load restaurants.");
            req.getRequestDispatcher("/WEB-INF/views/user/restaurants.jsp").forward(req, resp);
        }
    }
}
