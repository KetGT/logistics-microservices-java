package ar.edu.utn.frc.solicitudes.repository;

import ar.edu.utn.frc.solicitudes.domain.Contenedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ContenedorRepository extends JpaRepository<Contenedor, Long>,JpaSpecificationExecutor<Contenedor> {
    // Aquí puedes agregar métodos personalizados si necesitas buscar contenedores
}