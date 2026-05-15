package com.restaurant.controller;

import com.restaurant.dao.MenuItemDAO;
import com.restaurant.dao.RestaurantDAO;
import com.restaurant.utils.ValidationUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet({"/menu", "/admin/menu-items"})
public class ViewMenuServlet extends HttpServlet {
    private final MenuItemDAO menuItemDAO = new MenuItemDAO();
    private final RestaurantDAO restaurantDAO = new RestaurantDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            if (req.getServletPath().startsWith("/admin")) {
                req.setAttribute("items", menuItemDAO.findAll());
                req.getRequestDispatcher("/WEB-INF/views/admin/menu-items.jsp").forward(req, resp);
                return;
            }
            Integer selectedId = (Integer) req.getSession().getAttribute("selectedRestaurantId");
            int restaurantId = selectedId == null ? ValidationUtils.parsePositiveInt(req.getParameter("restaurantId"), 0) : selectedId;
            if (restaurantId == 0) {
                resp.sendRedirect(req.getContextPath() + "/restaurants?error=Select a restaurant first");
                return;
            }
            String q = ValidationUtils.clean(req.getParameter("q"));
            req.setAttribute("q", q);
            req.setAttribute("restaurant", restaurantDAO.findById(restaurantId));
            req.setAttribute("items", ValidationUtils.isBlank(q) ? menuItemDAO.findByRestaurant(restaurantId) : menuItemDAO.searchByRestaurant(restaurantId, q));
            req.getRequestDispatcher("/WEB-INF/views/user/menu.jsp").forward(req, resp);
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/restaurants?error=Unable to load menu");
        }
    }
}
