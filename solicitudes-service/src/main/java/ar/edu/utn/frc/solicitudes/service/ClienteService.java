package ar.edu.utn.frc.solicitudes.service;

import ar.edu.utn.frc.solicitudes.domain.Cliente;
import ar.edu.utn.frc.solicitudes.domain.dto.CrearClienteDTO;
import ar.edu.utn.frc.solicitudes.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Service // Marca esta clase como la capa de lógica de negocio
@RequiredArgsConstructor // Inyección por constructor con Lombok
public class ClienteService {

    // Inyección del Repository
    private final ClienteRepository clienteRepository;

    // Método para crear un nuevo cliente a partir del DTO
    public Cliente crearCliente(CrearClienteDTO dto) {
        
        // 1. Lógica de Negocio (Ej: Verificar si el email ya existe)
        // ...
        
        // 2. Mapear DTO a la Entidad de Dominio (Cliente)
        Cliente nuevoCliente = new Cliente();
        nuevoCliente.setNombre(dto.getNombre());
        nuevoCliente.setEmail(dto.getEmail());
        nuevoCliente.setTelefono(dto.getTelefono());
        
        // 3. Persistir la entidad usando el Repository
        return clienteRepository.save(nuevoCliente);
    }
    
    // Método para obtener un cliente por ID (necesario para las otras relaciones)
    public Cliente obtenerClientePorId(Long id) {
        // Usa el método findById de JpaRepository
        // Lanza una excepción si no lo encuentra.
        return clienteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));
    }

    /**
     * Obtiene todos los clientes
     * @return Lista de todos los clientes
     */
    public List<Cliente> obtenerTodosLosClientes() {
        return clienteRepository.findAll();
    }

    /**
     * Busca un cliente por email. Si no existe, lo crea.
     * @param nombre Nombre del cliente
     * @param email Email del cliente (usado para buscar)
     * @param telefono Teléfono del cliente
     * @return Cliente existente o nuevo cliente creado
     */
    @jakarta.transaction.Transactional
    public Cliente buscarOCrearCliente(String nombre, String email, Integer telefono) {
        return clienteRepository.findByEmail(email)
            .orElseGet(() -> {
                // Si no existe, crear nuevo cliente
                Cliente nuevoCliente = new Cliente();
                nuevoCliente.setNombre(nombre);
                nuevoCliente.setEmail(email);
                nuevoCliente.setTelefono(telefono);
                return clienteRepository.save(nuevoCliente);
            });
    }
}