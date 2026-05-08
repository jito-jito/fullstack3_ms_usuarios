package com.usuarios.usuarios.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ResourceNotFoundExceptionTest {

    @Test
    void constructor_estableceMensajeCorrectamente() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Usuario no encontrado con ID: 1");

        assertThat(ex.getMessage()).isEqualTo("Usuario no encontrado con ID: 1");
    }

    @Test
    void esSubclaseDeRuntimeException() {
        ResourceNotFoundException ex = new ResourceNotFoundException("test");

        assertThat(ex).isInstanceOf(RuntimeException.class);
    }

    @Test
    void lanzarExcepcion_propagaMensaje() {
        assertThatThrownBy(() -> {
            throw new ResourceNotFoundException("Recurso no encontrado");
        })
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Recurso no encontrado");
    }
}
