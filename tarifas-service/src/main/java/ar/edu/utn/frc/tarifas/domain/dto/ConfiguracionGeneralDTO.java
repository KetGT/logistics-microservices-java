// En: tarifas-service/src/main/java/ar/edu/utn/frc/tarifas/domain/dto/ConfiguracionGeneralDTO.java

package ar.edu.utn.frc.tarifas.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ConfiguracionGeneralDTO {
    
    // No incluimos el ID, ya que siempre será la misma
    private Double precioLitroCombustible; 
    private Double cargoGestion; 
    private Double consumoCombustiblePromedioGeneral;
}
