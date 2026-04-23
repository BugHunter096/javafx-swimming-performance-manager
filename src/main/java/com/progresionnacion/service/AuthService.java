package com.progresionnacion.service;

import com.progresionnacion.dao.UserDAO;
import com.progresionnacion.model.User;
import com.progresionnacion.util.PasswordUtils;

import java.util.Optional;

public class AuthService {

    private final UserDAO userDAO = new UserDAO();

    public Optional<User> authenticate(String username, String password) {
        Optional<User> userOptional = userDAO.findByUsername(username);

        if (userOptional.isEmpty()) {
            return Optional.empty();
        }

        User user = userOptional.get();
        String inputHash = PasswordUtils.sha256(password);

        if (user.getPasswordHash().equals(inputHash)) {
            return Optional.of(user);
        }

        return Optional.empty();
    }
}
