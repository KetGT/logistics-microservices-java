package ar.edu.utn.frc.rutas.service;

import ar.edu.utn.frc.rutas.domain.Camion;
import ar.edu.utn.frc.rutas.domain.CrearCamionDTO;
import ar.edu.utn.frc.rutas.exceptions.DominioAlreadyExistsException; // (Ver nota abajo)
import ar.edu.utn.frc.rutas.exceptions.ResourceNotFoundException;
import ar.edu.utn.frc.rutas.repository.CamionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CamionService {

    private final CamionRepository camionRepository;

    @Transactional
    public Camion crearCamion(CrearCamionDTO dto) {

        // 1. Validar Lógica de Negocio: Unicidad del dominio
        if (camionRepository.findByDominio(dto.getDominio()).isPresent()) {
            // Es mejor lanzar una excepción específica (ver abajo)
            throw new DominioAlreadyExistsException("Ya existe un camión con el dominio: " + dto.getDominio());
        }

        // 2. Mapear DTO a Entidad
        Camion nuevoCamion = new Camion();
        nuevoCamion.setDominio(dto.getDominio());
        nuevoCamion.setNombreTransportista(dto.getNombreTransportista());
        nuevoCamion.setTelefono(dto.getTelefono());
        nuevoCamion.setCapacidadPeso(dto.getCapacidadPeso());
        nuevoCamion.setCapacidadVolumen(dto.getCapacidadVolumen());
        nuevoCamion.setConsumoCombustiblePromedio(dto.getConsumoCombustiblePromedio());
        nuevoCamion.setCostoPorKm(dto.getCostoPorKm());
        // 'disponible' se establece en 'true' por defecto gracias a la entidad.

        // 3. Guardar y retornar
        return camionRepository.save(nuevoCamion);
    }

    /**
     * Busca un camión por su ID.
     */
    public Camion obtenerCamionPorId(Long id) {
        return camionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Camión no encontrado con ID: " + id));
    }

    @Transactional
    public Camion actualizarCamion(Long id, CrearCamionDTO dto) {
        Camion camion = camionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Camión no encontrado con ID: " + id));

        // Validar unicidad del dominio solo si se está cambiando y ya existe en otro camión
        if (!camion.getDominio().equals(dto.getDominio())) {
            if (camionRepository.findByDominio(dto.getDominio()).isPresent()) {
                throw new DominioAlreadyExistsException("Ya existe un camión con el dominio: " + dto.getDominio());
            }
        }

        camion.setDominio(dto.getDominio());
        camion.setNombreTransportista(dto.getNombreTransportista());
        camion.setTelefono(dto.getTelefono());
        camion.setCapacidadPeso(dto.getCapacidadPeso());
        camion.setCapacidadVolumen(dto.getCapacidadVolumen());
        camion.setConsumoCombustiblePromedio(dto.getConsumoCombustiblePromedio());
        camion.setCostoPorKm(dto.getCostoPorKm());
        
        return camionRepository.save(camion);
    }

    /**
     * Devuelve una lista de todos los camiones registrados.
     */
    public List<Camion> obtenerTodosLosCamiones() {
        return camionRepository.findAll();
    }
}