package com.usuarios.usuarios.exception;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ErrorDetailsTest {

    @Test
    void constructor_inicializaTodosLosCampos() {
        LocalDateTime timestamp = LocalDateTime.of(2026, 5, 7, 10, 30, 0);
        String message = "Usuario no encontrado";
        String details = "uri=/api/usuarios/99";

        ErrorDetails errorDetails = new ErrorDetails(timestamp, message, details);

        assertThat(errorDetails.getTimestamp()).isEqualTo(timestamp);
        assertThat(errorDetails.getMessage()).isEqualTo(message);
        assertThat(errorDetails.getDetails()).isEqualTo(details);
    }

    @Test
    void getTimestamp_retornaValorCorrecto() {
        LocalDateTime ahora = LocalDateTime.now();
        ErrorDetails errorDetails = new ErrorDetails(ahora, "msg", "details");

        assertThat(errorDetails.getTimestamp()).isEqualTo(ahora);
    }

    @Test
    void getMessage_retornaValorCorrecto() {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), "Error de prueba", "uri=/test");

        assertThat(errorDetails.getMessage()).isEqualTo("Error de prueba");
    }

    @Test
    void getDetails_retornaValorCorrecto() {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), "msg", "uri=/api/test");

        assertThat(errorDetails.getDetails()).isEqualTo("uri=/api/test");
    }
}
