package com.usuarios.usuarios.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usuarios.usuarios.exception.ResourceNotFoundException;
import com.usuarios.usuarios.model.Usuario;
import com.usuarios.usuarios.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario("Juan Pérez", "juan@ejemplo.com", "password123", "USER");
        usuario.setId(1L);
    }

    // ==========================================
    // POST /api/usuarios
    // ==========================================

    @Test
    void crearUsuario_datosValidos_retorna201() throws Exception {
        when(usuarioService.guardar(any(Usuario.class))).thenReturn(usuario);

        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("juan@ejemplo.com"))
                .andExpect(jsonPath("$.nombre").value("Juan Pérez"))
                .andExpect(jsonPath("$.rol").value("USER"));
    }

    @Test
    void crearUsuario_datosInvalidos_retorna400() throws Exception {
        // nombre vacío, email inválido, sin password ni rol → @Valid falla
        String jsonInvalido = """
                {
                  "nombre": "",
                  "email": "no-es-un-email",
                  "password": "",
                  "rol": "INVALIDO"
                }
                """;

        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonInvalido))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void crearUsuario_emailDuplicado_retorna500() throws Exception {
        // IllegalArgumentException no está mapeada específicamente → GlobalExceptionHandler → 500
        when(usuarioService.guardar(any(Usuario.class)))
                .thenThrow(new IllegalArgumentException("El email ya está registrado: juan@ejemplo.com"));

        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Error interno del servidor"));
    }

    // ==========================================
    // PUT /api/usuarios/{id}
    // ==========================================

    @Test
    void actualizarUsuario_datosValidos_retorna200() throws Exception {
        when(usuarioService.actualizar(eq(1L), any(Usuario.class))).thenReturn(usuario);

        mockMvc.perform(put("/api/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("juan@ejemplo.com"));
    }

    @Test
    void actualizarUsuario_noEncontrado_retorna404() throws Exception {
        when(usuarioService.actualizar(eq(99L), any(Usuario.class)))
                .thenThrow(new ResourceNotFoundException("Usuario no encontrado con ID: 99"));

        mockMvc.perform(put("/api/usuarios/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Usuario no encontrado con ID: 99"));
    }

    @Test
    void actualizarUsuario_datosInvalidos_retorna400() throws Exception {
        String jsonInvalido = """
                {
                  "nombre": "A",
                  "email": "invalido",
                  "password": "x",
                  "rol": "DESCONOCIDO"
                }
                """;

        mockMvc.perform(put("/api/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonInvalido))
                .andExpect(status().isBadRequest());
    }

    // ==========================================
    // DELETE /api/usuarios/{id}
    // ==========================================

    @Test
    void eliminarUsuario_existe_retorna204() throws Exception {
        doNothing().when(usuarioService).eliminar(1L);

        mockMvc.perform(delete("/api/usuarios/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void eliminarUsuario_noEncontrado_retorna404() throws Exception {
        doThrow(new ResourceNotFoundException("Usuario no encontrado con ID: 99"))
                .when(usuarioService).eliminar(99L);

        mockMvc.perform(delete("/api/usuarios/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Usuario no encontrado con ID: 99"));
    }

    // ==========================================
    // POST /api/usuarios/login
    // ==========================================

    @Test
    void loginUsuario_credencialesValidas_retorna200() throws Exception {
        Map<String, Object> loginResponse = new HashMap<>();
        loginResponse.put("usuario", usuario);
        loginResponse.put("mensaje", "Login exitoso");
        loginResponse.put("token", "token_temporal_1");

        when(usuarioService.login("juan@ejemplo.com", "password123")).thenReturn(loginResponse);

        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("email", "juan@ejemplo.com");
        loginRequest.put("password", "password123");

        mockMvc.perform(post("/api/usuarios/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Login exitoso"))
                .andExpect(jsonPath("$.token").value("token_temporal_1"));
    }

    @Test
    void loginUsuario_usuarioInactivo_retorna404() throws Exception {
        when(usuarioService.login(any(), any()))
                .thenThrow(new ResourceNotFoundException("Credenciales inválidas"));

        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("email", "inactivo@ejemplo.com");
        loginRequest.put("password", "password123");

        mockMvc.perform(post("/api/usuarios/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Credenciales inválidas"));
    }

    @Test
    void loginUsuario_passwordIncorrecto_retorna500() throws Exception {
        // IllegalArgumentException → capturado por @ExceptionHandler(Exception.class) → 500
        when(usuarioService.login(any(), any()))
                .thenThrow(new IllegalArgumentException("Credenciales inválidas"));

        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("email", "juan@ejemplo.com");
        loginRequest.put("password", "wrong");

        mockMvc.perform(post("/api/usuarios/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Error interno del servidor"));
    }
}
