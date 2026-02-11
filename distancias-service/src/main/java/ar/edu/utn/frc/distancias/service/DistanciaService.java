/*
VIEJO

package ar.edu.utn.frc.distancias.service;

import ar.edu.utn.frc.distancias.domain.dto.DistanciaResponseDTO;
import org.springframework.stereotype.Service;

@Service
public class DistanciaService {

    // Radio de la Tierra en kilómetros
    private static final double RADIO_TIERRA_KM = 6371.0;

    
    public DistanciaResponseDTO calcularDistancia(double lat1, double lon1, double lat2, double lon2) {

        // Convertir grados a radianes
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        // Diferencias
        double dLat = lat2Rad - lat1Rad;
        double dLon = lon2Rad - lon1Rad;

        // Fórmula de Haversine
        double a = Math.pow(Math.sin(dLat / 2), 2) +
                   Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                   Math.pow(Math.sin(dLon / 2), 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distancia = RADIO_TIERRA_KM * c;

        // Retorna el DTO de respuesta
        return new DistanciaResponseDTO(distancia, "km");
    }
}*/