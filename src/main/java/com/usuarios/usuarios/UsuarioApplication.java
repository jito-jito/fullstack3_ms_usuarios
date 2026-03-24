package com.usuarios.usuarios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ===============================
 * MICROSERVICIO DE USUARIOS
 * ===============================
 * 
 * Clase principal del microservicio de gestión de usuarios.
 * Proporciona funcionalidades de:
 * - Gestión CRUD de usuarios
 * - Autenticación y login
 * - Control de roles y permisos
 * - Estados activos/inactivos
 * 
 * Puerto: 8081
 * Base URL: /api/usuarios
 */
@SpringBootApplication
public class UsuarioApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsuarioApplication.class, args);
		System.out.println("🚀 Microservicio de Usuarios iniciado en puerto 8081");
		System.out.println("📊 API disponible en: http://localhost:8081/api/usuarios");
	}

}
