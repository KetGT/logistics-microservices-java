package ar.edu.utn.frc.rutas.domain;

import ar.edu.utn.frc.rutas.domain.tipo.TipoTramo;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CrearTramoDTO {

    @NotNull(message = "El tipo de tramo es obligatorio.")
    private TipoTramo tipo;

    // --- Posible Origen ---
    private Long origenDepositoId; // Usado si tipo es DEPOSITO_DEPOSITO o DEPOSITO_DESTINO
    private String origenExternoDireccion; // Usado si tipo es ORIGEN_DEPOSITO u ORIGEN_DESTINO
    private String origenExternoLat;
    private String origenExternoLon;

    // --- Posible Destino ---
    private Long destinoDepositoId; // Usado si tipo es ORIGEN_DEPOSITO o DEPOSITO_DEPOSITO
    private String destinoExternoDireccion; // Usado si tipo es DEPOSITO_DESTINO u ORIGEN_DESTINO
    private String destinoExternoLat;
    private String destinoExternoLon;

    // Nota: No pedimos estado (es ESTIMADO), ni camionId, ni fechas, ni costoReal.
}