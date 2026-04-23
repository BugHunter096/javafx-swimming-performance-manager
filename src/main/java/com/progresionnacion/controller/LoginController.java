package com.progresionnacion.controller;

import com.progresionnacion.App;
import com.progresionnacion.model.User;
import com.progresionnacion.service.AuthService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Optional;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label infoLabel;

    private final AuthService authService = new AuthService();

    @FXML
    private void initialize() {
        infoLabel.setText("Usuario inicial de prueba: admin / admin123");
    }

    @FXML
    private void onLogin() {
        String username = usernameField.getText() == null ? "" : usernameField.getText().trim();
        String password = passwordField.getText() == null ? "" : passwordField.getText().trim();

        if (username.isBlank() || password.isBlank()) {
            infoLabel.setText("Debes escribir usuario y contraseña.");
            return;
        }

        Optional<User> userOptional = authService.authenticate(username, password);

        if (userOptional.isEmpty()) {
            infoLabel.setText("Credenciales incorrectas.");
            return;
        }

        try {
            App.showMainView(userOptional.get());
        } catch (IOException e) {
            infoLabel.setText("No se pudo abrir la pantalla principal.");
        }
    }

    @FXML
    private void onExit() {
        App.getPrimaryStage().close();
    }
}
