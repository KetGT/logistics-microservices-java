package ar.edu.utn.frc.rutas.infraestructure;

import ar.edu.utn.frc.rutas.domain.dto.DistanciaDTO; // Importa el DTO
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder; // Herramienta para construir URLs

@Component
@RequiredArgsConstructor
public class DistanciasClient {

    // 1. El 'mensajero' que hace la llamada HTTP
    private final RestTemplate restTemplate;

    // 2. La URL del servicio al que vamos a llamar
    // (La lee de application.properties)
    @Value("${distancias.base-url:http://localhost:8081}") 
    private String baseUrl;

    /**
     * Llama al distancias-service para obtener los KM y tiempo
     * entre dos puntos.
     */
    public DistanciaDTO getDistancia(double latOrigen, double lonOrigen, 
                                     double latDestino, double lonDestino) {
        
        // 3. Construye la URL completa con los parámetros
        // ej: http://localhost:8081/api/distancia?latOrigen=...&lonOrigen=...
        String url = UriComponentsBuilder.fromUriString(baseUrl + "/api/distancia")
                .queryParam("latOrigen", latOrigen)
                .queryParam("lonOrigen", lonOrigen)
                .queryParam("latDestino", latDestino)
                .queryParam("lonDestino", lonDestino)
                .toUriString();

        // 4. Llama al servicio y convierte la respuesta JSON
        //    automáticamente en un objeto DistanciaDTO
        try {
            return restTemplate.getForObject(url, DistanciaDTO.class);
        } catch (Exception e) {
            // Manejo de error si el distancias-service está caído o falla
            throw new RuntimeException("No se pudo calcular la distancia: " + e.getMessage());
        }
    }
}