package ar.edu.utn.frc.rutas.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Entity
@Data
@NoArgsConstructor
public class Camion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "camion_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String dominio; // Patente o identificador único

    @Column(nullable = false)
    private String nombreTransportista;

    @Column(nullable = false)
    private String telefono;

    @Column(name = "capacidad_peso", nullable = false)
    private double capacidadPeso;

    @Column(name = "capacidad_volumen", nullable = false)
    private double capacidadVolumen;

    // 'true' si está disponible, 'false' si está en un viaje o en mantenimiento
    @Column(nullable = false)
    private boolean disponible = true;

    private Double consumoCombustiblePromedio;

    private Double costoPorKm;
}