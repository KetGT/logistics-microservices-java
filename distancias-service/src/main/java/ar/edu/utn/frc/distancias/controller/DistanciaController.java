package ar.edu.utn.frc.distancias.controller;

import ar.edu.utn.frc.distancias.domain.dto.DistanciaDTO;
import ar.edu.utn.frc.distancias.service.GoogleMapsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/distancia")
@RequiredArgsConstructor
public class DistanciaController {

    private final GoogleMapsService googleMapsService;

    @GetMapping
    public ResponseEntity<DistanciaDTO> getDistancia(
            @RequestParam double latOrigen,
            @RequestParam double lonOrigen,
            @RequestParam double latDestino,
            @RequestParam double lonDestino
    ) {
        DistanciaDTO dto = googleMapsService.getDistancia(latOrigen, lonOrigen, latDestino, lonDestino);
        return ResponseEntity.ok(dto);
    }
}