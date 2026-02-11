package ar.edu.utn.frc.solicitudes.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Email;

@Data
@NoArgsConstructor
public class CrearClienteDTO {

    @NotNull(message = "El nombre del cliente es obligatorio.")
    private String nombre;
    
    @NotNull(message = "El email del cliente es obligatorio.")
    @Email(message = "El formato del email es inválido.")
    private String email;
    
    // Asumiendo que el teléfono es opcional, no necesita @NotNull.
    private Integer telefono; 
}