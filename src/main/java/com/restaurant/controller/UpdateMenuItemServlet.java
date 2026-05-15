package com.restaurant.controller;

import com.restaurant.dao.MenuItemDAO;
import com.restaurant.models.MenuItem;
import com.restaurant.utils.ValidationUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/menu-items/edit")
public class UpdateMenuItemServlet extends HttpServlet {
    private final MenuItemDAO menuItemDAO = new MenuItemDAO();
    private final AddMenuItemServlet helper = new AddMenuItemServlet();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            MenuItem item = menuItemDAO.findById(ValidationUtils.parsePositiveInt(req.getParameter("id"), 0));
            if (item == null) {
                resp.sendRedirect(req.getContextPath() + "/admin/menu-items?error=Menu item not found");
                return;
            }
            req.setAttribute("item", item);
            helper.loadRestaurants(req);
            req.getRequestDispatcher("/WEB-INF/views/admin/edit-menu-item.jsp").forward(req, resp);
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/admin/menu-items?error=Unable to edit item");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        MenuItem item = AddMenuItemServlet.read(req);
        item.setId(ValidationUtils.parsePositiveInt(req.getParameter("id"), 0));
        if (item.getId() == 0 || !AddMenuItemServlet.valid(item)) {
            req.setAttribute("error", "Please correct the menu item details.");
            req.setAttribute("item", item);
            helper.loadRestaurants(req);
            req.getRequestDispatcher("/WEB-INF/views/admin/edit-menu-item.jsp").forward(req, resp);
            return;
        }
        try {
            menuItemDAO.update(item);
            resp.sendRedirect(req.getContextPath() + "/admin/menu-items?success=Menu item updated");
        } catch (Exception e) {
            req.setAttribute("error", "Could not update menu item.");
            req.setAttribute("item", item);
            helper.loadRestaurants(req);
            req.getRequestDispatcher("/WEB-INF/views/admin/edit-menu-item.jsp").forward(req, resp);
        }
    }
}
