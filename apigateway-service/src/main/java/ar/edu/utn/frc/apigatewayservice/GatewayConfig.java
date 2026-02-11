// Reemplaza tu paquete por este
package ar.edu.utn.frc.apigatewayservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator configurarRutas(RouteLocatorBuilder builder,
                                        @Value("${app.microservicios.solicitudes-uri}") String uriSolicitudes,
                                        @Value("${app.microservicios.distancias-uri}") String uriDistancias,
                                        @Value("${app.microservicios.rutas-uri}") String uriRutas,
                                        @Value("${app.microservicios.tarifas-uri}") String uriTarifas) {
        
        return builder.routes()
            
            // ====== 1. RUTAS DEL API (Tu código original) ======
            
            .route("solicitudes_api", p -> p
                .path("/api/solicitudes/**", "/api/clientes/**", "/api/contenedores/**")
                .uri(uriSolicitudes)
            )
            .route("distancias_api", p -> p
                .path("/api/distancias/**") // <-- OJO: Tu .properties dice /distancias/**
                .uri(uriDistancias)
            )
            .route("rutas_api", p -> p
                .path("/api/rutas/**", "/api/depositos/**", "/api/camiones/**", "/api/tramos/**")
                .uri(uriRutas)
            )
            .route("tarifas_api", p -> p
                .path("/api/tarifas/**")
                .uri(uriTarifas)
            )

            // ====== 2. RUTAS PARA SWAGGER (La parte que faltaba) ======
            // Hacemos que /solicitudes/v3/api-docs vaya a http://solicitudes-service:8080/v3/api-docs

            .route("solicitudes_docs", p -> p
                .path("/solicitudes/v3/api-docs/**")  // Lo que pide el navegador
                .filters(f -> f.stripPrefix(1))     // Quita "/solicitudes"
                .uri(uriSolicitudes)                // Lo manda a http://solicitudes-service:8080/v3/api-docs
            )
            .route("distancias_docs", p -> p
                .path("/distancias/v3/api-docs/**") // Lo que pide el navegador
                .filters(f -> f.stripPrefix(1))     // Quita "/distancias"
                .uri(uriDistancias)                 // Lo manda a http://distancias-service:8081/v3/api-docs
            )
            .route("rutas_docs", p -> p
                .path("/rutas/v3/api-docs/**")      // Lo que pide el navegador
                .filters(f -> f.stripPrefix(1))     // Quita "/rutas"
                .uri(uriRutas)                      // Lo manda a http://rutas-service:8082/v3/api-docs
            )
            .route("tarifas_docs", p -> p
                .path("/tarifas/v3/api-docs/**")    // Lo que pide el navegador
                .filters(f -> f.stripPrefix(1))     // Quita "/tarifas"
                .uri(uriTarifas)                    // Lo manda a http://tarifas-service:8083/v3/api-docs
            )
            
            .build();
    }
}