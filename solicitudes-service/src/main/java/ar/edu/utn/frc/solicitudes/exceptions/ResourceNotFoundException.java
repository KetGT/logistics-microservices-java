package ar.edu.utn.frc.solicitudes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción personalizada para indicar que un recurso no fue encontrado.
 *
 * Al anotarla con @ResponseStatus(HttpStatus.NOT_FOUND), Spring Boot
 * automáticamente devolverá un código de estado 404 Not Found
 * cuando esta excepción sea lanzada y no sea capturada por otro manejador.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructor que acepta un mensaje de error.
     * Este es el constructor que tus servicios están intentando llamar.
     *
     * @param message El mensaje detallando el error.
     */
    public ResourceNotFoundException(String message) {
        super(message); // Pasa el mensaje a la clase padre (RuntimeException)
    }
}