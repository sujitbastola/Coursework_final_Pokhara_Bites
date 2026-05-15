package com.restaurant.controller;

import com.restaurant.dao.MenuItemDAO;
import com.restaurant.utils.ValidationUtils;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/menu-items/delete")
public class DeleteMenuItemServlet extends HttpServlet {
    private final MenuItemDAO menuItemDAO = new MenuItemDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            menuItemDAO.delete(ValidationUtils.parsePositiveInt(req.getParameter("id"), 0));
            resp.sendRedirect(req.getContextPath() + "/admin/menu-items?success=Menu item deleted");
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/admin/menu-items?error=Delete failed. Item may be part of an order.");
        }
    }
}
