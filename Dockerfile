# Etapa 1: Construcción (Build)
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copiar el archivo de dependencias primero y descargar las dependencias
# Esto permite aprovechar la caché de Docker para acelerar las builds futuras
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar el código fuente y compilar el proyecto
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Ejecución (Run)
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copiar el JAR generado desde la etapa de construcción anterior
COPY --from=build /app/target/biblioteca-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto configurado en el microservicio (ver application.properties)
EXPOSE 8081

# Punto de entrada para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
