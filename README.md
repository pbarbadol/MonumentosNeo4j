# ViaNorba: Explorador de Monumentos de Cáceres

![Java Version](https://img.shields.io/badge/Java-11%2B-blue)
![Status](https://img.shields.io/badge/Status-Completo-brightgreen)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT) <!-- Asumiendo Licencia MIT, ajusta si es necesario -->

Una aplicación web de portfolio que permite explorar monumentos históricos de Cáceres, utilizando Java con SparkJava para el backend, Neo4j como base de datos de grafos para almacenar y relacionar monumentos, y Leaflet.js para la visualización en un mapa interactivo. Incluye funcionalidades de gestión de usuarios y cálculo de rutas.

<!-- ![Demostración de ViaNorba](URL_A_TU_IMAGEN_O_GIF) -->

## Características Principales

*   **Visualización en Mapa Interactivo:** Muestra monumentos de Cáceres en un mapa usando Leaflet.js, cargando datos desde el backend.
*   **Información Detallada:** Permite hacer clic en los marcadores del mapa para ver detalles básicos de cada monumento (obtenidos vía API).
*   **Filtrado de Monumentos:** Permite filtrar los monumentos mostrados en el mapa por su tipo (Iglesia, Palacio, Torre, etc.).
*   **Gestión de Usuarios:**
    *   Registro de nuevos usuarios.
    *   Inicio de sesión (Autenticación básica).
    *   Gestión de perfil de usuario (ver, actualizar, eliminar cuenta).
    *   Manejo de sesiones para usuarios autenticados.
*   **Monumentos Favoritos:** Los usuarios registrados pueden marcar/desmarcar monumentos como favoritos y ver su lista personalizada.
*   **Cálculo de Rutas:** Encuentra y visualiza (como lista, la visualización en mapa sería una mejora futura) la ruta más corta entre dos monumentos seleccionados, utilizando las capacidades de grafos de Neo4j y las relaciones `CERCANO_A` precalculadas.
*   **Carga Inicial de Datos:** Script de administración (`Administrador.java`) para poblar la base de datos Neo4j desde un archivo `monumentos.json` e insertar usuarios de ejemplo.
*   **Conexión Automática de Monumentos:** El script de administración puede crear relaciones `[:CERCANO_A]` entre monumentos basándose en su proximidad geográfica.

## Tech Stack

*   **Backend:**
    *   **Java 11+**: Lenguaje de programación principal.
    *   **SparkJava**: Microframework web para definir la API REST y servir archivos estáticos.
    *   **Neo4j**: Base de datos de grafos para almacenar monumentos, usuarios y sus relaciones (cercanía, favoritos).
    *   **Neo4j Java Driver**: Driver oficial para interactuar con Neo4j desde Java.
    *   **Gson**: Librería para serializar/deserializar objetos Java a/desde JSON.
*   **Frontend:**
    *   **HTML5**: Estructura de las páginas web.
    *   **CSS3**: Estilos visuales (`style.css`).
    *   **JavaScript (ES6+)**: Lógica del lado del cliente, interacciones con el mapa y llamadas a la API (usando `fetch`).
    *   **Leaflet.js**: Librería JavaScript para mapas interactivos.
*   **Build Tool:**
    *   **Gradle**: Herramienta de automatización de construcción y gestión de dependencias.
*   **Base de Datos:**
    *   **Neo4j Server**: Instancia de la base de datos Neo4j (se requiere una instalación local o remota).

## Instalación

1.  **Prerrequisitos:**
    *   Tener instalado **JDK 11** o superior.
    *   Tener instalado **Gradle** (o usar el Gradle Wrapper incluido si existe `./gradlew`).
    *   Tener una instancia de **Neo4j Server** ejecutándose. Por defecto, la aplicación espera conectarse a `bolt://localhost:7687` con el usuario `neo4j` y la contraseña `monumento`. *Asegúrate de que estos datos coinciden con tu configuración de Neo4j o modifícalos en `Neo4jConnection.java` o donde se inicialice.*

2.  **Clonar el Repositorio:**
    ```bash
    git clone https://github.com/tu_usuario/via_norba.git # Reemplaza con tu URL
    cd via_norba
    ```

3.  **Construir el Proyecto:**
    *   Utiliza Gradle para compilar el código y descargar las dependencias:
        ```bash
        gradle build
        ```
        *(O si usas el wrapper: `./gradlew build`)*

## Preparación de la Base de Datos y Datos Iniciales

1.  **Asegúrate de que Neo4j esté corriendo** y accesible con las credenciales configuradas.
2.  **Prepara el archivo de datos:** Coloca tu archivo `monumentos.json` dentro del directorio `src/main/resources/`. Este archivo debe contener la información de los monumentos a cargar.
3.  **Ejecuta el script de Administración:** Este script limpiará datos previos (opcionalmente), insertará los monumentos desde el JSON, creará usuarios de ejemplo y conectará monumentos cercanos.
    *   Puedes ejecutar la clase `Administrador.java` desde tu IDE (IntelliJ, Eclipse, etc.).
    *   O bien, si has configurado una tarea Gradle específica o un JAR ejecutable, úsalo. (Una forma simple desde línea de comandos después de `gradle build` podría ser, ajusta según tu estructura):
        ```bash
        # Ejemplo genérico, puede variar según tu configuración de build
        java -cp build/classes/java/main:build/resources/main:$(echo build/libs/*.jar build/dependencies/* | tr ' ' ':') Administrador
        ```
    *   **¡Importante!** Este script realiza operaciones destructivas si se ejecuta varias veces sin cuidado (borra y reinserta). Ejecútalo una vez para la configuración inicial. Revisa el código de `Administrador.java` para entender qué operaciones realiza.

## Uso

1.  **Iniciar la Aplicación Web:**
    *   Ejecuta la clase `Main.java` desde tu IDE.
    *   O, si has configurado una tarea `run` en Gradle:
        ```bash
        gradle run
        ```
        *(O `./gradlew run`)*
    *   La consola indicará que el servidor está corriendo en el puerto 8080 (o el configurado en `Main.java`).

2.  **Acceder a la Aplicación:**
    *   Abre tu navegador web y ve a `http://localhost:8080`.
    *   Serás redirigido a `index.html`, donde verás el mapa y las opciones.
    *   Puedes registrarte, iniciar sesión, explorar el mapa, filtrar monumentos, añadirlos a favoritos (si estás logueado) y calcular rutas entre ellos.

## API Endpoints Principales

La aplicación expone una API REST gestionada por `MonumentoController` y `UsuarioController`. Algunos endpoints clave:

*   `GET /monumentos`: Obtiene todos los monumentos.
*   `GET /monumentos/clase/:clase`: Obtiene monumentos filtrados por clase.
*   `GET /monumentos/:uri`: Obtiene detalles de un monumento específico.
*   `GET /monumentos/shortestPath?startUri=...&endUri=...`: Calcula la ruta más corta entre dos monumentos.
*   `POST /usuarios/registro`: Registra un nuevo usuario.
*   `POST /usuarios/login`: Autentica un usuario.
*   `GET /usuarios/logueado`: Verifica si hay un usuario logueado en la sesión.
*   `GET /usuarios/perfil`: Obtiene los datos del perfil del usuario logueado.
*   `POST /usuarios/actualizar`: Actualiza el perfil del usuario.
*   `POST /usuarios/logout`: Cierra la sesión del usuario.
*   `POST /usuarios/eliminar`: Elimina la cuenta del usuario.
*   `GET /usuarios/favoritos`: Obtiene la lista de monumentos favoritos del usuario.
*   `POST /usuarios/favoritos/add/:uri`: Añade un monumento a favoritos.
*   `POST /usuarios/favoritos/delete/:uri`: Elimina un monumento de favoritos.
*   *(Endpoints de administración como `POST /monumentos/add`, `DELETE /monumentos/:uri`, etc., también existen pero están más orientados a la gestión directa)*

## Limitaciones

*   **Autenticación Básica:** La gestión de contraseñas es simple (almacenamiento directo en Neo4j como String). No implementa hashing de contraseñas, lo cual es inseguro para producción.
*   **Visualización de Rutas:** La ruta más corta se devuelve como una lista de monumentos en JSON, pero no se dibuja visualmente en el mapa Leaflet en la implementación actual.
*   **Manejo de Errores:** Podría mejorarse el manejo de errores y feedback al usuario en el frontend y backend.
*   **Escalabilidad:** Para un número muy grande de monumentos o usuarios, podrían necesitarse optimizaciones en las consultas Cypher o en la arquitectura.

## Posibles Mejoras Futuras

*   Implementar hashing seguro de contraseñas (e.g., bcrypt).
*   Dibujar la ruta más corta calculada directamente sobre el mapa Leaflet.
*   Añadir más detalles de los monumentos (descripción, imágenes, horarios).
*   Implementar roles de usuario (ej. Administrador con interfaz web).
*   Mejorar la interfaz de usuario y la experiencia general.
*   Añadir tests unitarios y de integración.
*   Empaquetar la aplicación con Docker para facilitar el despliegue.

## Licencia

Distribuido bajo la Licencia MIT. Ver `LICENSE` para más información.

---

*Este proyecto fue desarrollado como parte de mi portfolio en Ingeniería de Software.*
