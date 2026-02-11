package ar.edu.utn.frc.solicitudes.controller;

import ar.edu.utn.frc.solicitudes.domain.Contenedor;
import ar.edu.utn.frc.solicitudes.domain.Solicitud;
import ar.edu.utn.frc.solicitudes.domain.dto.AsignarRutaDTO;
import ar.edu.utn.frc.solicitudes.domain.dto.CrearContenedorDTO;
import ar.edu.utn.frc.solicitudes.domain.dto.CrearSolicitudDTO;
import ar.edu.utn.frc.solicitudes.service.SolicitudService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/solicitudes")
@RequiredArgsConstructor
public class SolicitudController {

    private final SolicitudService solicitudService;

    @PostMapping
    public ResponseEntity<Solicitud> crearSolicitud(@Valid @RequestBody CrearSolicitudDTO dto) {
        Solicitud solicitudCreada = solicitudService.crearSolicitud(dto);
        URI location = URI.create("/api/solicitudes/" + solicitudCreada.getId());
        return ResponseEntity.created(location).body(solicitudCreada);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Solicitud> getSolicitudPorId(@PathVariable Long id) {
        Solicitud solicitud = solicitudService.obtenerSolicitudPorId(id);
        return ResponseEntity.ok(solicitud);
    }

    @PatchMapping("/{solicitudId}/asignar-ruta")
    public ResponseEntity<Solicitud> asignarRuta(
            @PathVariable Long solicitudId,
            @RequestBody AsignarRutaDTO dto) {
        Solicitud solicitudActualizada = solicitudService.asignarRuta(solicitudId, dto);
        return ResponseEntity.ok(solicitudActualizada);
    }

    // ===== NUEVO: Puntos 8 y 9 (finalizar solicitud con cálculo de costo/tiempo real) =====
    @PatchMapping("/{id}/finalizar")
    public ResponseEntity<Solicitud> finalizar(@PathVariable Long id) {
        Solicitud s = solicitudService.finalizarSolicitud(id);
        return ResponseEntity.ok(s);
    }

    @GetMapping("/{id}/contenedor")
public ResponseEntity<?> getContenedorDeSolicitud(@PathVariable Long id) {
    try {
        Solicitud solicitud = solicitudService.obtenerSolicitudPorId(id);
        Contenedor contenedor = solicitud.getContenedor();

        // Usamos tu DTO existente, como mencionaste
        CrearContenedorDTO dto = new CrearContenedorDTO(); 
        
        // Asumimos que CrearContenedorDTO tiene setters para peso y volumen
        // (Si no los tiene, tendrás que agregarlos)
        dto.setPeso(contenedor.getPeso());
        dto.setVolumen(contenedor.getVolumen());
        // No es necesario setear clienteId u otros campos aquí

        return ResponseEntity.ok(dto);
        
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("Solicitud o contenedor no encontrado: " + e.getMessage());
    }
}
}
