package com.progresionnacion.dao;

import com.progresionnacion.DatabaseManager;
import com.progresionnacion.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAO {

    public Optional<User> findByUsername(String username) {
        String sql = "SELECT id, username, password_hash, full_name, role FROM users WHERE username = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User(
                            resultSet.getInt("id"),
                            resultSet.getString("username"),
                            resultSet.getString("password_hash"),
                            resultSet.getString("full_name"),
                            resultSet.getString("role")
                    );
                    return Optional.of(user);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar el usuario", e);
        }

        return Optional.empty();
    }

    public List<User> findAll() {
        String sql = "SELECT id, username, password_hash, full_name, role FROM users ORDER BY username";
        List<User> users = new ArrayList<>();

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                users.add(new User(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password_hash"),
                        resultSet.getString("full_name"),
                        resultSet.getString("role")
                ));
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al listar usuarios", e);
        }

        return users;
    }

    public boolean insert(User user) {
        String sql = "INSERT INTO users (username, password_hash, full_name, role) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPasswordHash());
            preparedStatement.setString(3, user.getFullName());
            preparedStatement.setString(4, user.getRole());

            return preparedStatement.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error al crear el usuario", e);
        }
    }

    public boolean deleteById(int id) {
        String clearRecordsSql = "UPDATE time_records SET created_by_user_id = NULL WHERE created_by_user_id = ?";
        String deleteUserSql = "DELETE FROM users WHERE id = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement clearStmt = connection.prepareStatement(clearRecordsSql);
             PreparedStatement deleteStmt = connection.prepareStatement(deleteUserSql)) {

            clearStmt.setInt(1, id);
            clearStmt.executeUpdate();

            deleteStmt.setInt(1, id);
            return deleteStmt.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el usuario", e);
        }
    }

    public int countUsers() {
        String sql = "SELECT COUNT(*) FROM users";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            return resultSet.next() ? resultSet.getInt(1) : 0;
        } catch (Exception e) {
            throw new RuntimeException("Error al contar usuarios", e);
        }
    }
}
