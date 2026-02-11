package ar.edu.utn.frc.distancias.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;

// Ignoramos campos que no necesitamos
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class GoogleDistanceMatrixResponse {
    
    private List<Row> rows;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Row {
        private List<Element> elements;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Element {
        private ValueText distance;
        private ValueText duration;
        private String status; // "OK", "NOT_FOUND", etc.
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ValueText {
        private String text;  // "100 km" o "1 hora 30 min"
        private double value; // 100000 (distancia en metros) o 5400 (tiempo en segundos)
    }
}