package com.restaurant.controller;

import com.restaurant.dao.OrderDAO;
import com.restaurant.models.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet({"/orders", "/admin/orders"})
public class ViewOrdersServlet extends HttpServlet {
    private final OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            if (req.getServletPath().startsWith("/admin")) {
                req.setAttribute("orders", orderDAO.findAll());
                req.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(req, resp);
                return;
            }
            User user = (User) req.getSession().getAttribute("user");
            req.setAttribute("orders", orderDAO.findForUser(user.getId()));
            req.getRequestDispatcher("/WEB-INF/views/user/orders.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("error", "Unable to load orders.");
            req.getRequestDispatcher("/WEB-INF/views/user/orders.jsp").forward(req, resp);
        }
    }
}
