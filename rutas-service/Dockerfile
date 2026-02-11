# Usa una imagen base de Java 21
FROM eclipse-temurin:21-jre-jammy

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el archivo .jar de tu proyecto al contenedor
# OJO: Cambia "mi-servicio.jar" por el nombre real de tu JAR
# (Ej: "rutas-service-0.0.1-SNAPSHOT.jar")
COPY target/*.jar app.jar

# Expone el puerto en el que corre tu servicio
EXPOSE 8082

# El comando para arrancar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]