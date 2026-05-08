package com.usuarios.usuarios.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class UsuarioModelTest {

    @Test
    void constructorConParametros_inicializaCamposCorrectamente() {
        Usuario usuario = new Usuario("María González", "maria@ejemplo.com", "pass1234", "ADMIN");

        assertThat(usuario.getNombre()).isEqualTo("María González");
        assertThat(usuario.getEmail()).isEqualTo("maria@ejemplo.com");
        assertThat(usuario.getPassword()).isEqualTo("pass1234");
        assertThat(usuario.getRol()).isEqualTo("ADMIN");
        assertThat(usuario.getActivo()).isTrue();
        assertThat(usuario.getFechaCreacion()).isNotNull();
        assertThat(usuario.getFechaActualizacion()).isNotNull();
    }

    @Test
    void constructorVacio_creaInstanciaValida() {
        Usuario usuario = new Usuario();

        assertThat(usuario).isNotNull();
        assertThat(usuario.getId()).isNull();
        assertThat(usuario.getNombre()).isNull();
    }

    @Test
    void settersYGetters_funcionanCorrectamente() {
        Usuario usuario = new Usuario();
        LocalDateTime ahora = LocalDateTime.now();

        usuario.setId(5L);
        usuario.setNombre("Pedro López");
        usuario.setEmail("pedro@ejemplo.com");
        usuario.setPassword("secreto123");
        usuario.setRol("USER");
        usuario.setActivo(false);
        usuario.setFechaCreacion(ahora);
        usuario.setFechaActualizacion(ahora);

        assertThat(usuario.getId()).isEqualTo(5L);
        assertThat(usuario.getNombre()).isEqualTo("Pedro López");
        assertThat(usuario.getEmail()).isEqualTo("pedro@ejemplo.com");
        assertThat(usuario.getPassword()).isEqualTo("secreto123");
        assertThat(usuario.getRol()).isEqualTo("USER");
        assertThat(usuario.getActivo()).isFalse();
        assertThat(usuario.getFechaCreacion()).isEqualTo(ahora);
        assertThat(usuario.getFechaActualizacion()).isEqualTo(ahora);
    }

    @Test
    void activoPorDefecto_esTrue() {
        Usuario usuario = new Usuario("Test", "test@ejemplo.com", "pass123", "USER");

        assertThat(usuario.getActivo()).isTrue();
    }

    @Test
    void setActivo_cambiaEstadoCorrectamente() {
        Usuario usuario = new Usuario("Test", "test@ejemplo.com", "pass123", "USER");

        usuario.setActivo(false);
        assertThat(usuario.getActivo()).isFalse();

        usuario.setActivo(true);
        assertThat(usuario.getActivo()).isTrue();
    }
}
