package ar.edu.utn.frc.solicitudes.controller;

import ar.edu.utn.frc.solicitudes.domain.Contenedor;
import ar.edu.utn.frc.solicitudes.domain.dto.CrearContenedorDTO;
import ar.edu.utn.frc.solicitudes.domain.dto.EstadoContenedorDTO;
import ar.edu.utn.frc.solicitudes.domain.tipo.EstadoContenedor;
import ar.edu.utn.frc.solicitudes.service.ContenedorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/contenedores")
@RequiredArgsConstructor
public class ContenedorController {

    private final ContenedorService contenedorService;

    @PostMapping
    public ResponseEntity<Contenedor> crearContenedor(@Valid @RequestBody CrearContenedorDTO dto) {
        
        Contenedor contenedorCreado = contenedorService.crearContenedor(dto);

        URI location = URI.create("/api/contenedores/" + contenedorCreado.getId());

        // Retorna 201 Created con el objeto y la ubicación
        return ResponseEntity.created(location).body(contenedorCreado);
    }

    /**
     * Endpoint: GET /api/contenedores
     * Obtiene todos los contenedores
     */
    @GetMapping
    public ResponseEntity<List<Contenedor>> getAllContenedores() {
        List<Contenedor> contenedores = contenedorService.obtenerTodosLosContenedores();
        return ResponseEntity.ok(contenedores);
    }

    /**
     * Endpoint: GET /api/contenedores/{id}
     * Obtiene un contenedor por su ID (útil para verificar la creación)
     */
    @GetMapping("/{id}")
    public Contenedor getContenedorPorId(@PathVariable Long id) {
        return contenedorService.obtenerContenedorPorId(id);
    }

    /**
     * Endpoint: GET /api/contenedores/{id}/estado
     * Obtiene el estado de un contenedor por su ID
     */
    @GetMapping("/{id}/estado")
    public ResponseEntity<EstadoContenedorDTO> getEstadoContenedor(@PathVariable Long id) {
        EstadoContenedor estado = contenedorService.obtenerEstadoContenedor(id);
        EstadoContenedorDTO response = new EstadoContenedorDTO(estado);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/pendientes")
    public ResponseEntity<List<Contenedor>> getContenedoresPendientes(
            @RequestParam(required = false) EstadoContenedor estado,
            @RequestParam(required = false) Long clienteId
    ) {
        List<Contenedor> contenedores = contenedorService.getContenedoresPendientes(estado, clienteId);
        return ResponseEntity.ok(contenedores);
    }

    // SOLO PARA PRUEBAS
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Contenedor> actualizarEstado(
            @PathVariable Long id,
            @Valid @RequestBody EstadoContenedorDTO dto // <-- Tu DTO
    ) {
        Contenedor contenedor = contenedorService.actualizarEstado(id, dto);
        return ResponseEntity.ok(contenedor);
    }
}