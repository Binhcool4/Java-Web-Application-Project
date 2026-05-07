package org.finalproject.config;

import org.finalproject.entity.User;
import jakarta.servlet.http.*;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        String uri = request.getRequestURI();

        if (user == null) {
            response.sendRedirect("/login");
            return false;
        }

        if (uri.startsWith("/admin") && !user.getRole().equals("ADMIN")) {
            response.sendRedirect("/home");
            return false;
        }

        if (uri.startsWith("/staff") && !user.getRole().equals("STAFF")) {
            response.sendRedirect("/home");
            return false;
        }

        return true;
    }
}
