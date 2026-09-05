package lk.icbt.dental.model.dao.impl;

import lk.icbt.dental.model.dao.UserDao;
import lk.icbt.dental.model.entity.User;
import lk.icbt.dental.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDaoImpl implements UserDao {
    @Override
    public User findByUsername(String username) throws Exception {
        String sql = "SELECT user_id, username, password_hash, role FROM users WHERE username = ?";
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            try (ResultSet result = statement.executeQuery()) {
                if (!result.next()) {
                    return null;
                }
                User user = new User();
                user.setUserId(result.getInt("user_id"));
                user.setUsername(result.getString("username"));
                user.setPasswordHash(result.getString("password_hash"));
                user.setRole(result.getString("role"));
                return user;
            }
        }
    }
}
