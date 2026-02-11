// En: tarifas-service/src/main/java/ar/edu/utn/frc/tarifas/domain/dto/CalcularCostoRealRequestDTO.java

package ar.edu.utn.frc.tarifas.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CalcularCostoRealRequestDTO {
    
    // --- Datos que debe enviar 'rutas-service' ---
    
    private Double distanciaEnKm;
    
    // Del Camion.java
    private Double costoPorKm; 
    private Double consumoCombustiblePorKm; 
    
    // Del Deposito.java
    private Double costoEstadiaDiario; 
    
    // Calculado por 'rutas-service' (Ej: fechaFin - fechaInicio)
    private Integer diasDeEstadia;
}