package com.usuarios.usuarios.repository;

// ===============================
// IMPORTACIÓN DE SPRING DATA JPA
// ===============================
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.usuarios.usuarios.model.Usuario;

import java.util.List;
import java.util.Optional;

/**
 * ===============================
 * REPOSITORY: UsuarioRepository
 * ===============================
 *
 * Interfaz de acceso a datos para la entidad Usuario.
 * Proporciona operaciones CRUD y consultas específicas para usuarios.
 *
 * Spring Data JPA generará automáticamente la implementación.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca un usuario por su email
     * @param email email del usuario
     * @return Optional con el usuario encontrado
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Verifica si existe un usuario con el email dado
     * @param email email a verificar
     * @return true si existe, false si no
     */
    boolean existsByEmail(String email);

    /**
     * Busca un usuario por email y que esté activo
     * @param email email del usuario
     * @param activo estado activo del usuario
     * @return Optional con el usuario encontrado
     */
    Optional<Usuario> findByEmailAndActivo(String email, Boolean activo);

    /**
     * Obtiene todos los usuarios activos
     * @return lista de usuarios activos
     */
    List<Usuario> findByActivoTrue();

    /**
     * Obtiene todos los usuarios inactivos
     * @return lista de usuarios inactivos
     */
    List<Usuario> findByActivoFalse();

    /**
     * Busca usuarios por rol
     * @param rol rol del usuario
     * @return lista de usuarios con el rol especificado
     */
    List<Usuario> findByRol(String rol);

    /**
     * Busca usuarios por rol y que estén activos
     * @param rol rol del usuario
     * @param activo estado activo del usuario
     * @return lista de usuarios que cumplen los criterios
     */
    List<Usuario> findByRolAndActivo(String rol, Boolean activo);

    /**
     * Busca usuarios por nombre (búsqueda parcial, case insensitive)
     * @param nombre nombre o parte del nombre del usuario
     * @return lista de usuarios que contienen el texto en su nombre
     */
    List<Usuario> findByNombreContainingIgnoreCase(String nombre);
}