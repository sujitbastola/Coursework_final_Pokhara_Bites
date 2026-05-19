package com.restaurant.filter;

import com.restaurant.models.User;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = {"/admin/*", "/checkout", "/place-order", "/orders"})
public class AuthFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        User user = (User) req.getSession().getAttribute("user");
        String path = req.getServletPath();

        if (user == null) {
            String redirect = "/place-order".equals(path) ? "/checkout" : path;
            res.sendRedirect(req.getContextPath() + "/login?error=Please+login+to+checkout&redirect=" + redirect);
            return;
        }
        if (path.startsWith("/admin") && !user.isAdmin()) {
            res.sendRedirect(req.getContextPath() + "/restaurants?error=Admins only");
            return;
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}
