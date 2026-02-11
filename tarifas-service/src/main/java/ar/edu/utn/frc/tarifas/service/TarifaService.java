// En: tarifas-service/src/main/java/ar/edu/utn/frc/tarifas/service/TarifaService.java

package ar.edu.utn.frc.tarifas.service;

import ar.edu.utn.frc.tarifas.domain.ConfiguracionGeneral;
import ar.edu.utn.frc.tarifas.domain.TarifaVolumen;
import ar.edu.utn.frc.tarifas.domain.dto.ConfiguracionGeneralDTO;
import ar.edu.utn.frc.tarifas.domain.dto.TarifaVolumenDTO;
import ar.edu.utn.frc.tarifas.repository.ConfiguracionGeneralRepository;
import ar.edu.utn.frc.tarifas.repository.TarifaVolumenRepository;
import lombok.RequiredArgsConstructor;
import ar.edu.utn.frc.tarifas.domain.dto.CalcularCostoEstimadoRequestDTO;
import ar.edu.utn.frc.tarifas.domain.dto.CalcularCostoRealRequestDTO;

import java.util.stream.Collectors;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TarifaService {

    private final ConfiguracionGeneralRepository configuracionRepo;
    private final TarifaVolumenRepository tarifaVolumenRepo;

    // --- Lógica para ConfiguracionGeneral ---

    @Transactional(readOnly = true)
    public ConfiguracionGeneralDTO getConfiguracion() {
        ConfiguracionGeneral config = configuracionRepo.findFirstByOrderByIdAsc()
            .orElseThrow(() -> new RuntimeException("Configuración general no encontrada."));
        
        // Mapeamos de Entidad a DTO
        return mapToDto(config);
    }

    @Transactional
    public ConfiguracionGeneralDTO updateConfiguracion(ConfiguracionGeneralDTO dto) {
        // Buscamos la config existente o creamos una nueva si no hay ninguna
        ConfiguracionGeneral config = configuracionRepo.findFirstByOrderByIdAsc()
            .orElse(new ConfiguracionGeneral());

        // Actualizamos los valores
        config.setPrecioLitroCombustible(dto.getPrecioLitroCombustible());
        config.setCargoGestion(dto.getCargoGestion());
        config.setConsumoCombustiblePromedioGeneral(dto.getConsumoCombustiblePromedioGeneral());

        ConfiguracionGeneral savedConfig = configuracionRepo.save(config);
        
        // Mapeamos y devolvemos el DTO actualizado
        return mapToDto(savedConfig);
    }

    // --- Lógica para TarifaVolumen ---

    @Transactional
    public TarifaVolumenDTO createTarifaVolumen(TarifaVolumenDTO dto) {
        TarifaVolumen tarifa = new TarifaVolumen();
        tarifa.setVolumenMin(dto.getVolumenMin());
        tarifa.setVolumenMax(dto.getVolumenMax());
        tarifa.setCostoPorKmBase(dto.getCostoPorKmBase());
        
        TarifaVolumen savedTarifa = tarifaVolumenRepo.save(tarifa);
        return mapToDto(savedTarifa);
    }

    @Transactional(readOnly = true)
    public List<TarifaVolumenDTO> getAllTarifasVolumen() {
        return tarifaVolumenRepo.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public TarifaVolumenDTO updateTarifaVolumen(Long id, TarifaVolumenDTO dto) {
        TarifaVolumen tarifa = tarifaVolumenRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Tarifa con id " + id + " no encontrada."));
        
        tarifa.setVolumenMin(dto.getVolumenMin());
        tarifa.setVolumenMax(dto.getVolumenMax());
        tarifa.setCostoPorKmBase(dto.getCostoPorKmBase());
        
        TarifaVolumen savedTarifa = tarifaVolumenRepo.save(tarifa);
        return mapToDto(savedTarifa);
    }
    
    @Transactional
    public void deleteTarifaVolumen(Long id) {
        if (!tarifaVolumenRepo.existsById(id)) {
            throw new RuntimeException("Tarifa con id " + id + " no encontrada.");
        }
        tarifaVolumenRepo.deleteById(id);
    }
    
    // (Este método lo usaremos internamente para los cálculos)
    @Transactional(readOnly = true)
    public TarifaVolumen getTarifaPorVolumen(Double volumen) {
        return tarifaVolumenRepo.findByVolumenMinLessThanEqualAndVolumenMaxGreaterThanEqual(volumen, volumen)
            .orElseThrow(() -> new RuntimeException("No se encontró tarifa para el volumen: " + volumen));
    }


    @Transactional(readOnly = true)
    public Double calcularCostoReal(CalcularCostoRealRequestDTO request) {
        
        // 1. Obtener valores globales
        ConfiguracionGeneral config = getConfiguracionInterna();

        // 2. Calcular cada componente
        
        // Costo de Gestión (Fijo)
        Double costoGestion = config.getCargoGestion();

        // Costo por Kilómetro (del camión específico)
        Double costoKmTotal = request.getCostoPorKm() * request.getDistanciaEnKm();
        
        // Costo de Combustible (del camión específico)
        Double litrosConsumidos = request.getConsumoCombustiblePorKm() * request.getDistanciaEnKm();
        Double costoCombustibleTotal = litrosConsumidos * config.getPrecioLitroCombustible();

        // Costo de Estadía en Depósito
        Double costoEstadiaTotal = request.getDiasDeEstadia() * request.getCostoEstadiaDiario();

        // 3. Sumar todo
        Double costoFinal = costoGestion + costoKmTotal + costoCombustibleTotal + costoEstadiaTotal;

        return costoFinal;
    }

    /**
     * Calcula el costo ESTIMADO de un envío.
     * Usa la fórmula: Gestión + CostoKm(Base) + Combustible(Promedio)
     */
    @Transactional(readOnly = true)
    public Double calcularCostoEstimado(CalcularCostoEstimadoRequestDTO request) {
        
        // 1. Obtener valores globales
        ConfiguracionGeneral config = getConfiguracionInterna();

        // 2. Obtener tarifa base por volumen
        TarifaVolumen tarifaVolumen = getTarifaPorVolumen(request.getVolumenContenedor());
        Double costoPorKmBase = tarifaVolumen.getCostoPorKmBase();

        // 3. Calcular cada componente
        
        // Costo de Gestión (Fijo)
        Double costoGestion = config.getCargoGestion();

        // Costo por Kilómetro (Estimado, basado en volumen)
        Double costoKmTotal = costoPorKmBase * request.getDistanciaEnKm();

        // Costo de Combustible (Estimado, basado en promedio general)
        Double litrosConsumidos = config.getConsumoCombustiblePromedioGeneral() * request.getDistanciaEnKm();
        Double costoCombustibleTotal = litrosConsumidos * config.getPrecioLitroCombustible();

        // 4. Sumar todo (sin estadía, ya que es un estimado)
        Double costoEstimado = costoGestion + costoKmTotal + costoCombustibleTotal;

        return costoEstimado;
    }


    // --- Mapeadores privados ---

    private ConfiguracionGeneralDTO mapToDto(ConfiguracionGeneral config) {
        ConfiguracionGeneralDTO dto = new ConfiguracionGeneralDTO();
        dto.setPrecioLitroCombustible(config.getPrecioLitroCombustible());
        dto.setCargoGestion(config.getCargoGestion());
        dto.setConsumoCombustiblePromedioGeneral(config.getConsumoCombustiblePromedioGeneral());
        return dto;
    }

    private TarifaVolumenDTO mapToDto(TarifaVolumen tarifa) {
        TarifaVolumenDTO dto = new TarifaVolumenDTO();
        dto.setId(tarifa.getId());
        dto.setVolumenMin(tarifa.getVolumenMin());
        dto.setVolumenMax(tarifa.getVolumenMax());
        dto.setCostoPorKmBase(tarifa.getCostoPorKmBase());
        return dto;
    }

    // Helper interno para no repetir código
    private ConfiguracionGeneral getConfiguracionInterna() {
        return configuracionRepo.findFirstByOrderByIdAsc()
            .orElseThrow(() -> new RuntimeException("Configuración general no encontrada."));
    }
}