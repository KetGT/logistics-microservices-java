package ar.edu.utn.frc.rutas.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Entity
@Data
@NoArgsConstructor
public class Deposito {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deposito_id")
    private Long id;

    @Column(nullable = false)
    private String nombre;
    
    // Dirección textual para referencia humana (Ej: "Puerto de Buenos Aires")
    @Column(nullable = false)
    private String direccionTextual;

    // Coordenadas precisas para el cálculo de rutas
    @Column(nullable = false)
    private Double latitud;

    @Column(nullable = false)
    private Double longitud;

    private Double costoEstadiaDiario;
}
