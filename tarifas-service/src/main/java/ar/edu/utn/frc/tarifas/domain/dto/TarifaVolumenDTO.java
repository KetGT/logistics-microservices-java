// En: tarifas-service/src/main/java/ar/edu/utn/frc/tarifas/domain/dto/TarifaVolumenDTO.java

package ar.edu.utn.frc.tarifas.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TarifaVolumenDTO {
    
    private Long id;
    private Double volumenMin;
    private Double volumenMax;
    private Double costoPorKmBase;
}