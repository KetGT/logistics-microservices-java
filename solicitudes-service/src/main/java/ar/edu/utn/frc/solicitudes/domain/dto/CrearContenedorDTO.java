package ar.edu.utn.frc.solicitudes.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CrearContenedorDTO {

    // Necesitamos el ID del cliente para la relación ManyToOne
    @NotNull(message = "El ID del cliente es obligatorio para asignar el contenedor.")
    private Long clienteId;

    @NotNull(message = "El peso del contenedor es obligatorio.")
    private Double peso;

    @NotNull(message = "El volumen del contenedor es obligatorio.")
    private Double volumen;
}