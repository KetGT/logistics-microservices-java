package ar.edu.utn.frc.rutas.controller;

import ar.edu.utn.frc.rutas.domain.Tramo;
import ar.edu.utn.frc.rutas.domain.CrearTramoDTO;
import ar.edu.utn.frc.rutas.domain.IniciarTramoDTO;
import ar.edu.utn.frc.rutas.domain.FinalizarTramoDTO;
import ar.edu.utn.frc.rutas.domain.AsignarCamionDTO;
import ar.edu.utn.frc.rutas.service.TramoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/tramos")
@RequiredArgsConstructor
public class TramoController {

    private final TramoService tramoService;

    @PostMapping
    public ResponseEntity<Tramo> crearTramo(@Valid @RequestBody CrearTramoDTO dto) {
        Tramo tramoCreado = tramoService.crearTramo(dto);
        URI location = URI.create("/api/tramos/" + tramoCreado.getId());
        return ResponseEntity.created(location).body(tramoCreado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tramo> getTramoPorId(@PathVariable Long id) {
        Tramo tramo = tramoService.obtenerTramoPorId(id);
        return ResponseEntity.ok(tramo);
    }

    @GetMapping
    public ResponseEntity<List<Tramo>> getAllTramos() {
        List<Tramo> tramos = tramoService.obtenerTodosLosTramos();
        return ResponseEntity.ok(tramos);
    }

    // ------------------- ENDPOINTS NUEVOS -------------------

    // Iniciar un tramo (PENDIENTE -> EN_TRASLADO)
    @PatchMapping("/{id}/iniciar")
    public ResponseEntity<Tramo> iniciarTramo(@PathVariable Long id,
                                              @RequestBody IniciarTramoDTO dto) {
        Tramo actualizado = tramoService.iniciarTramo(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    // Finalizar un tramo (EN_TRASLADO -> COMPLETADO)
    @PatchMapping("/{id}/finalizar")
    public ResponseEntity<Tramo> finalizarTramo(@PathVariable Long id,
                                                @RequestBody FinalizarTramoDTO dto) {
        Tramo actualizado = tramoService.finalizarTramo(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    // Asignar camión a un tramo (con validación de capacidad)
    @PatchMapping("/{id}/asignar-camion")
    public ResponseEntity<Tramo> asignarCamion(@PathVariable Long id,
                                               @RequestBody AsignarCamionDTO dto) {
        Tramo actualizado = tramoService.asignarCamion(id, dto.getCamionId());
        return ResponseEntity.ok(actualizado);
    }
}
