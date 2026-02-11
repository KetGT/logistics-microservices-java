package ar.edu.utn.frc.rutas.controller;

import ar.edu.utn.frc.rutas.domain.Camion;
import ar.edu.utn.frc.rutas.domain.CrearCamionDTO;
import ar.edu.utn.frc.rutas.service.CamionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/camiones")
@RequiredArgsConstructor
public class CamionController {

    private final CamionService camionService;

    @PostMapping
    public ResponseEntity<Camion> crearCamion(@Valid @RequestBody CrearCamionDTO dto) {
        
        Camion camionCreado = camionService.crearCamion(dto);

        URI location = URI.create("/api/camiones/" + camionCreado.getId());

        // Retornamos 201 Created
        return ResponseEntity.created(location).body(camionCreado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Camion> getCamionPorId(@PathVariable Long id) {
        Camion camion = camionService.obtenerCamionPorId(id);
        return ResponseEntity.ok(camion);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Camion> actualizarCamion(
            @PathVariable Long id,
            @Valid @RequestBody CrearCamionDTO dto) {
        Camion camionActualizado = camionService.actualizarCamion(id, dto);
        return ResponseEntity.ok(camionActualizado);
    }

    @GetMapping
    public ResponseEntity<List<Camion>> getAllCamiones() {
        List<Camion> camiones = camionService.obtenerTodosLosCamiones();
        return ResponseEntity.ok(camiones);
    }
}