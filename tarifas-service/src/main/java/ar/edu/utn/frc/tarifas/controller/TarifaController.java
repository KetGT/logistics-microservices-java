// En: tarifas-service/src/main/java/ar/edu/utn/frc/tarifas/controller/TarifaController.java

package ar.edu.utn.frc.tarifas.controller;

import ar.edu.utn.frc.tarifas.domain.dto.CalcularCostoEstimadoRequestDTO;
import ar.edu.utn.frc.tarifas.domain.dto.CalcularCostoRealRequestDTO;
import ar.edu.utn.frc.tarifas.domain.dto.ConfiguracionGeneralDTO;
import ar.edu.utn.frc.tarifas.domain.dto.TarifaVolumenDTO;
import ar.edu.utn.frc.tarifas.service.TarifaService;
import lombok.RequiredArgsConstructor;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tarifas")
@RequiredArgsConstructor
public class TarifaController {

    private final TarifaService tarifaService;

    // --- Endpoints para ConfiguracionGeneral ---

    @GetMapping("/configuracion")
    public ResponseEntity<ConfiguracionGeneralDTO> getConfiguracion() {
        return ResponseEntity.ok(tarifaService.getConfiguracion());
    }

    @PutMapping("/configuracion") // Usamos PUT para actualizar la configuración única
    public ResponseEntity<ConfiguracionGeneralDTO> updateConfiguracion(
            @RequestBody ConfiguracionGeneralDTO dto) {
        return ResponseEntity.ok(tarifaService.updateConfiguracion(dto));
    }
    
    // --- Endpoints para TarifaVolumen (para el Admin) ---

    @PostMapping("/volumen")
    public ResponseEntity<TarifaVolumenDTO> createTarifaVolumen(
            @RequestBody TarifaVolumenDTO dto) {
        TarifaVolumenDTO created = tarifaService.createTarifaVolumen(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/volumen")
    public ResponseEntity<List<TarifaVolumenDTO>> getAllTarifasVolumen() {
        return ResponseEntity.ok(tarifaService.getAllTarifasVolumen());
    }

    @PutMapping("/volumen/{id}")
    public ResponseEntity<TarifaVolumenDTO> updateTarifaVolumen(
            @PathVariable Long id,
            @RequestBody TarifaVolumenDTO dto) {
        TarifaVolumenDTO updated = tarifaService.updateTarifaVolumen(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/volumen/{id}")
    public ResponseEntity<Void> deleteTarifaVolumen(@PathVariable Long id) {
        tarifaService.deleteTarifaVolumen(id);
        return ResponseEntity.noContent().build();
    }

    // --- ENDPOINTS DE CÁLCULO (Para otros Microservicios) ---

    @PostMapping("/calcular/real")
    public ResponseEntity<Double> calcularCostoReal(
            @RequestBody CalcularCostoRealRequestDTO request) {
        
        Double costo = tarifaService.calcularCostoReal(request);
        return ResponseEntity.ok(costo);
    }

    @PostMapping("/calcular/estimado")
    public ResponseEntity<Double> calcularCostoEstimado(
            @RequestBody CalcularCostoEstimadoRequestDTO request) {
        
        Double costo = tarifaService.calcularCostoEstimado(request);
        return ResponseEntity.ok(costo);
    }
}