package ar.edu.utn.frc.solicitudes.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import ar.edu.utn.frc.solicitudes.domain.tipo.EstadoContenedor;
@Entity
@Data
@NoArgsConstructor
public class Contenedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contenedor_id")
    private Long id;

    private Double peso;

    private Double volumen;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoContenedor estado = EstadoContenedor.PENDIENTE_RETIRO; // Valor por defecto al crear

    @ManyToOne 
    @JoinColumn(name = "cliente_id") // La columna 'cliente_id' estará en la tabla 'Contenedor'
    private Cliente cliente;
}
