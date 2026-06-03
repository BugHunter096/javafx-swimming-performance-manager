# Swim Performance Tracker

Desktop application built with **Java, JavaFX and SQLite** to register swimmers, store race times, filter results and visualize performance progress over time.

## Features

- Basic local login.
- Swimmer CRUD.
- Race time record CRUD.
- Filters by gender, category, stroke, session type, date range and text.
- Coach comments for each record.
- Progress chart by swimmer and stroke.
- Local SQLite database created automatically on startup.

## Technologies

- Java 21
- JavaFX for the user interface
- FXML to separate views from logic
- SQLite for local data storage
- Maven for dependency management and execution

## Default User

Username: admin
Password: admin123

## How to Run with IntelliJ IDEA

1. Open IntelliJ IDEA.
2. Click Open and select the proyecto-natacion folder.
3. Wait for IntelliJ to import the pom.xml file and download the dependencies.
4. Find the class: com.progresionnacion.Launcher
5. Run that class.


## Project Structure

```text
src/main/java/com/progresionnacion
├── App.java
├── Launcher.java
├── DatabaseManager.java
├── controller
│   ├── LoginController.java
│   └── MainController.java
├── dao
│   ├── SwimmerDAO.java
│   ├── TimeRecordDAO.java
│   └── UserDAO.java
├── model
│   ├── RecordFilter.java
│   ├── Swimmer.java
│   ├── TimeRecord.java
│   └── User.java
├── service
│   └── AuthService.java
└── util
    ├── AlertUtils.java
    ├── PasswordUtils.java
    └── TimeUtils.java
```

## Database

The application automatically creates the following database file:

```text
data/progresion_natacion.db
```

## Future Improvements

- Complete user and role management.
- Export data to PDF or Excel.
- More advanced statistics.
- Direct comparison between multiple swimmers.
- Stricter validations and automated tests.

## Screenshots

### Login


### Swimmers Management

![Swimmers table](assets/screenshots/swimmers-table.png)

### Time Record Form

![Time record form](assets/screenshots/time-record-form.png)

### Performance Progress Chart

![Performance progress chart](assets/screenshots/progress-chart.png)

### Database Structure

![Database structure](assets/screenshots/database-structure.png)
