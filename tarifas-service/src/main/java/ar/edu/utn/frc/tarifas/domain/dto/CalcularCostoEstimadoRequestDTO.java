// En: tarifas-service/src/main/java/ar/edu/utn/frc/tarifas/domain/dto/CalcularCostoEstimadoRequestDTO.java

package ar.edu.utn.frc.tarifas.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CalcularCostoEstimadoRequestDTO {

    private Double distanciaEnKm;
    private Double volumenContenedor;
    
    // Opcional: se podría agregar 'diasDeEstadiaEstimados', 
    // pero lo omitimos para simplificar, tal como sugiere el TPI.
}