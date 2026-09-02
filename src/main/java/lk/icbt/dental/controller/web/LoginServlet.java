package lk.icbt.dental.controller.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lk.icbt.dental.model.entity.User;
import lk.icbt.dental.model.exception.ValidationException;
import lk.icbt.dental.model.service.ServiceFactory;
import lk.icbt.dental.util.AppConstants;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getSession(false) != null
                && request.getSession(false).getAttribute(AppConstants.SESSION_USER) != null) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            User user = ServiceFactory.authService().authenticate(
                    request.getParameter("username"), request.getParameter("password"));
            HttpSession existing = request.getSession(false);
            if (existing != null) {
                existing.invalidate();
            }
            HttpSession session = request.getSession(true);
            session.setAttribute(AppConstants.SESSION_USER, user);
            session.setMaxInactiveInterval(30 * 60);
            response.sendRedirect(request.getContextPath() + "/dashboard");
        } catch (ValidationException e) {
            request.setAttribute("error", e.getMessage());
            request.setAttribute("username", request.getParameter("username"));
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Login failed because the database is unavailable.");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }
}
