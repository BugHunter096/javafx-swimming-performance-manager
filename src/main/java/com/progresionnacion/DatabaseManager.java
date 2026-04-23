package com.progresionnacion;

import com.progresionnacion.util.PasswordUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private static final String DB_FOLDER = "data";
    private static final String DB_FILE = "progresion_natacion.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_FOLDER + "/" + DB_FILE;

    private DatabaseManager() {
    }

    public static Connection getConnection() throws SQLException {
        Connection connection = java.sql.DriverManager.getConnection(DB_URL);
        try (Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON");
        }
        return connection;
    }

    public static void initializeDatabase() {
        createDataFolder();

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute("""
                    CREATE TABLE IF NOT EXISTS users (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        username TEXT NOT NULL UNIQUE,
                        password_hash TEXT NOT NULL,
                        full_name TEXT NOT NULL,
                        role TEXT NOT NULL
                    )
                    """);

            statement.execute("""
                    CREATE TABLE IF NOT EXISTS swimmers (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        first_name TEXT NOT NULL,
                        last_name TEXT NOT NULL,
                        birth_date TEXT NOT NULL,
                        sex TEXT NOT NULL,
                        category TEXT NOT NULL,
                        club TEXT,
                        notes TEXT
                    )
                    """);

            statement.execute("""
                    CREATE TABLE IF NOT EXISTS time_records (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        swimmer_id INTEGER NOT NULL,
                        record_date TEXT NOT NULL,
                        stroke TEXT NOT NULL,
                        session_type TEXT NOT NULL,
                        time_seconds REAL NOT NULL,
                        location TEXT,
                        coach_comment TEXT,
                        coach_name TEXT,
                        created_by_user_id INTEGER,
                        FOREIGN KEY (swimmer_id) REFERENCES swimmers(id) ON DELETE CASCADE,
                        FOREIGN KEY (created_by_user_id) REFERENCES users(id)
                    )
                    """);

            int exampleUserId = ensureExampleUser(connection);

            if (countRows(statement, "swimmers") == 0 && countRows(statement, "time_records") == 0) {
                seedExampleData(connection, exampleUserId);
            }

        } catch (SQLException e) {
            throw new RuntimeException("No se pudo inicializar la base de datos", e);
        }
    }

    private static int ensureExampleUser(Connection connection) throws SQLException {
        Integer adminId = findUserIdByUsername(connection, "admin");
        if (adminId != null) {
            return adminId;
        }

        Integer anyUserId = findAnyUserId(connection);
        if (anyUserId != null) {
            return anyUserId;
        }

        String passwordHash = PasswordUtils.sha256("admin123");
        String sql = "INSERT INTO users (username, password_hash, full_name, role) VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, "admin");
            preparedStatement.setString(2, passwordHash);
            preparedStatement.setString(3, "Administrador inicial");
            preparedStatement.setString(4, "ADMIN");
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        }

        Integer createdAdminId = findUserIdByUsername(connection, "admin");
        if (createdAdminId != null) {
            return createdAdminId;
        }

        throw new SQLException("No se pudo crear ni recuperar el usuario de ejemplo");
    }

    private static Integer findUserIdByUsername(Connection connection, String username) throws SQLException {
        String sql = "SELECT id FROM users WHERE username = ? LIMIT 1";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next() ? resultSet.getInt("id") : null;
            }
        }
    }

    private static Integer findAnyUserId(Connection connection) throws SQLException {
        String sql = "SELECT id FROM users ORDER BY id LIMIT 1";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            return resultSet.next() ? resultSet.getInt("id") : null;
        }
    }

    private static int countRows(Statement statement, String tableName) throws SQLException {
        try (ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM " + tableName)) {
            return resultSet.next() ? resultSet.getInt(1) : 0;
        }
    }

    private static void seedExampleData(Connection connection, int createdByUserId) throws SQLException {
        int luciaId = insertSwimmer(connection, "Lucía", "Gómez", "2010-05-14", "Femenino", "Infantil", "Club Delfín", "Especialista en crol");
        int marioId = insertSwimmer(connection, "Mario", "Santos", "2008-09-03", "Masculino", "Junior", "Club Delfín", "Buen final en mariposa");
        int claraId = insertSwimmer(connection, "Clara", "Ruiz", "2012-01-21", "Femenino", "Alevín", "Club Delfín", "Debe mejorar la salida");
        int pabloId = insertSwimmer(connection, "Pablo", "Martín", "2006-11-10", "Masculino", "Absoluto", "Club Delfín", "Constante en entrenamientos");

        insertTimeRecord(connection, luciaId, "2026-03-01", "Crol", "Entrenamiento", 68.40, "Piscina municipal", "Buen ritmo en la parte central", "Administrador inicial", createdByUserId);
        insertTimeRecord(connection, luciaId, "2026-03-08", "Crol", "Entrenamiento", 67.95, "Piscina municipal", "Mejora de técnica en viraje", "Administrador inicial", createdByUserId);
        insertTimeRecord(connection, luciaId, "2026-03-15", "Crol", "Competición", 67.10, "Centro acuático", "Marca muy sólida", "Administrador inicial", createdByUserId);
        insertTimeRecord(connection, marioId, "2026-03-04", "Mariposa", "Entrenamiento", 72.85, "Piscina municipal", "Falta mantener frecuencia al final", "Administrador inicial", createdByUserId);
        insertTimeRecord(connection, marioId, "2026-03-18", "Mariposa", "Competición", 71.90, "Centro acuático", "Buena salida", "Administrador inicial", createdByUserId);
        insertTimeRecord(connection, claraId, "2026-03-06", "Espalda", "Entrenamiento", 81.55, "Piscina municipal", "Corregir entrada de mano", "Administrador inicial", createdByUserId);
        insertTimeRecord(connection, pabloId, "2026-03-20", "Braza", "Entrenamiento", 75.35, "Piscina municipal", "Muy regular", "Administrador inicial", createdByUserId);
    }

    private static int insertSwimmer(Connection connection,
                                     String firstName,
                                     String lastName,
                                     String birthDate,
                                     String sex,
                                     String category,
                                     String club,
                                     String notes) throws SQLException {
        String sql = """
                INSERT INTO swimmers (first_name, last_name, birth_date, sex, category, club, notes)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, birthDate);
            preparedStatement.setString(4, sex);
            preparedStatement.setString(5, category);
            preparedStatement.setString(6, club);
            preparedStatement.setString(7, notes);
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        }

        throw new SQLException("No se pudo obtener el ID del nadador insertado");
    }

    private static void insertTimeRecord(Connection connection,
                                         int swimmerId,
                                         String recordDate,
                                         String stroke,
                                         String sessionType,
                                         double timeSeconds,
                                         String location,
                                         String coachComment,
                                         String coachName,
                                         int createdByUserId) throws SQLException {
        String sql = """
                INSERT INTO time_records (swimmer_id, record_date, stroke, session_type, time_seconds, location, coach_comment, coach_name, created_by_user_id)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, swimmerId);
            preparedStatement.setString(2, recordDate);
            preparedStatement.setString(3, stroke);
            preparedStatement.setString(4, sessionType);
            preparedStatement.setDouble(5, timeSeconds);
            preparedStatement.setString(6, location);
            preparedStatement.setString(7, coachComment);
            preparedStatement.setString(8, coachName);
            preparedStatement.setInt(9, createdByUserId);
            preparedStatement.executeUpdate();
        }
    }

    private static void createDataFolder() {
        try {
            Files.createDirectories(Path.of(DB_FOLDER));
        } catch (Exception e) {
            throw new RuntimeException("No se pudo crear la carpeta de datos", e);
        }
    }
}
