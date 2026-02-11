package ar.edu.utn.frc.rutas.domain.dto;
import lombok.Data;
import java.util.List;

@Data
public class RutaTentativaDTO {
    private String tipoRuta; // Ej: "Ruta Directa"
    private List<String> tramosSugeridos; // Lista de descripciones
    private Double costoEstimado;
    private String tiempoEstimado;
    private Double kilometrosTotales;
}