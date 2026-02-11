package ar.edu.utn.frc.solicitudes.service;

import ar.edu.utn.frc.solicitudes.domain.Contenedor;
import ar.edu.utn.frc.solicitudes.domain.Cliente;
import ar.edu.utn.frc.solicitudes.domain.dto.CrearContenedorDTO;
import ar.edu.utn.frc.solicitudes.domain.dto.EstadoContenedorDTO;
import ar.edu.utn.frc.solicitudes.domain.tipo.EstadoContenedor;
import ar.edu.utn.frc.solicitudes.repository.ContenedorRepository;
import ar.edu.utn.frc.solicitudes.exceptions.ResourceNotFoundException;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import jakarta.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContenedorService {
    
    private final ContenedorRepository contenedorRepository;
    private final ClienteService clienteService; // Necesitamos el servicio de cliente

    @Transactional
    public Contenedor crearContenedor(CrearContenedorDTO dto) {
        
        // 1. OBTENER LA ENTIDAD RELACIONADA: Buscar el Cliente por su ID
        // Si el clienteService no lo encuentra, lanzará la ResourceNotFoundException (404)
        Cliente cliente = clienteService.obtenerClientePorId(dto.getClienteId());

        // 2. MAPEO: Crear la nueva Entidad Contenedor
        Contenedor nuevoContenedor = new Contenedor();
        nuevoContenedor.setPeso(dto.getPeso());
        nuevoContenedor.setVolumen(dto.getVolumen());
        
        // 3. ESTABLECER LA RELACIÓN
        nuevoContenedor.setCliente(cliente);

        // 4. PERSISTENCIA
        return contenedorRepository.save(nuevoContenedor);
    }

    public Contenedor obtenerContenedorPorId(Long id) {
         return contenedorRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Contenedor no encontrado con ID: " + id));
    }

    /**
     * Obtiene el estado de un contenedor por su ID
     * @param id ID del contenedor
     * @return El estado del contenedor
     */
    public EstadoContenedor obtenerEstadoContenedor(Long id) {
        Contenedor contenedor = obtenerContenedorPorId(id);
        return contenedor.getEstado();
    }

    /**
     * Obtiene todos los contenedores
     * @return Lista de todos los contenedores
     */
    public List<Contenedor> obtenerTodosLosContenedores() {
        return contenedorRepository.findAll();
    }

    /**
     * Guarda un contenedor (método auxiliar para guardar contenedores ya creados)
     * @param contenedor Contenedor a guardar
     * @return Contenedor guardado con ID asignado
     */
    @Transactional
    public Contenedor guardarContenedor(Contenedor contenedor) {
        return contenedorRepository.save(contenedor);
    }

    public List<Contenedor> getContenedoresPendientes(EstadoContenedor estado, Long clienteId) {
        
        // 1. Creamos la especificación de consulta con los filtros
        Specification<Contenedor> spec = ContenedorSpecification.withFilters(estado, clienteId);
        
        // 2. Ejecutamos la consulta
        return contenedorRepository.findAll(spec);
    }

    //SOLO PARA PRUEBAS
    @Transactional
    public Contenedor actualizarEstado(Long id, EstadoContenedorDTO dto) {
        Contenedor contenedor = obtenerContenedorPorId(id); // Reutilizas tu método
        
        // Usamos el getter de tu DTO
        contenedor.setEstado(dto.getEstado_contenedor()); 
        
        return contenedorRepository.save(contenedor);
    }
}