package ar.edu.utn.frc.rutas.service;

import ar.edu.utn.frc.rutas.domain.Ruta;
import ar.edu.utn.frc.rutas.domain.Tramo;
import ar.edu.utn.frc.rutas.domain.dto.RutaTentativaDTO;
import ar.edu.utn.frc.rutas.domain.CrearRutaDTO;
import ar.edu.utn.frc.rutas.domain.dto.DistanciaDTO;
import ar.edu.utn.frc.rutas.domain.Deposito;
import ar.edu.utn.frc.rutas.exceptions.ResourceNotFoundException;
import ar.edu.utn.frc.rutas.infraestructure.SolicitudesClient;
import ar.edu.utn.frc.rutas.infraestructure.TarifasClient;
import ar.edu.utn.frc.rutas.infraestructure.DistanciasClient;
import ar.edu.utn.frc.rutas.repository.DepositoRepository;
import ar.edu.utn.frc.rutas.repository.RutaRepository;
import ar.edu.utn.frc.rutas.repository.TramoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RutaService {

    private final RutaRepository rutaRepository;
    private final TramoRepository tramoRepository; // Para buscar los tramos
    private final DepositoRepository depositoRepository;

    
    private final SolicitudesClient solicitudesClient;
    private final DistanciasClient distanciasClient;
    private final TarifasClient tarifasClient;

    @Transactional
    public Ruta crearRuta(CrearRutaDTO dto) {
        
        // 1. Buscar las entidades Tramo
        // Usamos findAllById para obtenerlos.
        List<Tramo> tramos = tramoRepository.findAllById(dto.getTramoIds());

        // 2. Opcional: Validar que se encontraron todos
        if (tramos.size() != dto.getTramoIds().size()) {
            throw new ResourceNotFoundException("Uno o más Tramos no fueron encontrados.");
        }
        
        // 3. Opcional: Re-ordenar la lista de 'tramos' para que coincida con el orden de 'dto.tramoIds'
        // findAllById no garantiza el orden.
        List<Tramo> tramosOrdenados = dto.getTramoIds().stream()
            .map(id -> tramos.stream().filter(t -> t.getId().equals(id)).findFirst().orElse(null))
            .collect(Collectors.toList());

        // 4. Crear la nueva Ruta
        Ruta nuevaRuta = new Ruta();
        nuevaRuta.setSolicitudId(dto.getSolicitudId());
        nuevaRuta.setTramos(tramosOrdenados);
        nuevaRuta.setDireccionOrigen(dto.getDireccionOrigen());
        nuevaRuta.setDireccionDestino(dto.getDireccionDestino());
        nuevaRuta.setLatitudOrigen(dto.getLatitudOrigen());
        nuevaRuta.setLongitudOrigen(dto.getLongitudOrigen());
        nuevaRuta.setLatitudDestino(dto.getLatitudDestino());
        nuevaRuta.setLongitudDestino(dto.getLongitudDestino());
        
        // 5. Guardar
        // 'rutaGuardada' tiene el ID, pero sus relaciones anidadas aún son LAZY.
        Ruta rutaGuardada = rutaRepository.save(nuevaRuta);


        // 6. ¡LA CORRECCIÓN!
        // Volvemos a buscar la ruta usando su ID y la query de FETCH
        // (findByIdWithTramos) para forzar la carga de todas las asociaciones
        // anidadas (Tramo -> Deposito, etc.) ANTES de que termine la transacción.
        // Usamos .get() porque SABEMOS que la ruta existe.
        return rutaRepository.findByIdWithTramos(rutaGuardada.getId()).get();
    }

    @Transactional(readOnly = true)
    public Ruta obtenerRutaPorId(Long id) {
        // Llama al nuevo método en lugar de findById()
        return rutaRepository.findByIdWithTramos(id) 
            .orElseThrow(() -> new ResourceNotFoundException("Ruta no encontrada con ID: " + id));
    }

    // Usamos el método con FETCH del repositorio
    @Transactional(readOnly = true)
    public List<Ruta> obtenerTodasLasRutas() {
        return rutaRepository.findAllWithTramos();
    }

    public List<RutaTentativaDTO> consultarRutasTentativas(Long solicitudId) {
        
        List<RutaTentativaDTO> sugerencias = new ArrayList<>();

        // 1. OBTENER DATOS de solicitudes-service
        SolicitudesClient.SolicitudDetallesDTO solicitud = solicitudesClient.getDetallesDeSolicitud(solicitudId);
        
        // --- ARREGLO 2: Dejar como double ---
        double volumen = solicitud.getContenedor().getVolumen();
        double latOrigen = solicitud.getOrigenLat();
        double lonOrigen = solicitud.getOrigenLon();
        double latDestino = solicitud.getDestinoLat();
        double lonDestino = solicitud.getDestinoLon();

        // 2. OBTENER COSTO de tarifas-service
        double costoPorKm = tarifasClient.costoPorKmParaVolumen(volumen);

        // --- 3. SUGERENCIA 1: RUTA DIRECTA ---
        try {
            // --- ARREGLO 1 y 2 ---
            DistanciaDTO distDirecta = distanciasClient.getDistancia(
                    latOrigen, lonOrigen, latDestino, lonDestino);
            
            RutaTentativaDTO rutaDirecta = new RutaTentativaDTO();
            // ... (resto de tu lógica para armar la ruta directa) ...
            rutaDirecta.setTipoRuta("Ruta Directa");
            rutaDirecta.setKilometrosTotales(distDirecta.getKilometros());
            rutaDirecta.setTiempoEstimado(distDirecta.getTiempoAprox());
            rutaDirecta.setCostoEstimado(distDirecta.getKilometros() * costoPorKm);
            rutaDirecta.setTramosSugeridos(List.of("Tramo: Origen Directo -> Destino"));
            
            sugerencias.add(rutaDirecta);

        } catch (Exception e) {
            // No se pudo calcular
        }

        // --- 4. SUGERENCIA 2: RUTA CON 1 PARADA (Depósito) ---
        List<Deposito> depositos = depositoRepository.findAll();
        
        for (Deposito parada : depositos) {
            try {
                // --- ARREGLO 2: Dejar como double ---
                double paradaLat = parada.getLatitud();
                double paradaLon = parada.getLongitud();

                // --- ARREGLO 1 y 2 ---
                DistanciaDTO tramo1 = distanciasClient.getDistancia(
                    latOrigen, lonOrigen, paradaLat, paradaLon);
                
                DistanciaDTO tramo2 = distanciasClient.getDistancia(
                    paradaLat, paradaLon, latDestino, lonDestino);
                
                // --- ¡AQUÍ ESTÁ EL CÓDIGO QUE FALTABA! ---
            
            // 1. Calculas los totales
            double kmTotales = tramo1.getKilometros() + tramo2.getKilometros();
            double costoEstadia = parada.getCostoEstadiaDiario() != null ? parada.getCostoEstadiaDiario() : 0.0;

            // 2. Creas el DTO de respuesta
            RutaTentativaDTO rutaConParada = new RutaTentativaDTO();
            rutaConParada.setTipoRuta("Ruta con 1 Parada (Depósito: " + parada.getNombre() + ")");
            rutaConParada.setKilometrosTotales(kmTotales);
            rutaConParada.setTiempoEstimado(tramo1.getTiempoAprox() + " + " + tramo2.getTiempoAprox() + " + 1 día estadía"); 
            rutaConParada.setCostoEstimado((kmTotales * costoPorKm) + costoEstadia);
            rutaConParada.setTramosSugeridos(List.of(
                "Tramo 1: Origen -> " + parada.getNombre(),
                "Tramo 2: " + parada.getNombre() + " -> Destino"
            ));
            
            // 3. ¡Lo agregas a la lista de sugerencias!
            sugerencias.add(rutaConParada);
                
            } catch (Exception e) {
                // No se pudo calcular esta ruta
                // No se pudo calcular esta ruta, mostramos el error
                System.out.println("ERROR al calcular ruta con parada: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        return sugerencias;
    }
}