package lk.icbt.dental.model.service;

import lk.icbt.dental.model.entity.User;

public interface AuthService {
    User authenticate(String username, String password) throws Exception;
}
