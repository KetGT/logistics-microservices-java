package ar.edu.utn.frc.rutas.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción para datos inválidos en la creación de un Tramo
 * (ej. falta un ID de depósito cuando el tipo lo requiere).
 * Devuelve un 400 Bad Request.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidTramoDataException extends RuntimeException {

    public InvalidTramoDataException(String message) {
        super(message);
    }
}