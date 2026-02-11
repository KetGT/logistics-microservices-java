-- Script de inicialización de bases de datos para los microservicios
-- Ejecutar este script en PostgreSQL después de iniciar el contenedor

-- Crear base de datos para rutas-service
CREATE DATABASE rutas_db;

-- Crear base de datos para solicitudes-service
CREATE DATABASE solicitudes_db;

-- Crear base de datos para tarifas-service
CREATE DATABASE tarifas_db;

-- Opcional: Otorgar permisos (si usas un usuario diferente)
-- GRANT ALL PRIVILEGES ON DATABASE rutas_db TO postgres;
-- GRANT ALL PRIVILEGES ON DATABASE solicitudes_db TO postgres;
-- GRANT ALL PRIVILEGES ON DATABASE tarifas_db TO postgres;

