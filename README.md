# Microservicio de Gestión de Usuarios

## Descripción
Microservicio para la gestión básica de usuarios con operaciones CRUD y sistema de autenticación.

## Características
- ✅ Crear, actualizar y eliminar usuarios (soft delete)
- ✅ Sistema de login con validación de credenciales
- ✅ Gestión de roles (ADMIN, USER)
- ✅ Validaciones de datos completas
- ✅ Manejo global de excepciones
- ✅ Estados activos/inactivos de usuarios

## Configuración
- **Puerto**: 8081
- **Base URL**: `http://localhost:8081/api/usuarios`
- **Base de datos**: Oracle Cloud

## Endpoints Disponibles

### Usuarios CRUD
- `POST /api/usuarios` - Crear usuario
- `PUT /api/usuarios/{id}` - Actualizar usuario
- `DELETE /api/usuarios/{id}` - Eliminar usuario (soft delete)

### Autenticación
- `POST /api/usuarios/login` - Login de usuario

## Modelo de Usuario
```json
{
  "id": 1,
  "nombre": "Juan Pérez",
  "email": "juan.perez@ejemplo.com",
  "password": "miPassword123",
  "rol": "USER",
  "activo": true,
  "fechaCreacion": "2026-03-23T10:30:00",
  "fechaActualizacion": "2026-03-23T10:30:00"
}
```

## Validaciones
- **Nombre**: requerido, 2-100 caracteres
- **Email**: requerido, formato válido, único, máximo 150 caracteres
- **Password**: requerido, mínimo 6 caracteres
- **Rol**: requerido, debe ser "ADMIN" o "USER"
- **Activo**: por defecto true

## Cómo ejecutar
```bash
# Compilar
./mvnw clean compile

# Ejecutar
./mvnw spring-boot:run

# Ejecutar tests
./mvnw test
```

## Ejemplos de uso

### Crear usuario
```bash
curl -X POST http://localhost:8081/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "María González",
    "email": "maria.gonzalez@ejemplo.com",
    "password": "password123",
    "rol": "USER"
  }'
```

### Actualizar usuario
```bash
curl -X PUT http://localhost:8081/api/usuarios/1 \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "María Rodríguez",
    "email": "maria.rodriguez@ejemplo.com",
    "password": "newPassword123",
    "rol": "ADMIN"
  }'
```

### Eliminar usuario (soft delete)
```bash
curl -X DELETE "http://localhost:8081/api/usuarios/1"
```

### Login de usuario
```bash
curl -X POST http://localhost:8081/api/usuarios/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "maria.gonzalez@ejemplo.com",
    "password": "password123"
  }'
```

**Respuesta del login:**
```json
{
  "usuario": {
    "id": 1,
    "nombre": "María González",
    "email": "maria.gonzalez@ejemplo.com",
    "rol": "USER",
    "activo": true,
    "fechaCreacion": "2026-03-23T10:30:00"
  },
  "mensaje": "Login exitoso",
  "token": "token_temporal_1"
}
```

## Manejo de Errores
El microservicio incluye manejo global de excepciones que devuelve respuestas estructuradas:

### Error de validación (400)
```json
{
  "timestamp": "2026-03-23T10:30:00",
  "message": "Validación fallida: El email es obligatorio, El nombre debe tener entre 2 y 100 caracteres",
  "details": "uri=/api/usuarios"
}
```

### Usuario no encontrado (404)
```json
{
  "timestamp": "2026-03-23T10:30:00",
  "message": "Usuario no encontrado con ID: 999",
  "details": "uri=/api/usuarios/999"
}
```

### Email duplicado (400)
```json
{
  "timestamp": "2026-03-23T10:30:00",
  "message": "El email ya está registrado: test@ejemplo.com",
  "details": "uri=/api/usuarios"
}
```

## Tecnologías Utilizadas
- **Spring Boot 3.5.11**
- **Spring Data JPA**
- **Spring Validation**
- **Oracle Database**
- **Maven**
- **Java 17**

## Estructura del Proyecto
```
src/
├── main/
│   ├── java/com/usuarios/usuarios/
│   │   ├── UsuarioApplication.java
│   │   ├── controller/
│   │   │   └── UsuarioController.java
│   │   ├── service/
│   │   │   └── UsuarioService.java
│   │   ├── repository/
│   │   │   └── UsuarioRepository.java
│   │   ├── model/
│   │   │   └── Usuario.java
│   │   └── exception/
│   │       ├── GlobalExceptionHandler.java
│   │       ├── ResourceNotFoundException.java
│   │       └── ErrorDetails.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/usuarios/usuarios/
        └── UsuarioApplicationTests.java
```