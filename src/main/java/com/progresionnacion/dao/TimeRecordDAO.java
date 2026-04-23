package com.progresionnacion.dao;

import com.progresionnacion.DatabaseManager;
import com.progresionnacion.model.RecordFilter;
import com.progresionnacion.model.TimeRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TimeRecordDAO {

    public List<TimeRecord> findAll(RecordFilter filter) {
        List<TimeRecord> records = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
                SELECT tr.id, tr.swimmer_id, tr.record_date, tr.stroke, tr.session_type, tr.time_seconds,
                       tr.location, tr.coach_comment, tr.coach_name,
                       s.first_name || ' ' || s.last_name AS swimmer_name,
                       s.sex, s.category
                FROM time_records tr
                INNER JOIN swimmers s ON tr.swimmer_id = s.id
                WHERE 1=1
                """);

        List<Object> parameters = new ArrayList<>();

        if (filter != null) {
            if (filter.getSwimmerId() != null) {
                sql.append(" AND tr.swimmer_id = ?");
                parameters.add(filter.getSwimmerId());
            }
            if (filter.getSex() != null && !filter.getSex().isBlank()) {
                sql.append(" AND s.sex = ?");
                parameters.add(filter.getSex());
            }
            if (filter.getCategory() != null && !filter.getCategory().isBlank()) {
                sql.append(" AND s.category = ?");
                parameters.add(filter.getCategory());
            }
            if (filter.getStroke() != null && !filter.getStroke().isBlank()) {
                sql.append(" AND tr.stroke = ?");
                parameters.add(filter.getStroke());
            }
            if (filter.getSessionType() != null && !filter.getSessionType().isBlank()) {
                sql.append(" AND tr.session_type = ?");
                parameters.add(filter.getSessionType());
            }
            if (filter.getDateFrom() != null) {
                sql.append(" AND tr.record_date >= ?");
                parameters.add(filter.getDateFrom().toString());
            }
            if (filter.getDateTo() != null) {
                sql.append(" AND tr.record_date <= ?");
                parameters.add(filter.getDateTo().toString());
            }
            if (filter.getSearchText() != null && !filter.getSearchText().isBlank()) {
                sql.append(" AND (s.first_name LIKE ? OR s.last_name LIKE ? OR tr.location LIKE ? OR tr.coach_comment LIKE ?)");
                String likeText = "%" + filter.getSearchText().trim() + "%";
                parameters.add(likeText);
                parameters.add(likeText);
                parameters.add(likeText);
                parameters.add(likeText);
            }
        }

        sql.append(" ORDER BY tr.record_date DESC, swimmer_name");

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql.toString())) {

            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setObject(i + 1, parameters.get(i));
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    records.add(mapRecord(resultSet));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al cargar registros", e);
        }

        return records;
    }

    public boolean insert(TimeRecord record, Integer userId) {
        String sql = """
                INSERT INTO time_records (swimmer_id, record_date, stroke, session_type, time_seconds, location, coach_comment, coach_name, created_by_user_id)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, record.getSwimmerId());
            preparedStatement.setString(2, record.getRecordDate().toString());
            preparedStatement.setString(3, record.getStroke());
            preparedStatement.setString(4, record.getSessionType());
            preparedStatement.setDouble(5, record.getTimeSeconds());
            preparedStatement.setString(6, record.getLocation());
            preparedStatement.setString(7, record.getCoachComment());
            preparedStatement.setString(8, record.getCoachName());

            if (userId == null) {
                preparedStatement.setNull(9, java.sql.Types.INTEGER);
            } else {
                preparedStatement.setInt(9, userId);
            }

            return preparedStatement.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar registro", e);
        }
    }

    public boolean update(TimeRecord record) {
        String sql = """
                UPDATE time_records
                SET swimmer_id = ?, record_date = ?, stroke = ?, session_type = ?, time_seconds = ?,
                    location = ?, coach_comment = ?, coach_name = ?
                WHERE id = ?
                """;

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, record.getSwimmerId());
            preparedStatement.setString(2, record.getRecordDate().toString());
            preparedStatement.setString(3, record.getStroke());
            preparedStatement.setString(4, record.getSessionType());
            preparedStatement.setDouble(5, record.getTimeSeconds());
            preparedStatement.setString(6, record.getLocation());
            preparedStatement.setString(7, record.getCoachComment());
            preparedStatement.setString(8, record.getCoachName());
            preparedStatement.setInt(9, record.getId());

            return preparedStatement.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar registro", e);
        }
    }

    public boolean delete(int recordId) {
        String sql = "DELETE FROM time_records WHERE id = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, recordId);
            return preparedStatement.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar registro", e);
        }
    }

    public List<TimeRecord> findBySwimmerAndStroke(int swimmerId, String stroke) {
        RecordFilter filter = new RecordFilter();
        filter.setSwimmerId(swimmerId);
        filter.setStroke(stroke);

        return findAll(filter);
    }

    public Double getBestTime(int swimmerId, String stroke) {
        String sql = """
                SELECT MIN(time_seconds) AS best_time
                FROM time_records
                WHERE swimmer_id = ? AND stroke = ?
                """;

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, swimmerId);
            preparedStatement.setString(2, stroke);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    double value = resultSet.getDouble("best_time");
                    return resultSet.wasNull() ? null : value;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener mejor marca", e);
        }

        return null;
    }

    public Double getAverageTime(int swimmerId, String stroke) {
        String sql = """
                SELECT AVG(time_seconds) AS avg_time
                FROM time_records
                WHERE swimmer_id = ? AND stroke = ?
                """;

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, swimmerId);
            preparedStatement.setString(2, stroke);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    double value = resultSet.getDouble("avg_time");
                    return resultSet.wasNull() ? null : value;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener promedio", e);
        }

        return null;
    }

    public int countRecords(int swimmerId, String stroke) {
        String sql = """
                SELECT COUNT(*) AS total
                FROM time_records
                WHERE swimmer_id = ? AND stroke = ?
                """;

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, swimmerId);
            preparedStatement.setString(2, stroke);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("total");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al contar registros", e);
        }

        return 0;
    }

    private TimeRecord mapRecord(ResultSet resultSet) throws Exception {
        return new TimeRecord(
                resultSet.getInt("id"),
                resultSet.getInt("swimmer_id"),
                resultSet.getString("swimmer_name"),
                resultSet.getString("sex"),
                resultSet.getString("category"),
                java.time.LocalDate.parse(resultSet.getString("record_date")),
                resultSet.getString("stroke"),
                resultSet.getString("session_type"),
                resultSet.getDouble("time_seconds"),
                resultSet.getString("location"),
                resultSet.getString("coach_comment"),
                resultSet.getString("coach_name")
        );
    }
}
