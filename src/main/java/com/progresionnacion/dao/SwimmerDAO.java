package com.progresionnacion.dao;

import com.progresionnacion.DatabaseManager;
import com.progresionnacion.model.Swimmer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SwimmerDAO {

    public List<Swimmer> findAll(String searchText, String sex, String category) {
        List<Swimmer> swimmers = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
                SELECT id, first_name, last_name, birth_date, sex, category, club, notes
                FROM swimmers
                WHERE 1=1
                """);

        List<Object> parameters = new ArrayList<>();

        if (searchText != null && !searchText.isBlank()) {
            sql.append(" AND (first_name LIKE ? OR last_name LIKE ? OR club LIKE ?)");
            String likeText = "%" + searchText.trim() + "%";
            parameters.add(likeText);
            parameters.add(likeText);
            parameters.add(likeText);
        }

        if (sex != null && !sex.isBlank()) {
            sql.append(" AND sex = ?");
            parameters.add(sex);
        }

        if (category != null && !category.isBlank()) {
            sql.append(" AND category = ?");
            parameters.add(category);
        }

        sql.append(" ORDER BY last_name, first_name");

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql.toString())) {

            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setObject(i + 1, parameters.get(i));
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Swimmer swimmer = new Swimmer(
                            resultSet.getInt("id"),
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name"),
                            java.time.LocalDate.parse(resultSet.getString("birth_date")),
                            resultSet.getString("sex"),
                            resultSet.getString("category"),
                            resultSet.getString("club"),
                            resultSet.getString("notes")
                    );
                    swimmers.add(swimmer);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al cargar nadadores", e);
        }

        return swimmers;
    }

    public boolean insert(Swimmer swimmer) {
        String sql = """
                INSERT INTO swimmers (first_name, last_name, birth_date, sex, category, club, notes)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, swimmer.getFirstName());
            preparedStatement.setString(2, swimmer.getLastName());
            preparedStatement.setString(3, swimmer.getBirthDate().toString());
            preparedStatement.setString(4, swimmer.getSex());
            preparedStatement.setString(5, swimmer.getCategory());
            preparedStatement.setString(6, swimmer.getClub());
            preparedStatement.setString(7, swimmer.getNotes());

            return preparedStatement.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar nadador", e);
        }
    }

    public boolean update(Swimmer swimmer) {
        String sql = """
                UPDATE swimmers
                SET first_name = ?, last_name = ?, birth_date = ?, sex = ?, category = ?, club = ?, notes = ?
                WHERE id = ?
                """;

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, swimmer.getFirstName());
            preparedStatement.setString(2, swimmer.getLastName());
            preparedStatement.setString(3, swimmer.getBirthDate().toString());
            preparedStatement.setString(4, swimmer.getSex());
            preparedStatement.setString(5, swimmer.getCategory());
            preparedStatement.setString(6, swimmer.getClub());
            preparedStatement.setString(7, swimmer.getNotes());
            preparedStatement.setInt(8, swimmer.getId());

            return preparedStatement.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar nadador", e);
        }
    }

    public boolean delete(int swimmerId) {
        String sql = "DELETE FROM swimmers WHERE id = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, swimmerId);
            return preparedStatement.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar nadador", e);
        }
    }
}
