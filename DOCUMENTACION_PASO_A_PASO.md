# Documentación paso a paso

## 1. Idea general del proyecto

El objetivo del proyecto es guardar y analizar los tiempos de los nadadores durante entrenamientos y competiciones.  
En vez de hacer una aplicación exageradamente compleja, se ha preparado una versión realista para un estudiante: funcional, clara y fácil de explicar.

---

## 2. Decisiones de diseño

### 2.1 Java
Se usa Java porque permite programación orientada a objetos y hace más sencillo organizar el proyecto por clases.

### 2.2 JavaFX
Se usa JavaFX para la interfaz de escritorio.  
La ventaja es que puedes separar:
- la vista (`FXML`)
- la lógica (`Controller`)

Eso hace que el proyecto quede más ordenado.

### 2.3 SQLite
SQLite es una buena opción para este proyecto porque:
- no necesita servidor
- se guarda en un archivo local
- es fácil de integrar con Java usando JDBC

---

## 3. Arquitectura usada

Se ha usado una estructura sencilla en capas:

- **model** → objetos del dominio (`Swimmer`, `TimeRecord`, `User`)
- **dao** → acceso a base de datos
- **controller** → eventos de la interfaz
- **service** → lógica de autenticación
- **util** → utilidades reutilizables

No es una arquitectura empresarial compleja.  
Es una arquitectura suficiente para un proyecto final de grado medio o superior.

---

## 4. Diseño de la base de datos

### Tabla `users`
Guarda los usuarios que pueden entrar en la aplicación.

Campos:
- `id`
- `username`
- `password_hash`
- `full_name`
- `role`

### Tabla `swimmers`
Guarda la información personal del nadador.

Campos:
- `id`
- `first_name`
- `last_name`
- `birth_date`
- `sex`
- `category`
- `club`
- `notes`

### Tabla `time_records`
Guarda cada marca.

Campos:
- `id`
- `swimmer_id`
- `record_date`
- `stroke`
- `session_type`
- `time_seconds`
- `location`
- `coach_comment`
- `coach_name`
- `created_by_user_id`

---

## 5. Por qué los tiempos se guardan en segundos

Esto es una decisión importante.

Si guardas un tiempo como texto (`01:08.45`), luego calcular medias, mejores marcas o dibujar un gráfico es más incómodo.

Por eso se guarda como número:
- `68.45`

Y luego, cuando se enseña al usuario, se convierte a:
- `01:08.45`

Eso se hace en la clase `TimeUtils`.

---

## 6. Explicación de las clases principales

## 6.1 `App.java`
Es la clase principal de JavaFX.

Se encarga de:
- arrancar la aplicación
- inicializar la base de datos
- abrir la pantalla de login
- cambiar entre login y pantalla principal

---

## 6.2 `DatabaseManager.java`
Es la clase que centraliza la conexión con SQLite.

Funciones:
- crear la carpeta `data`
- abrir la conexión JDBC
- activar claves foráneas
- crear tablas si no existen
- insertar usuario y datos de ejemplo

Esto es útil porque así la app funciona desde el primer arranque.

---

## 6.3 Modelos (`User`, `Swimmer`, `TimeRecord`, `RecordFilter`)
Representan los datos del sistema.

### `User`
Representa un usuario que puede iniciar sesión.

### `Swimmer`
Representa un nadador.

### `TimeRecord`
Representa una marca registrada.

### `RecordFilter`
Es un objeto auxiliar para pasar filtros a la consulta SQL sin llenar el código de muchos parámetros.

---

## 6.4 DAO (`UserDAO`, `SwimmerDAO`, `TimeRecordDAO`)
Estas clases hablan directamente con la base de datos.

### `UserDAO`
Busca usuarios.

### `SwimmerDAO`
Hace:
- listar
- insertar
- actualizar
- eliminar nadadores

### `TimeRecordDAO`
Hace:
- listar registros con filtros
- insertar
- actualizar
- eliminar
- obtener mejor tiempo
- obtener promedio
- contar registros

---

## 6.5 `AuthService.java`
Se encarga de validar el login:
1. busca el usuario por nombre
2. genera el hash de la contraseña escrita
3. compara el hash escrito con el guardado en base de datos

---

## 6.6 Utilidades

### `PasswordUtils`
Genera un hash SHA-256 de la contraseña.

### `TimeUtils`
Convierte tiempos:
- de texto a segundos
- de segundos a formato visual

### `AlertUtils`
Evita repetir código de alertas en los controladores.

---

## 7. Interfaz gráfica

## 7.1 `login-view.fxml`
Pantalla de acceso:
- usuario
- contraseña
- botón de iniciar sesión
- botón salir

## 7.2 `main-view.fxml`
Pantalla principal con tres pestañas:

### Pestaña 1: Nadadores
Permite:
- buscar
- filtrar por sexo y categoría
- ver tabla
- añadir
- editar
- eliminar

### Pestaña 2: Marcas y entrenamientos
Permite:
- ver tabla de registros
- filtrar por varios criterios
- crear y modificar marcas
- guardar comentarios del entrenador

### Pestaña 3: Estadísticas
Permite:
- elegir nadador
- elegir estilo
- ver gráfico de progresión
- ver mejor marca
- ver promedio
- ver número de registros

---

## 8. Cómo funciona un caso real

### Ejemplo: guardar un nuevo registro
1. El usuario selecciona un nadador.
2. Introduce fecha, estilo, tipo de sesión y tiempo.
3. Pulsa **Guardar registro**.
4. El controlador valida los datos.
5. Convierte el tiempo a segundos.
6. Llama al DAO.
7. El DAO ejecuta el `INSERT` en SQLite.
8. Se recarga la tabla.
9. Se actualiza el gráfico.

---

## 9. Seguridad aplicada

La seguridad aquí es básica, no profesional de empresa.

Qué se hizo:
- login
- contraseña guardada como hash SHA-256
- sesión del usuario mostrada en la parte superior

Qué faltaría en una versión más avanzada:
- sal aleatoria por contraseña
- recuperación de claves
- permisos reales por rol
- auditoría de cambios

---

## 10. Limitaciones reales del proyecto

Esto es importante decirlo en la defensa porque demuestra criterio.

Limitaciones:
- solo hay un usuario inicial de ejemplo
- los roles todavía no limitan acciones
- no hay exportación de datos
- no hay comparativa múltiple entre nadadores
- no hay tests automatizados

---

## 11. Mejoras futuras razonables

- pantalla de gestión de usuarios
- exportar informes
- más gráficos
- rankings por estilo
- estadísticas por categoría
- comparación de dos nadadores
- empaquetado instalable

---

## 12. Conclusión

Este proyecto cumple bien con el objetivo principal:
- registrar nadadores
- guardar marcas
- filtrar información
- visualizar progresión
- mantener los datos en local

Además, está montado de una forma bastante limpia para poder explicarlo y ampliarlo sin destrozar el código.
