# Pistón Riojano

Aplicación de escritorio para un taller mecánico. Trabajo práctico de Paradigmas y Lenguajes III, UNLaR.

## Abrir en NetBeans

1. Clonar el repositorio y abrir NetBeans.
2. Elegir **File → Open Project** y seleccionar la carpeta `proyecto`, que contiene `pom.xml`.
3. Usar un JDK 17 o superior. El proyecto compila para Java 17 y Maven descarga el driver JDBC.
4. Para comprobar la compilación, elegir **Build** en NetBeans o ejecutar `mvn -f proyecto/pom.xml package` desde la raíz si Maven está disponible en la terminal.

## Estructura

| Carpeta | Contenido |
| --- | --- |
| `diseño/` | Diagrama de clases y modelo relacional. |
| `sql/` | `schema.sql` crea las tablas y `datos.sql` carga datos ficticios para pruebas. |
| `proyecto/src/main/java/modelo/` | Entidades, enumeraciones y reglas de negocio. |
| `proyecto/src/main/java/dao/` | Conexión y acceso a PostgreSQL mediante JDBC. |
| `proyecto/src/main/java/vista/` | Ventana MDI y formularios Swing. |
| `proyecto/src/main/resources/` | Recursos de la aplicación. |

Para preparar la base de desarrollo en PostgreSQL, crear una base vacía como `taller_piston_prueba`. Ejecutar `sql/schema.sql` y luego `sql/datos.sql` sobre esa misma base. Volver a ejecutar el esquema elimina los datos de esas tablas.
