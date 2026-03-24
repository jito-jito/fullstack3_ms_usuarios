package com.usuarios.usuarios.service;

// ===============================
// IMPORTACIONES DE SPRING
// ===============================
import org.springframework.stereotype.Service;

import com.usuarios.usuarios.exception.ResourceNotFoundException;
import com.usuarios.usuarios.model.Usuario;
import com.usuarios.usuarios.repository.UsuarioRepository;

// ===============================
// IMPORTACIONES DE LOMBOK
// ===============================
import lombok.extern.slf4j.Slf4j;

// ===============================
// IMPORTACIONES DE JAVA
// ===============================
import java.util.HashMap;
import java.util.Map;

/**
 * ===============================
 * SERVICIO: UsuarioService
 * ===============================
 *
 * Contiene únicamente la lógica de negocio necesaria para los endpoints del controlador:
 * - Crear usuario (guardar)
 * - Actualizar usuario (actualizar) 
 * - Eliminar usuario (eliminar - soft delete)
 * - Login de usuario (login)
 */
@Service
@Slf4j
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // ==========================================================
    // GUARDAR NUEVO USUARIO
    // ==========================================================

    public Usuario guardar(Usuario usuario) {
        log.info("Guardando nuevo usuario con email: {}", usuario.getEmail());

        // Verificar que el email no exista
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            log.warn("Email ya existe: {}", usuario.getEmail());
            throw new IllegalArgumentException("El email ya está registrado: " + usuario.getEmail());
        }

        // TODO: Aquí se debería encriptar la contraseña
        // usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        log.info("Usuario guardado correctamente con ID: {}", usuarioGuardado.getId());
        return usuarioGuardado;
    }

    // ==========================================================
    // ACTUALIZAR USUARIO
    // ==========================================================

    public Usuario actualizar(Long id, Usuario usuarioActualizado) {
        log.info("Intentando actualizar usuario con ID: {}", id);

        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se puede actualizar. Usuario no encontrado con ID: {}", id);
                    return new ResourceNotFoundException("Usuario no encontrado con ID: " + id);
                });

        // Verificar que el email no esté en uso por otro usuario
        if (!usuarioExistente.getEmail().equals(usuarioActualizado.getEmail()) 
            && usuarioRepository.existsByEmail(usuarioActualizado.getEmail())) {
            log.warn("Email ya existe: {}", usuarioActualizado.getEmail());
            throw new IllegalArgumentException("El email ya está registrado: " + usuarioActualizado.getEmail());
        }

        // Actualizar campos
        usuarioExistente.setNombre(usuarioActualizado.getNombre());
        usuarioExistente.setEmail(usuarioActualizado.getEmail());
        usuarioExistente.setRol(usuarioActualizado.getRol());
        
        // Solo actualizar password si se proporciona uno nuevo
        if (usuarioActualizado.getPassword() != null && !usuarioActualizado.getPassword().isEmpty()) {
            // TODO: Encriptar la nueva contraseña
            usuarioExistente.setPassword(usuarioActualizado.getPassword());
        }

        Usuario usuarioGuardado = usuarioRepository.save(usuarioExistente);
        log.info("Usuario actualizado correctamente con ID: {}", usuarioGuardado.getId());
        return usuarioGuardado;
    }

    // ==========================================================
    // ELIMINAR USUARIO (SOFT DELETE)
    // ==========================================================

    public void eliminar(Long id) {
        log.info("Intentando desactivar usuario con ID: {}", id);

        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se puede eliminar. Usuario no encontrado con ID: {}", id);
                    return new ResourceNotFoundException("Usuario no encontrado con ID: " + id);
                });

        // Soft delete - cambiar estado a inactivo
        usuarioExistente.setActivo(false);
        usuarioRepository.save(usuarioExistente);

        log.info("Usuario desactivado correctamente con ID: {}", id);
    }

    // ==========================================================
    // LOGIN DE USUARIO
    // ==========================================================

    public Map<String, Object> login(String email, String password) {
        log.info("Intento de login para email: {}", email);

        Usuario usuario = usuarioRepository.findByEmailAndActivo(email, true)
                .orElseThrow(() -> {
                    log.warn("Login fallido - usuario no encontrado o inactivo: {}", email);
                    return new ResourceNotFoundException("Credenciales inválidas");
                });

        // TODO: Verificar password encriptado
        // if (!passwordEncoder.matches(password, usuario.getPassword())) {
        if (!password.equals(usuario.getPassword())) {
            log.warn("Login fallido - contraseña incorrecta para: {}", email);
            throw new IllegalArgumentException("Credenciales inválidas");
        }

        log.info("Login exitoso para usuario: {}", email);

        Map<String, Object> response = new HashMap<>();
        response.put("usuario", usuario);
        response.put("mensaje", "Login exitoso");
        response.put("token", "token_temporal_" + usuario.getId()); // TODO: Implementar JWT
        
        return response;
    }
}