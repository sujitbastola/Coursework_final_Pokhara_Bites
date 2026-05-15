package com.restaurant.controller;

import com.restaurant.dao.MenuItemDAO;
import com.restaurant.dao.RestaurantDAO;
import com.restaurant.models.MenuItem;
import com.restaurant.utils.ValidationUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/admin/menu-items/add")
public class AddMenuItemServlet extends HttpServlet {
    private final MenuItemDAO menuItemDAO = new MenuItemDAO();
    private final RestaurantDAO restaurantDAO = new RestaurantDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        loadRestaurants(req);
        req.getRequestDispatcher("/WEB-INF/views/admin/add-menu-item.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        MenuItem item = read(req);
        if (!valid(item)) {
            req.setAttribute("error", "Restaurant, name, price, category, and availability are required.");
            req.setAttribute("item", item);
            loadRestaurants(req);
            req.getRequestDispatcher("/WEB-INF/views/admin/add-menu-item.jsp").forward(req, resp);
            return;
        }
        try {
            menuItemDAO.add(item);
            resp.sendRedirect(req.getContextPath() + "/admin/menu-items?success=Menu item added");
        } catch (Exception e) {
            req.setAttribute("error", "Could not add menu item.");
            req.setAttribute("item", item);
            loadRestaurants(req);
            req.getRequestDispatcher("/WEB-INF/views/admin/add-menu-item.jsp").forward(req, resp);
        }
    }

    void loadRestaurants(HttpServletRequest req) throws ServletException {
        try {
            req.setAttribute("restaurants", restaurantDAO.findAll());
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    static MenuItem read(HttpServletRequest req) {
        MenuItem item = new MenuItem();
        item.setRestaurantId(ValidationUtils.parsePositiveInt(req.getParameter("restaurantId"), 0));
        item.setName(ValidationUtils.clean(req.getParameter("name")));
        item.setDescription(ValidationUtils.clean(req.getParameter("description")));
        BigDecimal price = ValidationUtils.parseMoney(req.getParameter("price"));
        item.setPrice(price);
        item.setCategory(ValidationUtils.clean(req.getParameter("category")));
        item.setAvailable("true".equals(req.getParameter("availability")));
        return item;
    }

    static boolean valid(MenuItem item) {
        return item.getRestaurantId() > 0
                && !ValidationUtils.isBlank(item.getName())
                && item.getPrice() != null
                && ("veg".equals(item.getCategory()) || "non-veg".equals(item.getCategory()));
    }
}
