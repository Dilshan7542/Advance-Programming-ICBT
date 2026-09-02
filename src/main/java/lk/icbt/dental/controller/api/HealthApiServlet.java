package lk.icbt.dental.controller.api;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.icbt.dental.util.AppConfig;
import lk.icbt.dental.util.JsonUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet("/api/health")
public class HealthApiServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("status", "OK");
        payload.put("application", AppConfig.getOrDefault("app.name", "Sunrise Dental Clinic"));
        payload.put("time", LocalDateTime.now().toString());
        response.getWriter().write(JsonUtil.toJson(payload));
    }
}
