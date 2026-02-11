package ar.edu.utn.frc.rutas.domain;

import ar.edu.utn.frc.rutas.domain.tipo.EstadoTramo;
import ar.edu.utn.frc.rutas.domain.tipo.TipoTramo;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Entity
@Data
@NoArgsConstructor
public class Tramo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tramo_id")
    private Long id;

    // --- TIPO y ESTADO ---
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoTramo tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoTramo estado = EstadoTramo.ESTIMADO; // Valor por defecto al crear

    // --- PUNTOS DE ORIGEN (Puede ser un Depósito o una dirección externa) ---
    
    // NULO si es 'ORIGEN_DEPOSITO' u 'ORIGEN_DESTINO'
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "origen_deposito_id")
    private Deposito origenDeposito; 

    // NULO si es 'DEPOSITO_DEPOSITO' o 'DEPOSITO_DESTINO'
    private String origenExternoDireccion; 
    private String origenExternoLat;
    private String origenExternoLon;

    // --- PUNTOS DE DESTINO (Puede ser un Depósito o una dirección externa) ---
    
    // NULO si es 'DEPOSITO_DESTINO' u 'ORIGEN_DESTINO'
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destino_deposito_id")
    private Deposito destinoDeposito; 

    // NULO si es 'ORIGEN_DEPOSITO' o 'DEPOSITO_DEPOSITO'
    private String destinoExternoDireccion; 
    private String destinoExternoLat;
    private String destinoExternoLon;

    // --- FECHAS ---
    // Se setea cuando estado = ASIGNADO o INICIADO
    @Column(nullable = true) 
    private LocalDateTime fechaHoraInicio;

    // Se setea cuando estado = FINALIZADO
    @Column(nullable = true) 
    private LocalDateTime fechaHoraFin;

    // --- RECURSO ASIGNADO ---
    
    // NULO si estado = ESTIMADO
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "camion_id") 
    private Camion camion;
}