package com.usuarios.usuarios.exception;

import java.time.LocalDateTime;

/**
 * ===============================
 * CLASE: ErrorDetails
 * ===============================
 *
 * Esta clase define la estructura del JSON que se devolverá
 * cuando ocurra un error en la aplicación.
 *
 * Semana 1:
 * - No existía manejo estructurado de errores.
 *
 * Semana 2:
 * - Se implementa una respuesta estándar para errores,
 *   lo que permite al cliente entender claramente qué ocurrió.
 */
public class ErrorDetails {

    // Fecha y hora en que ocurrió el error
    private LocalDateTime timestamp;

    // Mensaje del error (ej: "Libro no encontrado")
    private String message;

    // Detalle adicional (generalmente la URL o contexto)
    private String details;

    /**
     * Constructor principal.
     */
    public ErrorDetails(LocalDateTime timestamp, String message, String details) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

    // ===============================
    // GETTERS
    // ===============================

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }
}
