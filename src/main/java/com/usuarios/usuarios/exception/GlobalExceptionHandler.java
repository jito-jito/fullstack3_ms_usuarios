package com.usuarios.usuarios.exception;

// ===============================
// IMPORTACIONES
// ===============================

// ResponseEntity permite construir respuestas HTTP completas,
// incluyendo cuerpo, estado y encabezados.
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

// FieldError permite obtener los mensajes de error de cada campo validado.
import org.springframework.validation.FieldError;

// MethodArgumentNotValidException se lanza cuando falla una validación
// activada con @Valid en el controlador.
import org.springframework.web.bind.MethodArgumentNotValidException;

// @ControllerAdvice permite manejar excepciones de forma global
// para todos los controladores de la aplicación.
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// WebRequest permite obtener información de la solicitud HTTP,
// como la URI donde ocurrió el error.
import org.springframework.web.context.request.WebRequest;

// LocalDateTime se utiliza para registrar la fecha y hora exacta del error.
import java.time.LocalDateTime;

// List y Collectors se usan para construir una lista de mensajes
// provenientes de los errores de validación.
import java.util.List;
import java.util.stream.Collectors;

/**
 * ===============================
 * CLASE: GlobalExceptionHandler
 * ===============================
 *
 * Permite manejar todas las excepciones de la aplicación
 * de forma centralizada.
 *
 * Semana 1:
 * - No existía manejo global de errores.
 *
 * Semana 2:
 * - Se implementa un manejador global para controlar
 *   las respuestas ante errores.
 * - Se agregan respuestas diferenciadas para:
 *      • recursos no encontrados (404)
 *      • errores de validación (400)
 *      • errores internos no controlados (500)
 *
 * Beneficio principal:
 * - El cliente recibe respuestas más claras, consistentes y profesionales.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    // ==========================================================
    // MANEJO DE RECURSO NO ENCONTRADO
    // ==========================================================

    /**
     * Maneja la excepción cuando un recurso no es encontrado.
     *
     * Ejemplo:
     * - Buscar un libro por ID y que no exista en la base de datos.
     *
     * Retorna:
     * - 404 NOT FOUND
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> manejarRecursoNoEncontrado(
            ResourceNotFoundException ex,
            WebRequest request) {

        // Se construye un objeto de error con:
        // - fecha y hora actual
        // - mensaje específico de la excepción
        // - detalle de la solicitud donde ocurrió el error
        ErrorDetails error = new ErrorDetails(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false)
        );

        // Se responde con estado HTTP 404.
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // ==========================================================
    // MANEJO DE ERRORES DE VALIDACIÓN
    // ==========================================================

    /**
     * Maneja los errores producidos por las validaciones de @Valid.
     *
     * Ejemplo:
     * - título vacío
     * - año fuera del rango permitido
     * - género en blanco
     *
     * Retorna:
     * - 400 BAD REQUEST
     *
     * Importancia en Semana 2:
     * - Aquí se corrige un punto clave del proyecto:
     *   cuando el cliente envía datos incorrectos, NO debe responder 500,
     *   porque el error no es del servidor, sino del request enviado.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> manejarErroresDeValidacion(
            MethodArgumentNotValidException ex,
            WebRequest request) {

        // Se obtienen todos los errores de validación generados
        // sobre los campos del objeto recibido.
        List<String> errores = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        // Se construye un mensaje único con todos los errores encontrados.
        String mensajeErrores = "Validación fallida: " + String.join(", ", errores);

        // Se construye la respuesta estructurada de error.
        ErrorDetails error = new ErrorDetails(
                LocalDateTime.now(),
                mensajeErrores,
                request.getDescription(false)
        );

        // Se responde con estado HTTP 400.
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // ==========================================================
    // MANEJO GLOBAL DE EXCEPCIONES
    // ==========================================================

    /**
     * Maneja cualquier excepción no controlada específicamente.
     *
     * Ejemplo:
     * - errores inesperados del sistema
     * - fallos no contemplados por otros manejadores
     *
     * Retorna:
     * - 500 INTERNAL SERVER ERROR
     *
     * Nota:
     * - Este manejador debe quedar al final como respaldo general.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> manejarExcepcionGlobal(
            Exception ex,
            WebRequest request) {

        // Se construye una respuesta genérica para errores internos.
        ErrorDetails error = new ErrorDetails(
                LocalDateTime.now(),
                "Error interno del servidor",
                request.getDescription(false)
        );

        // Se responde con estado HTTP 500.
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}