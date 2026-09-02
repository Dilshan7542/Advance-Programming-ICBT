package lk.icbt.dental.model.service.impl;

import lk.icbt.dental.model.dao.DaoFactory;
import lk.icbt.dental.model.entity.User;
import lk.icbt.dental.model.exception.ValidationException;
import lk.icbt.dental.model.service.AuthService;
import lk.icbt.dental.util.PasswordUtil;

public class AuthServiceImpl implements AuthService {
    @Override
    public User authenticate(String username, String password) throws Exception {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            throw new ValidationException("Username and password are required.");
        }
        User user = DaoFactory.userDao().findByUsername(username.trim());
        if (user == null || !PasswordUtil.matches(password, user.getPasswordHash())) {
            throw new ValidationException("Invalid username or password.");
        }
        user.setPasswordHash(null);
        return user;
    }
}
