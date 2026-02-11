package ar.edu.utn.frc.solicitudes.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CrearSolicitudDTO {

    // Datos de la solicitud
    private Double costoEstimado;
    private String tiempoEstimado;
    private Double costoFinal;
    private String tiempoFinal;

    // Datos del cliente (si no existe, se crea)
    @NotNull(message = "El nombre del cliente es obligatorio.")
    private String clienteNombre;
    
    @NotNull(message = "El email del cliente es obligatorio.")
    @Email(message = "El formato del email es inválido.")
    private String clienteEmail;
    
    private Integer clienteTelefono;

    // Datos del contenedor
    // Si contenedorId viene, se busca y se actualiza. Si no viene, se crea uno nuevo.
    private Long contenedorId; // Opcional: si viene, se busca el contenedor existente
    
    @NotNull(message = "El peso del contenedor es obligatorio.")
    private Double contenedorPeso;
    
    @NotNull(message = "El volumen del contenedor es obligatorio.")
    private Double contenedorVolumen;

    @NotNull(message = "La dirección de origen es obligatoria")
    private String origenDireccion;
    @NotNull(message = "La latitud de origen es obligatoria")
    private Double origenLat;
    @NotNull(message = "La longitud de origen es obligatoria")
    private Double origenLon;

    @NotNull(message = "La dirección de destino es obligatoria")
    private String destinoDireccion;
    @NotNull(message = "La latitud de destino es obligatoria")
    private Double destinoLat;
    @NotNull(message = "La longitud de destino es obligatoria")
    private Double destinoLon;


}
