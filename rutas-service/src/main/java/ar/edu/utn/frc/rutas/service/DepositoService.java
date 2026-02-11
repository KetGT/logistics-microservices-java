package ar.edu.utn.frc.rutas.service;

import ar.edu.utn.frc.rutas.domain.Deposito;
import ar.edu.utn.frc.rutas.domain.CrearDepositoDTO;
import ar.edu.utn.frc.rutas.repository.DepositoRepository;
// Asumiendo que tienes un paquete de excepciones similar al otro servicio
import ar.edu.utn.frc.rutas.exceptions.ResourceNotFoundException; 
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DepositoService {

    private final DepositoRepository depositoRepository;

    @Transactional
    public Deposito crearDeposito(CrearDepositoDTO dto) {
        
        // Mapeo de DTO a Entidad
        Deposito nuevoDeposito = new Deposito();
        nuevoDeposito.setNombre(dto.getNombre());
        nuevoDeposito.setDireccionTextual(dto.getDireccionTextual());
        nuevoDeposito.setLatitud(dto.getLatitud());
        nuevoDeposito.setLongitud(dto.getLongitud());
        nuevoDeposito.setCostoEstadiaDiario(dto.getCostoEstadiaDiario());

        // Guardar en la BBDD
        return depositoRepository.save(nuevoDeposito);
    }

    /**
     * Busca un depósito por su ID.
     * Lanza ResourceNotFoundException si no lo encuentra (para un 404).
     */
    public Deposito obtenerDepositoPorId(Long id) {
        return depositoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Deposito no encontrado con ID: " + id));
    }

    @Transactional
    public Deposito actualizarDeposito(Long id, CrearDepositoDTO dto) {
        Deposito deposito = depositoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Deposito no encontrado con ID: " + id));
        
        deposito.setNombre(dto.getNombre());
        deposito.setDireccionTextual(dto.getDireccionTextual());
        deposito.setLatitud(dto.getLatitud());
        deposito.setLongitud(dto.getLongitud());
        deposito.setCostoEstadiaDiario(dto.getCostoEstadiaDiario());
        
        return depositoRepository.save(deposito);
    }

    public List<Deposito> obtenerTodosLosDepositos() {
        return depositoRepository.findAll();
    }
}