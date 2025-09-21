# Task Manager API
## Objetivo
Desarrollar una API REST para gestionar tareas personales, aplicando buenas prácticas modernas de diseño, validación, persistencia, pruebas y nuevas características de Java 17 y Spring Boot 3.

## Funcionalidades Básicas
- CRUD completo de tareas
    * Crear tarea
    * Obtener todas las tareas
    * Obtener tarea por ID
    * Editar tarea
    * Cambiar de estados ("To do" -> "In Progress" -> "Done")
    * Eliminar tarea
- Manejo de Excepciones Personalizadas
- Persistencia en Base de Datos en memoria (H2)

## Funcionalidades extras
* Ordenamiento y filtrado personalizado vía queries
* Tareas programables
* CRUD usuario, autenticación y panel de configuración

### Tecnologías aplicadas y conceptos
- Java 17
- Spring Boot 3
- Bean Validations
- Spring Data JPA
- H2 Database
- Spring Web
- Mockito
- MockMvc
- JUnit 5
- Spring HATEOAS
- Git
- SonarLint
- Docker

#### Checklist desarrollo actual

- [x] Novedades de Java 17
    + [x] Uso de `record` para DTO
    + [x] Uso de `switch` mejorado para enums o flujos
    + [x] Uso de Text Blocks (""") para logs, errores y templates.
    + [x] Sealed Classes para jerarquizar errores.
- [x] Persistencia
    + [ ] Queries personalizadas (orden/filtro)
    + [ ] Migrar a BD Relacional
- [x] CRUD completo de tareas
- [x] Validaciones (form input)
- [x] Manejo global de excepciones personalizadas
- [x] HATEOAS
- [ ] Scheduling
- [x] Testing
- [ ] Dockerizar proyecto




    
