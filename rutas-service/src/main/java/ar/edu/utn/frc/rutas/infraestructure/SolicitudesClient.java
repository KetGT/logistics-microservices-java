package ar.edu.utn.frc.rutas.infraestructure;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
@Component
@RequiredArgsConstructor
public class SolicitudesClient {

    private final RestTemplate restTemplate;

    // Apunta al puerto de tu solicitudes-service
    @Value("${solicitudes.base-url:http://localhost:8080}") 
    private String baseUrl;

    /**
     * DTO para recibir solo los datos que nos importan del contenedor.
     */
    @Data
    public static class ContenedorDTO {
        private Long id;
        private Double peso;
        private Double volumen;
    }

    /**
     * Llama a solicitudes-service para obtener el contenedor asociado a una solicitud.
     * * NOTA: Esto asume que tienes un endpoint en Solicitudes-service en:
     * GET /api/solicitudes/{id}/contenedor
     */
    public ContenedorDTO getContenedorDeSolicitud(Long solicitudId) {
        try {
            String url = baseUrl + "/api/solicitudes/" + solicitudId + "/contenedor";
            return restTemplate.getForObject(url, ContenedorDTO.class);
        } catch (Exception e) {
            // Maneja el error como prefieras (lanzar excepción, loggear, etc.)
            throw new RuntimeException("No se pudo obtener el contenedor para la solicitud: " + solicitudId, e);
        }
    }

    // --- NUEVO DTO ---
    @Data
    public static class SolicitudDetallesDTO {
        private Double origenLat;
        private Double origenLon;
        private Double destinoLat;
        private Double destinoLon;
        private ContenedorDTO contenedor;
    }
    
    // --- NUEVO MÉTODO ---
    public SolicitudDetallesDTO getDetallesDeSolicitud(Long solicitudId) {
        try {
            // Asumimos que creas este endpoint en solicitudes-service
            String url = baseUrl + "/api/solicitudes/" + solicitudId; 
            return restTemplate.getForObject(url, SolicitudDetallesDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo obtener la solicitud: " + solicitudId, e);
        }
    }
}