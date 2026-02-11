Sistema de Microservicios para Logística y Transporte 🚛🌐





📖 Descripción Funcional del Sistema

Este sistema simula el backend de una empresa de logística especializada en el traslado terrestre de contenedores. A diferencia de los sistemas de transporte convencionales, aquí el objeto de transporte es el contenedor mismo, lo que requiere una gestión de dimensiones, pesos y capacidades de los mismos.

🛠️ ¿Cómo funciona el ecosistema?

El flujo operativo se divide en tres pilares fundamentales integrados a través de microservicios:

Gestión de Solicitudes y Clientes:

Los clientes pueden registrar pedidos de traslado indicando origen y destino mediante geolocalización (latitud/longitud).

El sistema permite el seguimiento en tiempo real del estado del contenedor (pendiente de retiro, en transito, en deposito, entregado)

Inteligencia Logística y Rutas:


Cálculo de Rutas Tentativas: El sistema determina automáticamente la hoja de ruta, calculando tramos entre el origen, los depósitos intermedios y el destino final.


Integración con Google Maps API: Se utiliza para obtener distancias reales y calcular tiempos estimados de entrega con precisión métrica.


Optimización de Carga: Los administradores asignan camiones basándose en restricciones estrictas: ningún camión puede superar su capacidad máxima de peso o volumen.

Administración y Tarifación:


Configuración de Depósitos: Gestión de puntos de almacenamiento temporal con costos de estadía diarios.


Motor de Costos: El sistema calcula la tarifa final combinando costos fijos de gestión, consumo de combustible por kilómetro (según el camión específico) y días de estadía en depósitos.

🏗️ Arquitectura del Sistema

El sistema se compone de 5 microservicios coordinados y securizados:

API Gateway: Punto de entrada único que gestiona el enrutamiento y la seguridad perimetral (Spring Cloud Gateway).

Servicio de Solicitudes: Gestión de pedidos de transporte y estados de carga.

Servicio de Rutas: Optimización de trayectos y gestión de depósitos.

Servicio de Distancias: Integración con la API de Google Maps para cálculos precisos de trayectos.

Servicio de Tarifas: Motor de cálculo de costos basado en volumen y distancia.

🛡️ Seguridad e Infraestructura

Identity & Access Management: Implementación de Keycloak para la gestión de identidades y autenticación mediante OAuth2 y JWT (JSON Web Tokens).

Persistencia de Datos: Arquitectura de bases de datos independientes por servicio mediante PostgreSQL.

Containerización: Orquestación completa mediante Docker Compose, con redes virtuales privadas para comunicación interna y healthchecks para estabilidad del sistema.

🛠️ Stack Tecnológico

Lenguajes: Java 21 (Servicios de dominio) y Java 17 (Gateway).

Frameworks: Spring Boot 3.x, Spring Data JPA, Spring Security OAuth2.

Base de Datos: PostgreSQL 16 (Alpine).

Documentación: Swagger / OpenAPI 3 para cada microservicio.

🚀 Cómo ejecutar el ecosistema

Clonar el repositorio.

Asegurarse de tener Docker y Docker Compose instalados.

Levantar toda la infraestructura con un solo comando:

Bash
docker-compose up --build

Acceder a la consola de Keycloak en http://localhost:8888 para configurar el reino y los clientes.
