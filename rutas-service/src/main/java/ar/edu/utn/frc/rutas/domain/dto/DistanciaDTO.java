package ar.edu.utn.frc.rutas.domain.dto;

import lombok.Data;

@Data
public class DistanciaDTO {
    // Estos campos deben coincidir con el JSON que devuelve
    // tu distancias-service (el que hicimos con Google Maps)
    private Double kilometros;
    private String tiempoAprox;
}