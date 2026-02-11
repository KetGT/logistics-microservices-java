package ar.edu.utn.frc.solicitudes.repository;

import ar.edu.utn.frc.solicitudes.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
// Cliente es la Entidad, Long es el tipo de su clave primaria (ID)
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    // Buscar cliente por email
    Optional<Cliente> findByEmail(String email);
}