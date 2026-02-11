package ar.edu.utn.frc.rutas.domain;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CrearRutaDTO {

    @NotNull(message = "El ID de la solicitud es obligatorio.")
    private Long solicitudId;

    @NotEmpty(message = "La ruta debe tener al menos un tramo.")
    private List<Long> tramoIds; // Lista de IDs de los Tramos que componen esta ruta

    private String direccionOrigen;
    private String direccionDestino;
    private Double latitudOrigen;
    private Double longitudOrigen;
    private Double latitudDestino;
    private Double longitudDestino;
}