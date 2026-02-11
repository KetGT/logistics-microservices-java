package ar.edu.utn.frc.rutas.controller;

import ar.edu.utn.frc.rutas.domain.Deposito;
import ar.edu.utn.frc.rutas.domain.CrearDepositoDTO;
import ar.edu.utn.frc.rutas.service.DepositoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/depositos")
@RequiredArgsConstructor
public class DepositoController {

    private final DepositoService depositoService;

    @PostMapping
    public ResponseEntity<Deposito> crearDeposito(@Valid @RequestBody CrearDepositoDTO dto) {
        
        Deposito depositoCreado = depositoService.crearDeposito(dto);

        // Creamos la URL del nuevo recurso (Ej: /api/depositos/3)
        URI location = URI.create("/api/depositos/" + depositoCreado.getId());

        // Retornamos 201 Created, la ubicación y el objeto creado
        return ResponseEntity.created(location).body(depositoCreado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Deposito> getDepositoPorId(@PathVariable Long id) {
        Deposito deposito = depositoService.obtenerDepositoPorId(id);
        // Retornamos 200 OK y el depósito
        return ResponseEntity.ok(deposito);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Deposito> actualizarDeposito(
            @PathVariable Long id,
            @Valid @RequestBody CrearDepositoDTO dto) {
        Deposito depositoActualizado = depositoService.actualizarDeposito(id, dto);
        return ResponseEntity.ok(depositoActualizado);
    }

    @GetMapping
    public ResponseEntity<List<Deposito>> getAllDepositos() {
        List<Deposito> depositos = depositoService.obtenerTodosLosDepositos();
        return ResponseEntity.ok(depositos);
    }
}