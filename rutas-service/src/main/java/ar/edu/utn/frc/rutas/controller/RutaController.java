package ar.edu.utn.frc.rutas.controller;

import ar.edu.utn.frc.rutas.domain.Ruta;
import ar.edu.utn.frc.rutas.domain.CrearRutaDTO;
import ar.edu.utn.frc.rutas.service.RutaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ar.edu.utn.frc.rutas.domain.dto.RutaTentativaDTO;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/rutas")
@RequiredArgsConstructor
public class RutaController {

    private final RutaService rutaService;

    @PostMapping
    public ResponseEntity<Ruta> crearRuta(@Valid @RequestBody CrearRutaDTO dto) {
        
        Ruta rutaCreada = rutaService.crearRuta(dto);
        URI location = URI.create("/api/rutas/" + rutaCreada.getId());
        return ResponseEntity.created(location).body(rutaCreada);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ruta> getRutaPorId(@PathVariable Long id) {
        Ruta ruta = rutaService.obtenerRutaPorId(id);
        return ResponseEntity.ok(ruta);
    }

    @GetMapping
    public ResponseEntity<List<Ruta>> getAllRutas() {
        List<Ruta> rutas = rutaService.obtenerTodasLasRutas();
        return ResponseEntity.ok(rutas);
    }

    @GetMapping("/tentativas")
    public ResponseEntity<List<RutaTentativaDTO>> getRutasTentativas(
            @RequestParam Long solicitudId
    ) {
        List<RutaTentativaDTO> sugerencias = rutaService.consultarRutasTentativas(solicitudId);
        return ResponseEntity.ok(sugerencias);
    }
}