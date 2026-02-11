package ar.edu.utn.frc.rutas.infraestructure;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TarifasClient {

    private final RestTemplate restTemplate;

    @Value("${tarifas.base-url:http://localhost:8083}")
    private String baseUrl;

    @Data
    public static class TarifaVolumenDTO {
        private Long id;
        private Double volumenMin;
        private Double volumenMax;
        private Double costoPorKmBase;
    }

    public List<TarifaVolumenDTO> getTarifasVolumen() {
        try {
            ResponseEntity<TarifaVolumenDTO[]> resp =
                    restTemplate.getForEntity(baseUrl + "/api/tarifas/volumen", TarifaVolumenDTO[].class);
            TarifaVolumenDTO[] body = resp.getBody();
            return body != null ? Arrays.asList(body) : Collections.emptyList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    /** Devuelve el costo por km aplicable a un volumen dado (o 100.0 por defecto). */
    public double costoPorKmParaVolumen(double volumen) {
        for (TarifaVolumenDTO t : getTarifasVolumen()) {
            double min = t.getVolumenMin() != null ? t.getVolumenMin() : 0.0;
            double max = t.getVolumenMax() != null ? t.getVolumenMax() : Double.MAX_VALUE;
            if (volumen >= min && volumen <= max) {
                return t.getCostoPorKmBase() != null ? t.getCostoPorKmBase() : 100.0;
            }
        }
        return 100.0;
    }
}
