# --- ETAPA DE CONSTRUCCIÓN ---
FROM eclipse-temurin:17-jdk-alpine AS build

# Directorio de trabajo
WORKDIR /app

# Copiamos los archivos de configuración de Maven y el código fuente
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# Damos permisos de ejecución al wrapper de Maven y construimos el proyecto
# Saltamos los tests para acelerar el despliegue
RUN chmod +x ./mvnw
RUN ./mvnw clean package -DskipTests

# --- ETAPA DE RUNTIME ---
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copiamos solo el JAR generado desde la etapa de construcción
# El nombre debe coincidir con artifactId-version.jar de tu pom.xml
COPY --from=build /app/target/gestion-parqueadero-0.0.1-SNAPSHOT.jar app.jar

# Exponemos el puerto
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
