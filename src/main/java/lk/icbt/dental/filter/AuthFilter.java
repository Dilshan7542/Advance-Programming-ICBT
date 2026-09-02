package lk.icbt.dental.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.icbt.dental.util.AppConstants;

import java.io.IOException;

@WebFilter(urlPatterns = {
        "/dashboard",
        "/appointments/*",
        "/billing",
        "/billing/*",
        "/help"
})
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        Object user = httpRequest.getSession(false) == null
                ? null
                : httpRequest.getSession(false).getAttribute(AppConstants.SESSION_USER);

        if (user == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
            return;
        }
        chain.doFilter(request, response);
    }
}
