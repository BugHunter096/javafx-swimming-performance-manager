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

<img width="500" height="369" alt="Login" src="https://github.com/user-attachments/assets/5b6a315c-b3f7-4615-9f08-de2a2f341ddb" />


### Main Dashboard

<img width="1397" height="888" alt="Main Dashboard" src="https://github.com/user-attachments/assets/b0dff943-d5db-4cd6-a2ad-a5068eda8ada" />


### Swimmers Management

<img width="1398" height="888" alt="Swimmer Management" src="https://github.com/user-attachments/assets/7712e690-93c9-4e89-abce-2ed416d8f23b" />


### Performance Progress Chart

<img width="1398" height="888" alt="Progress Chart" src="https://github.com/user-attachments/assets/d914c332-6cda-483a-a21f-9998e35f43a8" />


### Time Records

<img width="1397" height="890" alt="Time Records" src="https://github.com/user-attachments/assets/f031d614-6aaf-46e0-a0c7-ec4c867fa7c2" />

