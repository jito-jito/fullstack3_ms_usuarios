package com.usuarios.usuarios.service;

import com.usuarios.usuarios.exception.ResourceNotFoundException;
import com.usuarios.usuarios.model.Usuario;
import com.usuarios.usuarios.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario("Juan Pérez", "juan@ejemplo.com", "password123", "USER");
        usuario.setId(1L);
    }

    // ==========================================
    // guardar()
    // ==========================================

    @Test
    void guardar_exitoso_retornaUsuarioGuardado() {
        when(usuarioRepository.existsByEmail("juan@ejemplo.com")).thenReturn(false);
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario resultado = usuarioService.guardar(usuario);

        assertThat(resultado).isEqualTo(usuario);
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void guardar_emailDuplicado_lanzaIllegalArgumentException() {
        when(usuarioRepository.existsByEmail("juan@ejemplo.com")).thenReturn(true);

        assertThatThrownBy(() -> usuarioService.guardar(usuario))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El email ya está registrado: juan@ejemplo.com");

        verify(usuarioRepository, never()).save(any());
    }

    // ==========================================
    // actualizar()
    // ==========================================

    @Test
    void actualizar_mismoEmail_exitoso() {
        Usuario actualizado = new Usuario("Juan Actualizado", "juan@ejemplo.com", "newpass123", "ADMIN");
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario resultado = usuarioService.actualizar(1L, actualizado);

        assertThat(resultado).isNotNull();
        verify(usuarioRepository, never()).existsByEmail(any());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void actualizar_nuevoEmailDisponible_exitoso() {
        Usuario actualizado = new Usuario("Juan Actualizado", "nuevo@ejemplo.com", "newpass123", "ADMIN");
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.existsByEmail("nuevo@ejemplo.com")).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario resultado = usuarioService.actualizar(1L, actualizado);

        assertThat(resultado).isNotNull();
        assertThat(usuario.getNombre()).isEqualTo("Juan Actualizado");
        assertThat(usuario.getRol()).isEqualTo("ADMIN");
    }

    @Test
    void actualizar_usuarioNoEncontrado_lanzaResourceNotFoundException() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.actualizar(99L, usuario))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Usuario no encontrado con ID: 99");
    }

    @Test
    void actualizar_nuevoEmailEnUso_lanzaIllegalArgumentException() {
        Usuario actualizado = new Usuario("Juan Actualizado", "ocupado@ejemplo.com", "newpass123", "USER");
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.existsByEmail("ocupado@ejemplo.com")).thenReturn(true);

        assertThatThrownBy(() -> usuarioService.actualizar(1L, actualizado))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El email ya está registrado: ocupado@ejemplo.com");

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void actualizar_passwordNulo_noActualizaPassword() {
        Usuario actualizado = new Usuario("Juan Actualizado", "juan@ejemplo.com", null, "USER");
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        usuarioService.actualizar(1L, actualizado);

        assertThat(usuario.getPassword()).isEqualTo("password123");
    }

    @Test
    void actualizar_passwordVacio_noActualizaPassword() {
        Usuario actualizado = new Usuario("Juan Actualizado", "juan@ejemplo.com", "", "USER");
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        usuarioService.actualizar(1L, actualizado);

        assertThat(usuario.getPassword()).isEqualTo("password123");
    }

    // ==========================================
    // eliminar()
    // ==========================================

    @Test
    void eliminar_exitoso_cambiaActivoAFalse() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        usuarioService.eliminar(1L);

        assertThat(usuario.getActivo()).isFalse();
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void eliminar_usuarioNoEncontrado_lanzaResourceNotFoundException() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.eliminar(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Usuario no encontrado con ID: 99");
    }

    // ==========================================
    // login()
    // ==========================================

    @Test
    void login_exitoso_retornaMapa() {
        when(usuarioRepository.findByEmailAndActivo("juan@ejemplo.com", true))
                .thenReturn(Optional.of(usuario));

        Map<String, Object> resultado = usuarioService.login("juan@ejemplo.com", "password123");

        assertThat(resultado).containsKey("usuario");
        assertThat(resultado).containsKey("token");
        assertThat(resultado.get("mensaje")).isEqualTo("Login exitoso");
        assertThat(resultado.get("token")).isEqualTo("token_temporal_1");
        assertThat(resultado.get("usuario")).isEqualTo(usuario);
    }

    @Test
    void login_usuarioNoEncontradoOInactivo_lanzaResourceNotFoundException() {
        when(usuarioRepository.findByEmailAndActivo("noexiste@ejemplo.com", true))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.login("noexiste@ejemplo.com", "password123"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Credenciales inválidas");
    }

    @Test
    void login_passwordIncorrecto_lanzaIllegalArgumentException() {
        when(usuarioRepository.findByEmailAndActivo("juan@ejemplo.com", true))
                .thenReturn(Optional.of(usuario));

        assertThatThrownBy(() -> usuarioService.login("juan@ejemplo.com", "wrongpass"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Credenciales inválidas");
    }
}
