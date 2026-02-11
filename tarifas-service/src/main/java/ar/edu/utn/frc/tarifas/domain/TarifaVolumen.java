// En: tarifas-service/src/main/java/ar/edu/utn/frc/tarifas/domain/TarifaVolumen.java

package ar.edu.utn.frc.tarifas.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class TarifaVolumen {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double volumenMin; // Límite inferior (ej: 0)
    private Double volumenMax; // Límite superior (ej: 30)
    
    // Costo base por km para este rango
    private Double costoPorKmBase; 
}