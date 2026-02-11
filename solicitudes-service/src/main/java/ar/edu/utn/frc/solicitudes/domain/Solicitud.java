package ar.edu.utn.frc.solicitudes.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data; // Incluye @Getter, @Setter, @ToString, @EqualsAndHashCode
import lombok.NoArgsConstructor; // Crea el constructor sin argumentos
import ar.edu.utn.frc.solicitudes.domain.tipo.EstadoSolicitud;

@Entity
@Data // getters y setters con lombok
@NoArgsConstructor 

public class Solicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "solicitud_id")
    private Long id;

    private Double costoEstimado;

    private String tiempoEstimado;

    private Double costoFinal;

    private String tiempoFinal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoSolicitud estadoSolicitud = EstadoSolicitud.BORRADOR; // Valor por defecto al crear


    @Column(name = "ruta_id", nullable = true) // 'nullable = true' es clave
    private String rutaId;

    @ManyToOne 
    @JoinColumn(name = "contenedor_id")
    private Contenedor contenedor;


    @Column(nullable = false)
    private String origenDireccion;
    @Column(nullable = false)
    private Double origenLat;
    @Column(nullable = false)
    private Double origenLon;

    @Column(nullable = false)
    private String destinoDireccion;
    @Column(nullable = false)
    private Double destinoLat;
    @Column(nullable = false)
    private Double destinoLon;

    
}
