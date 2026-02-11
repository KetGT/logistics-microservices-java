package ar.edu.utn.frc.rutas.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CrearDepositoDTO {

    @NotBlank(message = "El nombre es obligatorio.")
    private String nombre;

    @NotBlank(message = "La dirección textual es obligatoria.")
    private String direccionTextual;

    @NotNull(message = "La latitud es obligatoria.")
    private Double latitud; // Usamos Double (objeto) para @NotNull

    @NotNull(message = "La longitud es obligatoria.")
    private Double longitud; // Usamos Double (objeto) para @NotNull

    private Double costoEstadiaDiario;
}