package ar.edu.utn.frc.rutas.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción para cuando se intenta crear un recurso que viola una
 * restricción de unicidad (ej. un 'dominio' de camión ya existente).
 * * Devuelve un código de estado 409 Conflict, que es más específico
 * que un 400 Bad Request para este caso.
 */
@ResponseStatus(value = HttpStatus.CONFLICT) // <-- Esto es lo más importante
public class DominioAlreadyExistsException extends RuntimeException {

    public DominioAlreadyExistsException(String message) {
        super(message); // Pasa el mensaje a la clase padre
    }
}