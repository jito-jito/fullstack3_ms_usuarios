package com.usuarios.usuarios.controller;

// ===============================
// IMPORTACIONES SPRING
// ===============================
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.usuarios.usuarios.model.Usuario;
import com.usuarios.usuarios.service.UsuarioService;

// ===============================
// VALIDACIONES
// ===============================
import jakarta.validation.Valid;

import java.util.Map;

/**
 * ===============================
 * CONTROLLER: UsuarioController
 * ===============================
 *
 * Maneja las solicitudes HTTP del cliente para gestión de usuarios.
 * Endpoints habilitados: crear, modificar, eliminar y login.
 */
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // ==========================================================
    // CREAR USUARIO
    // ==========================================================

    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@Valid @RequestBody Usuario usuario) {
        Usuario nuevoUsuario = usuarioService.guardar(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }

    // ==========================================================
    // ACTUALIZAR USUARIO
    // ==========================================================

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(
            @PathVariable Long id,
            @Valid @RequestBody Usuario usuario) {
        Usuario usuarioActualizado = usuarioService.actualizar(id, usuario);
        return ResponseEntity.ok(usuarioActualizado);
    }

    // ==========================================================
    // ELIMINAR USUARIO (SOFT DELETE - cambiar a inactivo)
    // ==========================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // ==========================================================
    // LOGIN DE USUARIO
    // ==========================================================

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUsuario(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String password = loginData.get("password");
        
        Map<String, Object> response = usuarioService.login(email, password);
        return ResponseEntity.ok(response);
    }

}