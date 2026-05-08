# Reporte de Pruebas Unitarias — ms-usuarios

**Fecha:** 7 de mayo de 2026  
**Herramientas:** JUnit 5 · Mockito · MockMvc · JaCoCo 0.8.11  
**Resultado general:** ✅ 37 tests · 0 fallos · 0 errores

---

## Cobertura JaCoCo

| Paquete | Instrucciones | Ramas |
|---------|:---:|:---:|
| `service` | 100% | 100% |
| `controller` | 100% | n/a |
| `exception` | 100% | n/a |
| `config` | 100% | n/a |
| `model` | 89% | n/a |
| **Total** | **96%** | **100%** |

> La clase `UsuarioApplication` (método `main`) aporta baja cobertura por diseño; SonarQube la excluye habitualmente mediante la regla de exclusión de clases de arranque.

---

## Archivos de prueba creados

### 1. `UsuarioServiceTest` — 13 tests
Prueba la lógica de negocio con Mockito (`@ExtendWith(MockitoExtension.class)`).

| Test | Escenario |
|------|-----------|
| `guardar_exitoso_retornaUsuarioGuardado` | Email libre → se persiste correctamente |
| `guardar_emailDuplicado_lanzaIllegalArgumentException` | Email ya registrado → excepción |
| `actualizar_mismoEmail_exitoso` | Actualiza sin cambiar email → no verifica duplicado |
| `actualizar_nuevoEmailDisponible_exitoso` | Cambia email libre → actualiza todos los campos |
| `actualizar_usuarioNoEncontrado_lanzaResourceNotFoundException` | ID inexistente → 404 |
| `actualizar_nuevoEmailEnUso_lanzaIllegalArgumentException` | Nuevo email ocupado → excepción |
| `actualizar_passwordNulo_noActualizaPassword` | Password null → mantiene el anterior |
| `actualizar_passwordVacio_noActualizaPassword` | Password vacío → mantiene el anterior |
| `eliminar_exitoso_cambiaActivoAFalse` | Soft delete → `activo = false` |
| `eliminar_usuarioNoEncontrado_lanzaResourceNotFoundException` | ID inexistente → 404 |
| `login_exitoso_retornaMapa` | Credenciales correctas → retorna usuario, token y mensaje |
| `login_usuarioNoEncontradoOInactivo_lanzaResourceNotFoundException` | Usuario inactivo/inexistente → excepción |
| `login_passwordIncorrecto_lanzaIllegalArgumentException` | Password erróneo → excepción |

---

### 2. `UsuarioControllerTest` — 11 tests
Prueba los endpoints HTTP con `@WebMvcTest` + `MockMvc`.

| Test | Endpoint | HTTP esperado |
|------|----------|:---:|
| `crearUsuario_datosValidos_retorna201` | `POST /api/usuarios` | 201 |
| `crearUsuario_datosInvalidos_retorna400` | `POST /api/usuarios` | 400 |
| `crearUsuario_emailDuplicado_retorna500` | `POST /api/usuarios` | 500 |
| `actualizarUsuario_datosValidos_retorna200` | `PUT /api/usuarios/1` | 200 |
| `actualizarUsuario_noEncontrado_retorna404` | `PUT /api/usuarios/99` | 404 |
| `actualizarUsuario_datosInvalidos_retorna400` | `PUT /api/usuarios/1` | 400 |
| `eliminarUsuario_existe_retorna204` | `DELETE /api/usuarios/1` | 204 |
| `eliminarUsuario_noEncontrado_retorna404` | `DELETE /api/usuarios/99` | 404 |
| `loginUsuario_credencialesValidas_retorna200` | `POST /api/usuarios/login` | 200 |
| `loginUsuario_usuarioInactivo_retorna404` | `POST /api/usuarios/login` | 404 |
| `loginUsuario_passwordIncorrecto_retorna500` | `POST /api/usuarios/login` | 500 |

---

### 3. `UsuarioModelTest` — 5 tests
Prueba el modelo POJO directamente (sin Spring context).

| Test | Escenario |
|------|-----------|
| `constructorConParametros_inicializaCamposCorrectamente` | Constructor con 4 args asigna campos y fechas |
| `constructorVacio_creaInstanciaValida` | Constructor vacío sin NullPointerException |
| `settersYGetters_funcionanCorrectamente` | Todos los setters/getters devuelven el valor asignado |
| `activoPorDefecto_esTrue` | Campo `activo` es `true` al crear |
| `setActivo_cambiaEstadoCorrectamente` | Toggle de estado activo/inactivo |

---

### 4. `ErrorDetailsTest` — 4 tests
Prueba la clase de respuesta de error.

| Test | Escenario |
|------|-----------|
| `constructor_inicializaTodosLosCampos` | Los 3 campos se asignan correctamente |
| `getTimestamp_retornaValorCorrecto` | Getter timestamp |
| `getMessage_retornaValorCorrecto` | Getter message |
| `getDetails_retornaValorCorrecto` | Getter details |

---

### 5. `ResourceNotFoundExceptionTest` — 3 tests

| Test | Escenario |
|------|-----------|
| `constructor_estableceMensajeCorrectamente` | Mensaje propagado desde constructor |
| `esSubclaseDeRuntimeException` | Hereda de `RuntimeException` |
| `lanzarExcepcion_propagaMensaje` | Se puede lanzar y capturar correctamente |

---

### 6. `UsuarioApplicationTests` — 1 test (preexistente)

| Test | Escenario |
|------|-----------|
| `contextLoads` | El contexto de Spring Boot arranca sin errores con perfil `test` (H2) |

---

## Configuración de calidad (JaCoCo)

El `pom.xml` fue configurado con un **gate de cobertura mínima del 80%** en líneas:

```xml
<counter>LINE</counter>
<value>COVEREDRATIO</value>
<minimum>0.80</minimum>
```

El reporte HTML se genera en `target/site/jacoco/index.html` al ejecutar:

```bash
./mvnw clean verify
```
