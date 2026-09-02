package lk.icbt.dental.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lk.icbt.dental.model.entity.User;

public final class SecurityUtil {
    private SecurityUtil() {
    }

    public static User currentUser(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        HttpSession session = request.getSession(false);
        Object user = session == null ? null : session.getAttribute(AppConstants.SESSION_USER);
        return user instanceof User ? (User) user : null;
    }

    public static boolean isManager(User user) {
        return user != null && AppConstants.ROLE_MANAGER.equalsIgnoreCase(user.getRole());
    }
}
