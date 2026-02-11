package ar.edu.utn.frc.distancias.service;

import ar.edu.utn.frc.distancias.domain.dto.DistanciaDTO;
import ar.edu.utn.frc.distancias.domain.dto.GoogleDistanceMatrixResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class GoogleMapsService {

    private final RestTemplate restTemplate;

    @Value("${google.api.key}")
    private String apiKey;

    public DistanciaDTO getDistancia(double latOrigen, double lonOrigen,
                                     double latDestino, double lonDestino) {
        
        // 1. TRADUCIR los 4 doubles a 2 strings (el "mismatch" que mencionaste)
        String origins = latOrigen + "," + lonOrigen;
        String destinations = latDestino + "," + lonDestino;

        // 2. CONSTRUIR la URL de la API de Google
        String url = String.format(
            "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins=%s&destinations=%s&key=%s",
            origins, destinations, apiKey
        );

        // 3. LLAMAR a la API
        // Mapeamos la respuesta JSON de Google a nuestra clase DTO
        GoogleDistanceMatrixResponse response = restTemplate.getForObject(url, GoogleDistanceMatrixResponse.class);

        // 4. PARSEAR la respuesta compleja de Google
        if (response != null && response.getRows() != null && !response.getRows().isEmpty()
                && response.getRows().get(0).getElements() != null && !response.getRows().get(0).getElements().isEmpty()) {
            
            GoogleDistanceMatrixResponse.Element element = response.getRows().get(0).getElements().get(0);

            if ("OK".equals(element.getStatus())) {
                DistanciaDTO dto = new DistanciaDTO();
                // Google devuelve distancia en metros, la pasamos a KM
                dto.setKilometros(element.getDistance().getValue() / 1000.0);
                dto.setTiempoAprox(element.getDuration().getText());
                return dto;
            }
        }

        // Manejo de error si Google no pudo calcular la ruta
        throw new RuntimeException("No se pudo calcular la distancia con Google Maps.");
    }
}