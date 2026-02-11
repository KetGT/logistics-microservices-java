package ar.edu.utn.frc.tarifas.domain;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class ConfiguracionGeneral {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Precio actual del litro de combustible [cite: 103]
    private Double precioLitroCombustible; 
    
    // Cargo fijo por gestión [cite: 142]
    private Double cargoGestion; 
    
    private Double consumoCombustiblePromedioGeneral;
}