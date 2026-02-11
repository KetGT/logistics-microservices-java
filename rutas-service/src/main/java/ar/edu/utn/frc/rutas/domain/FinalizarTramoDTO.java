package ar.edu.utn.frc.rutas.domain;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FinalizarTramoDTO {
    private LocalDateTime fechaFinReal;
    private LocalDateTime fechaEntradaDeposito; // opcional
    private LocalDateTime fechaSalidaDeposito;  // opcional
}
