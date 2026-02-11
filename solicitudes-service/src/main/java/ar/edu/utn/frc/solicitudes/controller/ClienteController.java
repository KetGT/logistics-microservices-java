package ar.edu.utn.frc.solicitudes.controller;

import ar.edu.utn.frc.solicitudes.domain.Cliente;
import ar.edu.utn.frc.solicitudes.domain.dto.CrearClienteDTO;
import ar.edu.utn.frc.solicitudes.service.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController // Indica que esta clase maneja peticiones REST
@RequestMapping("/api/clientes") // Define la URL base para todos los métodos de esta clase
@RequiredArgsConstructor // Inyección por constructor con Lombok
public class ClienteController {

    // Inyección del Service
    private final ClienteService clienteService;

    /**
     * Endpoint: POST /api/clientes
     * Crea un nuevo cliente.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Devuelve un código 201 si la creación es exitosa
    public Cliente crearCliente(@Valid @RequestBody CrearClienteDTO dto) {
        
        // 1. @Valid asegura que los datos del DTO sean correctos
        // 2. El Controller delega inmediatamente al Service
        return clienteService.crearCliente(dto);
    }

    /**
     * Endpoint: GET /api/clientes
     * Obtiene todos los clientes
     */
    @GetMapping
    public ResponseEntity<List<Cliente>> getAllClientes() {
        List<Cliente> clientes = clienteService.obtenerTodosLosClientes();
        return ResponseEntity.ok(clientes);
    }
    
    /**
     * Endpoint: GET /api/clientes/{id}
     * Obtiene un cliente por su ID (útil para verificar la creación)
     */
    @GetMapping("/{id}")
    public Cliente getClientePorId(@PathVariable Long id) {
        return clienteService.obtenerClientePorId(id);
    }
}