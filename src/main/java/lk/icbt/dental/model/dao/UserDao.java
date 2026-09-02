package lk.icbt.dental.model.dao;

import lk.icbt.dental.model.entity.User;

public interface UserDao {
    User findByUsername(String username) throws Exception;
}
