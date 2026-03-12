# Usamos una imagen base de Java 17 (coincide con tu pom.xml)
FROM eclipse-temurin:17-jdk-alpine

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos el archivo JAR generado (asegúrate de hacer mvn clean package primero)
# El nombre del jar depende de tu pom.xml (artifactId-version)
COPY target/gestion-parqueadero-0.0.1-SNAPSHOT.jar app.jar

# Exponemos el puerto configurado en application.properties
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
