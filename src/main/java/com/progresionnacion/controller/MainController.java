package com.progresionnacion.controller;

import com.progresionnacion.App;
import com.progresionnacion.dao.SwimmerDAO;
import com.progresionnacion.dao.TimeRecordDAO;
import com.progresionnacion.dao.UserDAO;
import com.progresionnacion.model.RecordFilter;
import com.progresionnacion.model.Swimmer;
import com.progresionnacion.model.TimeRecord;
import com.progresionnacion.model.User;
import com.progresionnacion.util.AlertUtils;
import com.progresionnacion.util.PasswordUtils;
import com.progresionnacion.util.TimeUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public class MainController {

    private final SwimmerDAO swimmerDAO = new SwimmerDAO();
    private final TimeRecordDAO timeRecordDAO = new TimeRecordDAO();
    private final UserDAO userDAO = new UserDAO();

    private final ObservableList<Swimmer> swimmers = FXCollections.observableArrayList();
    private final ObservableList<TimeRecord> records = FXCollections.observableArrayList();
    private final ObservableList<User> users = FXCollections.observableArrayList();

    private User loggedUser;
    private Swimmer selectedSwimmer;
    private TimeRecord selectedRecord;
    private User selectedUser;

    @FXML
    private Label loggedUserLabel;

    @FXML
    private TableView<Swimmer> swimmerTable;
    @FXML
    private TableColumn<Swimmer, Integer> swimmerIdColumn;
    @FXML
    private TableColumn<Swimmer, String> swimmerNameColumn;
    @FXML
    private TableColumn<Swimmer, String> swimmerSexColumn;
    @FXML
    private TableColumn<Swimmer, String> swimmerCategoryColumn;
    @FXML
    private TableColumn<Swimmer, LocalDate> swimmerBirthDateColumn;
    @FXML
    private TableColumn<Swimmer, String> swimmerClubColumn;

    @FXML
    private TextField swimmerSearchField;
    @FXML
    private ComboBox<String> swimmerFilterSexCombo;
    @FXML
    private ComboBox<String> swimmerFilterCategoryCombo;

    @FXML
    private TextField swimmerFirstNameField;
    @FXML
    private TextField swimmerLastNameField;
    @FXML
    private DatePicker swimmerBirthDatePicker;
    @FXML
    private ComboBox<String> swimmerSexCombo;
    @FXML
    private ComboBox<String> swimmerCategoryCombo;
    @FXML
    private TextField swimmerClubField;
    @FXML
    private TextArea swimmerNotesArea;
    @FXML
    private Label swimmerFormLabel;

    @FXML
    private TableView<TimeRecord> recordTable;
    @FXML
    private TableColumn<TimeRecord, LocalDate> recordDateColumn;
    @FXML
    private TableColumn<TimeRecord, String> recordSwimmerColumn;
    @FXML
    private TableColumn<TimeRecord, String> recordStrokeColumn;
    @FXML
    private TableColumn<TimeRecord, String> recordTypeColumn;
    @FXML
    private TableColumn<TimeRecord, Double> recordTimeColumn;
    @FXML
    private TableColumn<TimeRecord, String> recordLocationColumn;
    @FXML
    private TableColumn<TimeRecord, String> recordCoachColumn;
    @FXML
    private TableColumn<TimeRecord, String> recordCommentColumn;

    @FXML
    private TextField recordSearchField;
    @FXML
    private ComboBox<Swimmer> recordFilterSwimmerCombo;
    @FXML
    private ComboBox<String> recordFilterSexCombo;
    @FXML
    private ComboBox<String> recordFilterCategoryCombo;
    @FXML
    private ComboBox<String> recordFilterStrokeCombo;
    @FXML
    private ComboBox<String> recordFilterTypeCombo;
    @FXML
    private DatePicker recordDateFromPicker;
    @FXML
    private DatePicker recordDateToPicker;

    @FXML
    private ComboBox<Swimmer> recordSwimmerCombo;
    @FXML
    private DatePicker recordDatePicker;
    @FXML
    private ComboBox<String> recordStrokeCombo;
    @FXML
    private ComboBox<String> recordTypeCombo;
    @FXML
    private TextField recordTimeField;
    @FXML
    private TextField recordLocationField;
    @FXML
    private TextField recordCoachField;
    @FXML
    private TextArea recordCommentArea;
    @FXML
    private Label recordFormLabel;

    @FXML
    private ComboBox<Swimmer> statsSwimmerCombo;
    @FXML
    private ComboBox<String> statsStrokeCombo;
    @FXML
    private LineChart<String, Number> progressChart;
    @FXML
    private Label bestTimeLabel;
    @FXML
    private Label averageTimeLabel;
    @FXML
    private Label totalRecordsLabel;

    @FXML
    private Tab usersTab;
    @FXML
    private TableView<User> userTable;
    @FXML
    private TableColumn<User, Integer> userIdColumn;
    @FXML
    private TableColumn<User, String> userUsernameColumn;
    @FXML
    private TableColumn<User, String> userFullNameColumn;
    @FXML
    private TableColumn<User, String> userRoleColumn;
    @FXML
    private TextField userUsernameField;
    @FXML
    private TextField userFullNameField;
    @FXML
    private ComboBox<String> userRoleCombo;
    @FXML
    private PasswordField userPasswordField;
    @FXML
    private PasswordField userConfirmPasswordField;
    @FXML
    private Label userFormLabel;

    @FXML
    private void initialize() {
        configureStaticCombos();
        configureTables();
        configureListeners();
        clearSwimmerForm();
        clearRecordForm();
        clearUserForm();
        loadSwimmers();
        loadRecords();
        loadUsers();
        refreshSwimmerCombos();
        refreshChart();
    }

    public void setLoggedUser(User user) {
        this.loggedUser = user;
        loggedUserLabel.setText("Sesión: " + user.getFullName() + " (" + user.getRole() + ")");
        if (recordCoachField != null && (recordCoachField.getText() == null || recordCoachField.getText().isBlank())) {
            recordCoachField.setText(user.getFullName());
        }
        configureUserManagementAccess();
    }

    private void configureStaticCombos() {
        ObservableList<String> sexes = FXCollections.observableArrayList("", "Femenino", "Masculino");
        ObservableList<String> categories = FXCollections.observableArrayList("", "Benjamín", "Alevín", "Infantil", "Junior", "Absoluto", "Máster");
        ObservableList<String> strokes = FXCollections.observableArrayList("", "Mariposa", "Espalda", "Braza", "Crol");
        ObservableList<String> sessionTypes = FXCollections.observableArrayList("", "Entrenamiento", "Competición");

        swimmerFilterSexCombo.setItems(sexes);
        swimmerFilterCategoryCombo.setItems(categories);
        swimmerSexCombo.setItems(FXCollections.observableArrayList("Femenino", "Masculino"));
        swimmerCategoryCombo.setItems(FXCollections.observableArrayList("Benjamín", "Alevín", "Infantil", "Junior", "Absoluto", "Máster"));

        recordFilterSexCombo.setItems(sexes);
        recordFilterCategoryCombo.setItems(categories);
        recordFilterStrokeCombo.setItems(strokes);
        recordFilterTypeCombo.setItems(sessionTypes);

        recordStrokeCombo.setItems(FXCollections.observableArrayList("Mariposa", "Espalda", "Braza", "Crol"));
        recordTypeCombo.setItems(FXCollections.observableArrayList("Entrenamiento", "Competición"));

        statsStrokeCombo.setItems(FXCollections.observableArrayList("Mariposa", "Espalda", "Braza", "Crol"));
        userRoleCombo.setItems(FXCollections.observableArrayList("ADMIN", "ENTRENADOR"));

        swimmerFilterSexCombo.getSelectionModel().selectFirst();
        swimmerFilterCategoryCombo.getSelectionModel().selectFirst();
        recordFilterSexCombo.getSelectionModel().selectFirst();
        recordFilterCategoryCombo.getSelectionModel().selectFirst();
        recordFilterStrokeCombo.getSelectionModel().selectFirst();
        recordFilterTypeCombo.getSelectionModel().selectFirst();
        statsStrokeCombo.getSelectionModel().select("Crol");
    }

    private void configureTables() {
        swimmerIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        swimmerNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        swimmerSexColumn.setCellValueFactory(new PropertyValueFactory<>("sex"));
        swimmerCategoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        swimmerBirthDateColumn.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        swimmerClubColumn.setCellValueFactory(new PropertyValueFactory<>("club"));

        recordDateColumn.setCellValueFactory(new PropertyValueFactory<>("recordDate"));
        recordSwimmerColumn.setCellValueFactory(new PropertyValueFactory<>("swimmerName"));
        recordStrokeColumn.setCellValueFactory(new PropertyValueFactory<>("stroke"));
        recordTypeColumn.setCellValueFactory(new PropertyValueFactory<>("sessionType"));
        recordLocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        recordCoachColumn.setCellValueFactory(new PropertyValueFactory<>("coachName"));
        recordCommentColumn.setCellValueFactory(new PropertyValueFactory<>("coachComment"));

        recordTimeColumn.setCellValueFactory(new PropertyValueFactory<>("timeSeconds"));
        recordTimeColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty);
                setText(empty || value == null ? "" : TimeUtils.formatSeconds(value));
            }
        });

        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        userUsernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        userFullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        userRoleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        swimmerTable.setItems(swimmers);
        recordTable.setItems(records);
        userTable.setItems(users);
    }

    private void configureListeners() {
        swimmerTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedSwimmer = newValue;
            if (newValue != null) {
                fillSwimmerForm(newValue);
            }
        });

        recordTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedRecord = newValue;
            if (newValue != null) {
                fillRecordForm(newValue);
            }
        });

        userTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedUser = newValue;
            if (newValue == null) {
                userFormLabel.setText("Nuevo usuario");
            } else {
                userFormLabel.setText("Usuario seleccionado: " + newValue.getUsername());
            }
        });

        statsSwimmerCombo.valueProperty().addListener((observable, oldValue, newValue) -> refreshChart());
        statsStrokeCombo.valueProperty().addListener((observable, oldValue, newValue) -> refreshChart());
    }

    private void configureUserManagementAccess() {
        if (usersTab == null) {
            return;
        }
        usersTab.setDisable(!isAdminUser());
    }

    private boolean isAdminUser() {
        return loggedUser != null && "ADMIN".equalsIgnoreCase(loggedUser.getRole());
    }

    private void fillSwimmerForm(Swimmer swimmer) {
        swimmerFormLabel.setText("Editando nadador ID: " + swimmer.getId());
        swimmerFirstNameField.setText(swimmer.getFirstName());
        swimmerLastNameField.setText(swimmer.getLastName());
        swimmerBirthDatePicker.setValue(swimmer.getBirthDate());
        swimmerSexCombo.setValue(swimmer.getSex());
        swimmerCategoryCombo.setValue(swimmer.getCategory());
        swimmerClubField.setText(valueOrEmpty(swimmer.getClub()));
        swimmerNotesArea.setText(valueOrEmpty(swimmer.getNotes()));
    }

    private void fillRecordForm(TimeRecord record) {
        recordFormLabel.setText("Editando registro ID: " + record.getId());
        selectSwimmerInCombo(recordSwimmerCombo, record.getSwimmerId());
        recordDatePicker.setValue(record.getRecordDate());
        recordStrokeCombo.setValue(record.getStroke());
        recordTypeCombo.setValue(record.getSessionType());
        recordTimeField.setText(TimeUtils.formatSeconds(record.getTimeSeconds()));
        recordLocationField.setText(valueOrEmpty(record.getLocation()));
        recordCoachField.setText(valueOrEmpty(record.getCoachName()));
        recordCommentArea.setText(valueOrEmpty(record.getCoachComment()));
    }

    private void selectSwimmerInCombo(ComboBox<Swimmer> comboBox, int swimmerId) {
        for (Swimmer swimmer : comboBox.getItems()) {
            if (swimmer.getId() == swimmerId) {
                comboBox.setValue(swimmer);
                return;
            }
        }
        comboBox.setValue(null);
    }

    private void loadSwimmers() {
        List<Swimmer> list = swimmerDAO.findAll(
                swimmerSearchField.getText(),
                emptyToNull(swimmerFilterSexCombo.getValue()),
                emptyToNull(swimmerFilterCategoryCombo.getValue())
        );
        swimmers.setAll(list);
        refreshSwimmerCombos();
    }

    private void loadRecords() {
        RecordFilter filter = new RecordFilter();
        Swimmer filterSwimmer = recordFilterSwimmerCombo.getValue();

        if (filterSwimmer != null) {
            filter.setSwimmerId(filterSwimmer.getId());
        }

        filter.setSex(emptyToNull(recordFilterSexCombo.getValue()));
        filter.setCategory(emptyToNull(recordFilterCategoryCombo.getValue()));
        filter.setStroke(emptyToNull(recordFilterStrokeCombo.getValue()));
        filter.setSessionType(emptyToNull(recordFilterTypeCombo.getValue()));
        filter.setDateFrom(recordDateFromPicker.getValue());
        filter.setDateTo(recordDateToPicker.getValue());
        filter.setSearchText(recordSearchField.getText());

        records.setAll(timeRecordDAO.findAll(filter));
    }

    private void loadUsers() {
        users.setAll(userDAO.findAll());
    }

    private void refreshSwimmerCombos() {
        List<Swimmer> swimmerList = swimmerDAO.findAll(null, null, null)
                .stream()
                .sorted(Comparator.comparing(Swimmer::getLastName).thenComparing(Swimmer::getFirstName))
                .toList();

        ObservableList<Swimmer> swimmerObservableList = FXCollections.observableArrayList(swimmerList);

        Swimmer currentRecordSelection = recordSwimmerCombo.getValue();
        Swimmer currentFilterSelection = recordFilterSwimmerCombo.getValue();
        Swimmer currentStatsSelection = statsSwimmerCombo.getValue();

        recordSwimmerCombo.setItems(swimmerObservableList);
        recordFilterSwimmerCombo.setItems(swimmerObservableList);
        statsSwimmerCombo.setItems(swimmerObservableList);

        restoreComboSelection(recordSwimmerCombo, currentRecordSelection);
        restoreComboSelection(recordFilterSwimmerCombo, currentFilterSelection);
        restoreComboSelection(statsSwimmerCombo, currentStatsSelection);

        if (statsSwimmerCombo.getValue() == null && !swimmerObservableList.isEmpty()) {
            statsSwimmerCombo.setValue(swimmerObservableList.get(0));
        }
    }

    private void restoreComboSelection(ComboBox<Swimmer> comboBox, Swimmer previousSelection) {
        if (previousSelection == null) {
            return;
        }
        selectSwimmerInCombo(comboBox, previousSelection.getId());
    }

    @FXML
    private void onSearchSwimmers() {
        loadSwimmers();
    }

    @FXML
    private void onClearSwimmerFilters() {
        swimmerSearchField.clear();
        swimmerFilterSexCombo.getSelectionModel().selectFirst();
        swimmerFilterCategoryCombo.getSelectionModel().selectFirst();
        loadSwimmers();
    }

    @FXML
    private void onNewSwimmer() {
        clearSwimmerForm();
    }

    @FXML
    private void onSaveSwimmer() {
        try {
            validateSwimmerForm();

            Swimmer swimmer = new Swimmer();
            if (selectedSwimmer != null) {
                swimmer.setId(selectedSwimmer.getId());
            }

            swimmer.setFirstName(swimmerFirstNameField.getText().trim());
            swimmer.setLastName(swimmerLastNameField.getText().trim());
            swimmer.setBirthDate(swimmerBirthDatePicker.getValue());
            swimmer.setSex(swimmerSexCombo.getValue());
            swimmer.setCategory(swimmerCategoryCombo.getValue());
            swimmer.setClub(nullIfBlank(swimmerClubField.getText()));
            swimmer.setNotes(nullIfBlank(swimmerNotesArea.getText()));

            boolean success;
            if (selectedSwimmer == null) {
                success = swimmerDAO.insert(swimmer);
                if (success) {
                    AlertUtils.showInfo("Nadador", "Nadador guardado correctamente.");
                }
            } else {
                success = swimmerDAO.update(swimmer);
                if (success) {
                    AlertUtils.showInfo("Nadador", "Nadador actualizado correctamente.");
                }
            }

            if (success) {
                clearSwimmerForm();
                loadSwimmers();
                loadRecords();
                refreshChart();
            }
        } catch (Exception e) {
            AlertUtils.showError("Error", e.getMessage());
        }
    }

    @FXML
    private void onDeleteSwimmer() {
        if (selectedSwimmer == null) {
            AlertUtils.showError("Error", "Selecciona un nadador para eliminar.");
            return;
        }

        boolean confirm = AlertUtils.confirm(
                "Eliminar nadador",
                "Se eliminará el nadador y sus registros asociados. ¿Quieres continuar?"
        );

        if (!confirm) {
            return;
        }

        boolean deleted = swimmerDAO.delete(selectedSwimmer.getId());
        if (deleted) {
            AlertUtils.showInfo("Nadador", "Nadador eliminado correctamente.");
            clearSwimmerForm();
            loadSwimmers();
            loadRecords();
            refreshChart();
        }
    }

    @FXML
    private void onSearchRecords() {
        loadRecords();
    }

    @FXML
    private void onClearRecordFilters() {
        recordSearchField.clear();
        recordFilterSwimmerCombo.setValue(null);
        recordFilterSexCombo.getSelectionModel().selectFirst();
        recordFilterCategoryCombo.getSelectionModel().selectFirst();
        recordFilterStrokeCombo.getSelectionModel().selectFirst();
        recordFilterTypeCombo.getSelectionModel().selectFirst();
        recordDateFromPicker.setValue(null);
        recordDateToPicker.setValue(null);
        loadRecords();
    }

    @FXML
    private void onNewRecord() {
        clearRecordForm();
    }

    @FXML
    private void onSaveRecord() {
        try {
            validateRecordForm();

            Swimmer swimmer = recordSwimmerCombo.getValue();
            TimeRecord record = new TimeRecord();

            if (selectedRecord != null) {
                record.setId(selectedRecord.getId());
            }

            record.setSwimmerId(swimmer.getId());
            record.setRecordDate(recordDatePicker.getValue());
            record.setStroke(recordStrokeCombo.getValue());
            record.setSessionType(recordTypeCombo.getValue());
            record.setTimeSeconds(TimeUtils.parseToSeconds(recordTimeField.getText()));
            record.setLocation(nullIfBlank(recordLocationField.getText()));
            record.setCoachName(nullIfBlank(recordCoachField.getText()));
            record.setCoachComment(nullIfBlank(recordCommentArea.getText()));

            boolean success;
            if (selectedRecord == null) {
                success = timeRecordDAO.insert(record, loggedUser == null ? null : loggedUser.getId());
                if (success) {
                    AlertUtils.showInfo("Registro", "Registro guardado correctamente.");
                }
            } else {
                success = timeRecordDAO.update(record);
                if (success) {
                    AlertUtils.showInfo("Registro", "Registro actualizado correctamente.");
                }
            }

            if (success) {
                clearRecordForm();
                loadRecords();
                refreshChart();
            }
        } catch (Exception e) {
            AlertUtils.showError("Error", "No se pudo guardar el registro: " + e.getMessage());
        }
    }

    @FXML
    private void onDeleteRecord() {
        if (selectedRecord == null) {
            AlertUtils.showError("Error", "Selecciona un registro para eliminar.");
            return;
        }

        boolean confirm = AlertUtils.confirm("Eliminar registro", "¿Seguro que quieres eliminar el registro seleccionado?");
        if (!confirm) {
            return;
        }

        boolean deleted = timeRecordDAO.delete(selectedRecord.getId());
        if (deleted) {
            AlertUtils.showInfo("Registro", "Registro eliminado correctamente.");
            clearRecordForm();
            loadRecords();
            refreshChart();
        }
    }

    @FXML
    private void onNewUser() {
        clearUserForm();
    }

    @FXML
    private void onSaveUser() {
        if (!isAdminUser()) {
            AlertUtils.showError("Usuarios", "Solo un administrador puede crear usuarios.");
            return;
        }

        try {
            validateUserForm();

            String username = userUsernameField.getText().trim();
            if (userDAO.findByUsername(username).isPresent()) {
                throw new IllegalArgumentException("Ya existe un usuario con ese nombre.");
            }

            User user = new User();
            user.setUsername(username);
            user.setFullName(userFullNameField.getText().trim());
            user.setRole(userRoleCombo.getValue());
            user.setPasswordHash(PasswordUtils.sha256(userPasswordField.getText()));

            boolean success = userDAO.insert(user);
            if (success) {
                AlertUtils.showInfo("Usuarios", "Usuario creado correctamente.");
                clearUserForm();
                loadUsers();
            }
        } catch (Exception e) {
            AlertUtils.showError("Error", e.getMessage());
        }
    }

    @FXML
    private void onDeleteUser() {
        if (!isAdminUser()) {
            AlertUtils.showError("Usuarios", "Solo un administrador puede eliminar usuarios.");
            return;
        }

        if (selectedUser == null) {
            AlertUtils.showError("Usuarios", "Selecciona un usuario en la tabla.");
            return;
        }

        if (loggedUser != null && selectedUser.getId() == loggedUser.getId()) {
            AlertUtils.showError("Usuarios", "No puedes eliminar el usuario con el que has iniciado sesión.");
            return;
        }

        if (userDAO.countUsers() <= 1) {
            AlertUtils.showError("Usuarios", "Debe quedar al menos un usuario en la aplicación.");
            return;
        }

        boolean confirm = AlertUtils.confirm(
                "Eliminar usuario",
                "Se eliminará el usuario seleccionado. Los registros creados por ese usuario conservarán los datos, pero quedarán sin autor asociado. ¿Quieres continuar?"
        );

        if (!confirm) {
            return;
        }

        boolean deleted = userDAO.deleteById(selectedUser.getId());
        if (deleted) {
            AlertUtils.showInfo("Usuarios", "Usuario eliminado correctamente.");
            clearUserForm();
            loadUsers();
        }
    }

    @FXML
    private void onRefreshChart() {
        refreshChart();
    }

    @FXML
    private void onLogout() {
        try {
            App.showLoginView();
        } catch (IOException e) {
            AlertUtils.showError("Error", "No se pudo volver al login.");
        }
    }

    private void refreshChart() {
        progressChart.getData().clear();

        Swimmer swimmer = statsSwimmerCombo.getValue();
        String stroke = statsStrokeCombo.getValue();

        if (swimmer == null || stroke == null || stroke.isBlank()) {
            bestTimeLabel.setText("Mejor marca: -");
            averageTimeLabel.setText("Promedio: -");
            totalRecordsLabel.setText("Registros: 0");
            return;
        }

        List<TimeRecord> chartRecords = timeRecordDAO.findBySwimmerAndStroke(swimmer.getId(), stroke)
                .stream()
                .sorted(Comparator.comparing(TimeRecord::getRecordDate))
                .toList();

        if (!chartRecords.isEmpty()) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(swimmer.getFullName() + " - " + stroke);

            for (TimeRecord record : chartRecords) {
                series.getData().add(new XYChart.Data<>(record.getRecordDate().toString(), record.getTimeSeconds()));
            }

            progressChart.getData().add(series);
        }

        Double bestTime = timeRecordDAO.getBestTime(swimmer.getId(), stroke);
        Double averageTime = timeRecordDAO.getAverageTime(swimmer.getId(), stroke);
        int totalRecords = timeRecordDAO.countRecords(swimmer.getId(), stroke);

        bestTimeLabel.setText("Mejor marca: " + (bestTime == null ? "-" : TimeUtils.formatSeconds(bestTime)));
        averageTimeLabel.setText("Promedio: " + (averageTime == null ? "-" : TimeUtils.formatSeconds(averageTime)));
        totalRecordsLabel.setText("Registros: " + totalRecords);
    }

    private void validateSwimmerForm() {
        if (swimmerFirstNameField.getText() == null || swimmerFirstNameField.getText().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (swimmerLastNameField.getText() == null || swimmerLastNameField.getText().isBlank()) {
            throw new IllegalArgumentException("Los apellidos son obligatorios.");
        }
        if (swimmerBirthDatePicker.getValue() == null) {
            throw new IllegalArgumentException("La fecha de nacimiento es obligatoria.");
        }
        if (swimmerSexCombo.getValue() == null || swimmerSexCombo.getValue().isBlank()) {
            throw new IllegalArgumentException("Debes seleccionar el sexo.");
        }
        if (swimmerCategoryCombo.getValue() == null || swimmerCategoryCombo.getValue().isBlank()) {
            throw new IllegalArgumentException("Debes seleccionar la categoría.");
        }
    }

    private void validateRecordForm() {
        if (recordSwimmerCombo.getValue() == null) {
            throw new IllegalArgumentException("Debes seleccionar un nadador.");
        }
        if (recordDatePicker.getValue() == null) {
            throw new IllegalArgumentException("Debes seleccionar la fecha.");
        }
        if (recordStrokeCombo.getValue() == null || recordStrokeCombo.getValue().isBlank()) {
            throw new IllegalArgumentException("Debes seleccionar el estilo.");
        }
        if (recordTypeCombo.getValue() == null || recordTypeCombo.getValue().isBlank()) {
            throw new IllegalArgumentException("Debes seleccionar el tipo de sesión.");
        }

        double time = TimeUtils.parseToSeconds(recordTimeField.getText());
        if (time <= 0) {
            throw new IllegalArgumentException("El tiempo debe ser mayor que 0.");
        }
    }

    private void validateUserForm() {
        if (userUsernameField.getText() == null || userUsernameField.getText().isBlank()) {
            throw new IllegalArgumentException("El nombre de usuario es obligatorio.");
        }
        if (userUsernameField.getText().contains(" ")) {
            throw new IllegalArgumentException("El nombre de usuario no debe contener espacios.");
        }
        if (userFullNameField.getText() == null || userFullNameField.getText().isBlank()) {
            throw new IllegalArgumentException("El nombre completo es obligatorio.");
        }
        if (userRoleCombo.getValue() == null || userRoleCombo.getValue().isBlank()) {
            throw new IllegalArgumentException("Debes seleccionar un rol.");
        }
        if (userPasswordField.getText() == null || userPasswordField.getText().isBlank()) {
            throw new IllegalArgumentException("La contraseña es obligatoria.");
        }
        if (userPasswordField.getText().length() < 4) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 4 caracteres.");
        }
        if (!userPasswordField.getText().equals(userConfirmPasswordField.getText())) {
            throw new IllegalArgumentException("Las contraseñas no coinciden.");
        }
    }

    private void clearSwimmerForm() {
        selectedSwimmer = null;
        swimmerTable.getSelectionModel().clearSelection();
        swimmerFormLabel.setText("Nuevo nadador");
        swimmerFirstNameField.clear();
        swimmerLastNameField.clear();
        swimmerBirthDatePicker.setValue(null);
        swimmerSexCombo.setValue(null);
        swimmerCategoryCombo.setValue(null);
        swimmerClubField.clear();
        swimmerNotesArea.clear();
    }

    private void clearRecordForm() {
        selectedRecord = null;
        recordTable.getSelectionModel().clearSelection();
        recordFormLabel.setText("Nuevo registro");
        recordSwimmerCombo.setValue(null);
        recordDatePicker.setValue(LocalDate.now());
        recordStrokeCombo.setValue(null);
        recordTypeCombo.setValue(null);
        recordTimeField.clear();
        recordLocationField.clear();
        recordCoachField.setText(loggedUser == null ? "" : loggedUser.getFullName());
        recordCommentArea.clear();
    }

    private void clearUserForm() {
        selectedUser = null;
        if (userTable != null) {
            userTable.getSelectionModel().clearSelection();
        }
        userFormLabel.setText("Nuevo usuario");
        userUsernameField.clear();
        userFullNameField.clear();
        userRoleCombo.setValue(null);
        userPasswordField.clear();
        userConfirmPasswordField.clear();
    }

    private String emptyToNull(String value) {
        return value == null || value.isBlank() ? null : value;
    }

    private String nullIfBlank(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private String valueOrEmpty(String value) {
        return value == null ? "" : value;
    }
}
