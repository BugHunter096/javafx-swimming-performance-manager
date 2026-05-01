# Progresión de marcas en natación

Aplicación de escritorio hecha en **Java + JavaFX + SQLite** para registrar nadadores, guardar marcas, filtrar resultados y visualizar la progresión del rendimiento.

## Qué hace la aplicación

- Login básico con usuario local.
- CRUD de nadadores.
- CRUD de registros de marcas.
- Filtros por sexo, categoría, estilo, tipo de sesión, fechas y texto.
- Comentarios del entrenador en cada registro.
- Gráfico de progresión por nadador y estilo.
- Base de datos SQLite local que se crea automáticamente.

## Tecnologías usadas

- **Java 21**
- **JavaFX** para la interfaz
- **FXML** para separar vista y lógica
- **SQLite** para base de datos local
- **Maven** para dependencias y ejecución

## Usuario inicial

- **Usuario:** `admin`
- **Contraseña:** `admin123`

## Cómo ejecutar en IntelliJ IDEA

1. Abre IntelliJ.
2. Pulsa **Open** y selecciona la carpeta `proyecto-natacion`.
3. Espera a que IntelliJ importe el `pom.xml` y descargue dependencias.
4. Busca la clase `com.progresionnacion.Launcher`.
5. Ejecuta esa clase.

## Cómo ejecutar por terminal

Necesitas Maven instalado.

```bash
mvn clean javafx:run
```

## Estructura del proyecto

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

## Base de datos

La aplicación crea automáticamente el archivo:

```text
data/progresion_natacion.db
```

También deja unos datos de ejemplo para poder probarla nada más arrancar.

## Cosas importantes para defender el proyecto

- Separé la interfaz (`FXML`) de la lógica (`controllers`) para que el código fuese más limpio.
- Elegí SQLite porque no necesita servidor.
- Guardé los tiempos en **segundos** para facilitar cálculos, filtros y estadísticas.
- Formateé esos segundos a `mm:ss.ss` en la interfaz para que el usuario vea una marca comprensible.
- La seguridad se implementó de forma básica con hash SHA-256 para la contraseña del usuario inicial.

## Mejoras futuras

- Gestión completa de usuarios y roles.
- Exportación a PDF o Excel.
- Estadísticas más avanzadas.
- Comparación directa entre varios nadadores.
- Validaciones más estrictas y tests automáticos.
