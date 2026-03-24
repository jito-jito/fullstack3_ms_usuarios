package com.usuarios.usuarios.model;

// ===============================
// IMPORTACIONES JPA
// ===============================
import jakarta.persistence.*;

// ===============================
// IMPORTACIONES DE VALIDACIÓN
// ===============================
import jakarta.validation.constraints.*;

// ===============================
// IMPORTACIONES DE TIEMPO
// ===============================
import java.time.LocalDateTime;

/**
 * ===============================
 * MODELO: Usuario
 * ===============================
 * 
 * Entidad que representa un usuario del sistema.
 * Permite gestionar usuarios con operaciones CRUD básicas
 * e inicio de sesión.
 */
@Entity
@Table(name = "USUARIOS")
public class Usuario {

    /**
     * Clave primaria de la tabla usuarios
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre completo del usuario
     */
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Column(name = "NOMBRE", nullable = false, length = 100)
    private String nombre;

    /**
     * Email único del usuario para login
     */
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    @Size(max = 150, message = "El email no puede exceder 150 caracteres")
    @Column(name = "EMAIL", nullable = false, unique = true, length = 150)
    private String email;

    /**
     * Contraseña del usuario
     */
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, max = 255, message = "La contraseña debe tener al menos 6 caracteres")
    @Column(name = "PASSWORD", nullable = false)
    private String password;

    /**
     * Rol del usuario en el sistema
     */
    @NotBlank(message = "El rol es obligatorio")
    @Pattern(regexp = "^(ADMIN|USER)$", message = "El rol debe ser ADMIN o USER")
    @Column(name = "ROL", nullable = false, length = 20)
    private String rol;

    /**
     * Estado activo/inactivo del usuario
     */
    @Column(name = "ACTIVO", nullable = false)
    private Boolean activo = true;

    /**
     * Fecha de creación del usuario
     */
    @Column(name = "FECHA_CREACION", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    /**
     * Fecha de última actualización
     */
    @Column(name = "FECHA_ACTUALIZACION")
    private LocalDateTime fechaActualizacion;

    /**
     * Constructor vacío obligatorio para JPA
     */
    public Usuario() {
    }

    /**
     * Constructor con parámetros principales
     */
    public Usuario(String nombre, String email, String password, String rol) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.rol = rol;
        this.activo = true;
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }

    /**
     * Se ejecuta antes de persistir la entidad
     */
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
    }

    /**
     * Se ejecuta antes de actualizar la entidad
     */
    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }

    // ===============================
    // GETTERS Y SETTERS
    // ===============================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}