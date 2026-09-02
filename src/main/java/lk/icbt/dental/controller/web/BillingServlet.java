package lk.icbt.dental.controller.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.icbt.dental.model.exception.ValidationException;
import lk.icbt.dental.model.service.ClinicFacade;

import java.io.IOException;

@WebServlet("/billing")
public class BillingServlet extends HttpServlet {
    private final ClinicFacade clinic = ClinicFacade.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            request.setAttribute("bill", clinic.generateBill(id));
            request.getRequestDispatcher("/WEB-INF/views/billing/bill.jsp").forward(request, response);
        } catch (ValidationException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/billing/bill.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Unable to calculate the bill.");
            request.getRequestDispatcher("/WEB-INF/views/billing/bill.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            clinic.updateBillStatus(id, request.getParameter("status"));
            response.sendRedirect(request.getContextPath() + "/billing?id=" + id + "&updated=1");
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/appointments/");
        }
    }
}
