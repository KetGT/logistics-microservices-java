package ar.edu.utn.frc.rutas.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CrearCamionDTO {

    @NotBlank(message = "El dominio (patente) es obligatorio.")
    private String dominio;

    @NotBlank(message = "El nombre del transportista es obligatorio.")
    private String nombreTransportista;

    @NotBlank(message = "El teléfono es obligatorio.")
    private String telefono;

    @NotNull(message = "La capacidad de peso es obligatoria.")
    @Positive(message = "La capacidad de peso debe ser positiva.")
    private Double capacidadPeso; // Usamos Double (wrapper) para @NotNull

    @NotNull(message = "La capacidad de volumen es obligatoria.")
    @Positive(message = "La capacidad de volumen debe ser positiva.")
    private Double capacidadVolumen;


    @NotNull(message = "El consumo promedio de combustible es obligatorio.")
    @Positive(message = "El consumo promedio debe ser positivo.")
    private Double consumoCombustiblePromedio;

    @NotNull(message = "El costo por km es obligatorio.")
    @Positive(message = "El costo por km debe ser positivo.")
    private Double costoPorKm;
}