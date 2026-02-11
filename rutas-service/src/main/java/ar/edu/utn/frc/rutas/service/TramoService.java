package ar.edu.utn.frc.rutas.service;

import ar.edu.utn.frc.rutas.domain.Deposito;
import ar.edu.utn.frc.rutas.domain.Tramo;
import ar.edu.utn.frc.rutas.domain.Camion;
import ar.edu.utn.frc.rutas.domain.CrearTramoDTO;
import ar.edu.utn.frc.rutas.domain.IniciarTramoDTO;
import ar.edu.utn.frc.rutas.domain.Ruta;
import ar.edu.utn.frc.rutas.domain.FinalizarTramoDTO;
import ar.edu.utn.frc.rutas.domain.tipo.EstadoTramo;
import ar.edu.utn.frc.rutas.exceptions.InvalidTramoDataException;
import ar.edu.utn.frc.rutas.exceptions.ResourceNotFoundException;
import ar.edu.utn.frc.rutas.infraestructure.SolicitudesClient;
import ar.edu.utn.frc.rutas.infraestructure.SolicitudesClient.ContenedorDTO;
import ar.edu.utn.frc.rutas.repository.DepositoRepository;
import ar.edu.utn.frc.rutas.repository.TramoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ar.edu.utn.frc.rutas.repository.CamionRepository;
import ar.edu.utn.frc.rutas.repository.RutaRepository;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TramoService {

    private final TramoRepository tramoRepository;
    private final DepositoRepository depositoRepository; // Para buscar los depósitos por ID
    private final CamionRepository camionRepository;
    private final RutaRepository rutaRepository;
    private final SolicitudesClient solicitudesClient;
    
    @Transactional
    public Tramo crearTramo(CrearTramoDTO dto) {

        Tramo nuevoTramo = new Tramo();
        nuevoTramo.setTipo(dto.getTipo());

        // Estado inicial
        nuevoTramo.setEstado(EstadoTramo.ESTIMADO);

        // Lógica de mapeo y validación según el TIPO
        switch (dto.getTipo()) {
            case ORIGEN_DEPOSITO:
                validarCamposExternos(dto.getOrigenExternoDireccion(), "origen");
                Deposito destinoDep = obtenerDeposito(dto.getDestinoDepositoId(), "destino");

                nuevoTramo.setOrigenExternoDireccion(dto.getOrigenExternoDireccion());
                nuevoTramo.setOrigenExternoLat(dto.getOrigenExternoLat());
                nuevoTramo.setOrigenExternoLon(dto.getOrigenExternoLon());
                nuevoTramo.setDestinoDeposito(destinoDep);
                break;

            case DEPOSITO_DEPOSITO:
                Deposito origenDep = obtenerDeposito(dto.getOrigenDepositoId(), "origen");
                Deposito destinoDep2 = obtenerDeposito(dto.getDestinoDepositoId(), "destino");

                nuevoTramo.setOrigenDeposito(origenDep);
                nuevoTramo.setDestinoDeposito(destinoDep2);
                break;

            case DEPOSITO_DESTINO:
                Deposito origenDep2 = obtenerDeposito(dto.getOrigenDepositoId(), "origen");
                validarCamposExternos(dto.getDestinoExternoDireccion(), "destino");

                nuevoTramo.setOrigenDeposito(origenDep2);
                nuevoTramo.setDestinoExternoDireccion(dto.getDestinoExternoDireccion());
                nuevoTramo.setDestinoExternoLat(dto.getDestinoExternoLat());
                nuevoTramo.setDestinoExternoLon(dto.getDestinoExternoLon());
                break;

            case ORIGEN_DESTINO:
                validarCamposExternos(dto.getOrigenExternoDireccion(), "origen");
                validarCamposExternos(dto.getDestinoExternoDireccion(), "destino");

                nuevoTramo.setOrigenExternoDireccion(dto.getOrigenExternoDireccion());
                nuevoTramo.setOrigenExternoLat(dto.getOrigenExternoLat());
                nuevoTramo.setOrigenExternoLon(dto.getOrigenExternoLon());
                nuevoTramo.setDestinoExternoDireccion(dto.getDestinoExternoDireccion());
                nuevoTramo.setDestinoExternoLat(dto.getDestinoExternoLat());
                nuevoTramo.setDestinoExternoLon(dto.getDestinoExternoLon());
                break;
        }

        return tramoRepository.save(nuevoTramo);
    }

    // --- Métodos de Ayuda (Helper) ---

    private Deposito obtenerDeposito(Long id, String tipoPunto) {
        if (id == null) {
            throw new InvalidTramoDataException("El ID del depósito de " + tipoPunto + " no puede ser nulo.");
        }
        return depositoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Depósito de " + tipoPunto + " no encontrado con ID: " + id));
    }

    private void validarCamposExternos(String direccion, String tipoPunto) {
        if (direccion == null || direccion.isBlank()) {
            throw new InvalidTramoDataException("La dirección externa de " + tipoPunto + " no puede estar vacía.");
        }
        // Aquí podrías agregar validación de Lat/Lon si fuesen obligatorios
    }

    // --- Métodos de Lectura ---

    public Tramo obtenerTramoPorId(Long id) {
        return tramoRepository.findByIdWithAssociations(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tramo no encontrado con ID: " + id));
    }


    public List<Tramo> obtenerTodosLosTramos() {
        return tramoRepository.findAllWithAssociations();
    }

    // =========================================================
    // ============ PUNTO 7: Iniciar / Finalizar Tramo =========
    // =========================================================

    /**
     * Inicia un tramo: ESTIMADO o ASIGNADO -> INICIADO
     */
    @Transactional
    public Tramo iniciarTramo(Long id, IniciarTramoDTO dto) {
        Tramo tramo = obtenerTramoPorId(id);

        // Permite iniciar si está ESTIMADO o ASIGNADO
        if (tramo.getEstado() != EstadoTramo.ASIGNADO && tramo.getEstado() != EstadoTramo.ESTIMADO) {
            throw new InvalidTramoDataException("Solo se puede iniciar un tramo en estado ESTIMADO o ASIGNADO.");
        }

        tramo.setEstado(EstadoTramo.INICIADO);
        return tramoRepository.save(tramo);
    }

    /**
     * Finaliza un tramo: INICIADO -> FINALIZADO
     */
    @Transactional
    public Tramo finalizarTramo(Long id, FinalizarTramoDTO dto) {
        Tramo tramo = obtenerTramoPorId(id);

        if (tramo.getEstado() != EstadoTramo.INICIADO) {
            throw new InvalidTramoDataException("Solo se puede finalizar un tramo en estado INICIADO.");
        }

        tramo.setEstado(EstadoTramo.FINALIZADO);
        return tramoRepository.save(tramo);
    }

    // =========================================================
    // ============ PUNTO 11: Asignar Camión a Tramo ===========
    // =========================================================

    @Transactional
    public Tramo asignarCamion(Long tramoId, Long camionId) {
        // Implementación mínima para compilar y responder al controller.
        Tramo tramo = obtenerTramoPorId(tramoId);
        Camion camion = camionRepository.findById(camionId)
        .orElseThrow(() -> new RuntimeException("Camión no encontrado con ID: " + camionId));
        // 2. Buscar la Ruta para obtener el SolicitudID
        Ruta rutaDelTramo = rutaRepository.findByTramosContains(tramo)
            .orElseThrow(() -> new RuntimeException("Ruta no encontrada para el tramo: " + tramoId));
    
        Long solicitudId = rutaDelTramo.getSolicitudId();

        ContenedorDTO contenedor = solicitudesClient.getContenedorDeSolicitud(solicitudId);

    // --- 4. LA VALIDACIÓN (RF 11) ---
        if (camion.getCapacidadPeso() < contenedor.getPeso()) {
            // ¡ESTO ES LO QUE DEBERÍA HABERSE EJECUTADO!
            throw new RuntimeException("VALIDACIÓN FALLIDA: El peso del contenedor supera al camión.");
        }

        if (camion.getCapacidadVolumen() < contenedor.getVolumen()) {
            throw new RuntimeException("VALIDACIÓN FALLIDA: El volumen del contenedor supera al camión.");
        }
    
        // 5. Asignar SÓLO SI la validación pasa
        tramo.setCamion(camion);
        tramo.setEstado(EstadoTramo.ASIGNADO);
    

        // Validar capacidad de camión

        return tramoRepository.save(tramo);
    }
}
