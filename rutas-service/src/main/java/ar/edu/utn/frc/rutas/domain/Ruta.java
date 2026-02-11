package ar.edu.utn.frc.rutas.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Entity
@Data
@NoArgsConstructor
public class Ruta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ruta_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private Long solicitudId;

    // --- CORRECCIÓN AQUÍ ---
    /**
     * Relación Muchos-a-Muchos:
     * - Una Ruta tiene muchos Tramos.
     * - Un Tramo puede estar en muchas Rutas.
     *
     * Usamos @JoinTable para definir la tabla intermedia que
     * gestionará esta relación.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "ruta_tramos", // Nombre de la tabla intermedia
        joinColumns = @JoinColumn(name = "ruta_id"),     // Columna que apunta a esta entidad (Ruta)
        inverseJoinColumns = @JoinColumn(name = "tramo_id") // Columna que apunta a la otra (Tramo)
    )
    @OrderBy("fechaHoraInicio ASC") // Opcional, pero bueno para mantener el orden
    private List<Tramo> tramos = new ArrayList<>();

    private String direccionOrigen;
    private String direccionDestino;

    private Double latitudOrigen;
    private Double longitudOrigen;

    private Double latitudDestino;;
    private Double longitudDestino;
}