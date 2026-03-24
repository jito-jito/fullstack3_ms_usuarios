package com.usuarios.usuarios.exception;

/**
 * Excepción personalizada que se lanza cuando un recurso no es encontrado.
 * En este caso, cuando un libro no existe en la base de datos.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String mensaje) {
        super(mensaje);
    }
}
