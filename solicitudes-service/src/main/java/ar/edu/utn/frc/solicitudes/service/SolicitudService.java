package ar.edu.utn.frc.solicitudes.service;

import ar.edu.utn.frc.solicitudes.domain.Contenedor;
import ar.edu.utn.frc.solicitudes.domain.Cliente;
import ar.edu.utn.frc.solicitudes.domain.Solicitud;
import ar.edu.utn.frc.solicitudes.domain.dto.AsignarRutaDTO;
import ar.edu.utn.frc.solicitudes.domain.dto.CrearSolicitudDTO;
import ar.edu.utn.frc.solicitudes.domain.tipo.EstadoSolicitud;
import ar.edu.utn.frc.solicitudes.exceptions.ResourceNotFoundException;
import ar.edu.utn.frc.solicitudes.infrastructure.TarifasClient;
import ar.edu.utn.frc.solicitudes.repository.SolicitudRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SolicitudService {

    private final SolicitudRepository solicitudRepository;
    private final ContenedorService contenedorService;
    private final ClienteService clienteService;

    // Cliente HTTP al microservicio de tarifas (puntos 8 y 9)
    private final TarifasClient tarifasClient;

    // ================== Crear solicitud ==================
    @Transactional
    public Solicitud crearSolicitud(CrearSolicitudDTO dto) {

        // 1) Buscar o crear cliente
        Cliente cliente = clienteService.buscarOCrearCliente(
                dto.getClienteNombre(),
                dto.getClienteEmail(),
                dto.getClienteTelefono()
        );

        // 2) Buscar o crear contenedor
        Contenedor contenedor;
        if (dto.getContenedorId() != null) {
            contenedor = contenedorService.obtenerContenedorPorId(dto.getContenedorId());
            contenedor.setCliente(cliente);
            if (dto.getContenedorPeso() != null) contenedor.setPeso(dto.getContenedorPeso());
            if (dto.getContenedorVolumen() != null) contenedor.setVolumen(dto.getContenedorVolumen());
            contenedor = contenedorService.guardarContenedor(contenedor);
        } else {
            Contenedor nuevoContenedor = new Contenedor();
            nuevoContenedor.setPeso(dto.getContenedorPeso());
            nuevoContenedor.setVolumen(dto.getContenedorVolumen());
            nuevoContenedor.setCliente(cliente);
            contenedor = contenedorService.guardarContenedor(nuevoContenedor);
        }

        // 3) Crear solicitud
        Solicitud nuevaSolicitud = new Solicitud();
        nuevaSolicitud.setCostoEstimado(dto.getCostoEstimado());
        nuevaSolicitud.setTiempoEstimado(dto.getTiempoEstimado());
        nuevaSolicitud.setCostoFinal(dto.getCostoFinal());
        nuevaSolicitud.setTiempoFinal(dto.getTiempoFinal());
        nuevaSolicitud.setOrigenDireccion(dto.getOrigenDireccion());
        nuevaSolicitud.setOrigenLat(dto.getOrigenLat());
        nuevaSolicitud.setOrigenLon(dto.getOrigenLon());
        nuevaSolicitud.setDestinoDireccion(dto.getDestinoDireccion());
        nuevaSolicitud.setDestinoLat(dto.getDestinoLat());
        nuevaSolicitud.setDestinoLon(dto.getDestinoLon());
        
        // Estado por defecto queda BORRADOR según tu entidad

        // 4) Vincular contenedor
        nuevaSolicitud.setContenedor(contenedor);

        // 5) Guardar
        return solicitudRepository.save(nuevaSolicitud);
    }

    // ================== Asignar ruta ==================
    @Transactional
    public Solicitud asignarRuta(Long solicitudId, AsignarRutaDTO dto) {

        Solicitud solicitud = this.obtenerSolicitudPorId(solicitudId);

        if (solicitud.getRutaId() != null) {
            throw new IllegalStateException("La solicitud " + solicitudId + " ya tiene una ruta asignada.");
        }

        solicitud.setRutaId(dto.getRutaId());
        solicitud.setEstadoSolicitud(EstadoSolicitud.PROGRAMADA);

        return solicitudRepository.save(solicitud);
    }

    // ================== Obtener por ID ==================
    public Solicitud obtenerSolicitudPorId(Long id) {
        return solicitudRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitud no encontrada con ID: " + id));
    }

    // ================== Puntos 8 y 9 ==================
    @Transactional
    public Solicitud finalizarSolicitud(Long id) {
        // 1) Buscar la solicitud
        Solicitud s = solicitudRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada: " + id));

        // 2) Obtener el VOLUMEN del contenedor
        double volumen = 0.0;
        try {
            if (s.getContenedor() != null && s.getContenedor().getVolumen() != null) {
                volumen = Double.parseDouble(String.valueOf(s.getContenedor().getVolumen()));
            }
        } catch (Exception ignored) { volumen = 0.0; }

        // 3) Obtenemos el costo por km según el volumen
        double costoPorKm = tarifasClient.costoPorKmParaVolumen(volumen);

        // 4) “Distancia” proxy con tiempoEstimado (convierte a int aunque venga como String)
        int tiempoEstimadoInt = 0;
        try {
            if (s.getTiempoEstimado() != null) {
                tiempoEstimadoInt = Integer.parseInt(String.valueOf(s.getTiempoEstimado()));
            }
        } catch (Exception ignored) { tiempoEstimadoInt = 0; }

        double distanciaKm = Math.max(1, tiempoEstimadoInt); // evita 0
        double costoCalculado = costoPorKm * distanciaKm;

        // 5) Persistir costo y tiempo final (tiempoFinal en tu entidad es String)
        s.setCostoFinal(costoCalculado);
        s.setTiempoFinal(String.valueOf(tiempoEstimadoInt));

        // 6) Estado final (según RF: entregada al finalizar)
        s.setEstadoSolicitud(EstadoSolicitud.ENTREGADA);

        return solicitudRepository.save(s);
    }
}
