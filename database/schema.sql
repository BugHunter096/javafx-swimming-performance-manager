CREATE TABLE users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL UNIQUE,
    password_hash TEXT NOT NULL,
    full_name TEXT NOT NULL,
    role TEXT NOT NULL
);

CREATE TABLE swimmers (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    birth_date TEXT NOT NULL,
    sex TEXT NOT NULL,
    category TEXT NOT NULL,
    club TEXT,
    notes TEXT
);

CREATE TABLE time_records (
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
);
