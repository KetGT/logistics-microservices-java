package ar.edu.utn.frc.solicitudes.repository;

import ar.edu.utn.frc.solicitudes.domain.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {
    // Spring Data JPA se encarga de los métodos CRUD (save, findById, etc.)
}