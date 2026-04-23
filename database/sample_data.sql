INSERT INTO users (username, password_hash, full_name, role)
VALUES ('admin', '240be518fabd2724ddb6f04eeb23bd4a1057df1f5752a64c0bc11d6b7d89bdf9', 'Administrador inicial', 'ADMIN');

INSERT INTO swimmers (first_name, last_name, birth_date, sex, category, club, notes) VALUES
('Lucía', 'Gómez', '2010-05-14', 'Femenino', 'Infantil', 'Club Delfín', 'Especialista en crol'),
('Mario', 'Santos', '2008-09-03', 'Masculino', 'Junior', 'Club Delfín', 'Buen final en mariposa'),
('Clara', 'Ruiz', '2012-01-21', 'Femenino', 'Alevín', 'Club Delfín', 'Debe mejorar la salida'),
('Pablo', 'Martín', '2006-11-10', 'Masculino', 'Absoluto', 'Club Delfín', 'Constante en entrenamientos');

INSERT INTO time_records (swimmer_id, record_date, stroke, session_type, time_seconds, location, coach_comment, coach_name, created_by_user_id) VALUES
(1, '2026-03-01', 'Crol', 'Entrenamiento', 68.40, 'Piscina municipal', 'Buen ritmo en la parte central', 'Administrador inicial', 1),
(1, '2026-03-08', 'Crol', 'Entrenamiento', 67.95, 'Piscina municipal', 'Mejora de técnica en viraje', 'Administrador inicial', 1),
(1, '2026-03-15', 'Crol', 'Competición', 67.10, 'Centro acuático', 'Marca muy sólida', 'Administrador inicial', 1),
(2, '2026-03-04', 'Mariposa', 'Entrenamiento', 72.85, 'Piscina municipal', 'Falta mantener frecuencia al final', 'Administrador inicial', 1),
(2, '2026-03-18', 'Mariposa', 'Competición', 71.90, 'Centro acuático', 'Buena salida', 'Administrador inicial', 1),
(3, '2026-03-06', 'Espalda', 'Entrenamiento', 81.55, 'Piscina municipal', 'Corregir entrada de mano', 'Administrador inicial', 1),
(4, '2026-03-20', 'Braza', 'Entrenamiento', 75.35, 'Piscina municipal', 'Muy regular', 'Administrador inicial', 1);
