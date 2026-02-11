package ar.edu.utn.frc.rutas.repository;

import ar.edu.utn.frc.rutas.domain.Camion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CamionRepository extends JpaRepository<Camion, Long> {

    /**
     * Busca un camión por su dominio (patente).
     * Lo usamos para verificar la unicidad antes de crear uno nuevo.
     * @param dominio La patente a buscar.
     * @return Un Optional que contiene el Camion si se encuentra.
     */
    Optional<Camion> findByDominio(String dominio);
}