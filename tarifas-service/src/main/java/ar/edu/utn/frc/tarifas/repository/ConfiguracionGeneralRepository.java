// En: tarifas-service/src/main/java/ar/edu/utn/frc/tarifas/repository/ConfiguracionGeneralRepository.java

package ar.edu.utn.frc.tarifas.repository;

import ar.edu.utn.frc.tarifas.domain.ConfiguracionGeneral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfiguracionGeneralRepository extends JpaRepository<ConfiguracionGeneral, Long> {

    /**
     * Busca la primera configuración disponible (asumimos que solo hay una).
     */
    Optional<ConfiguracionGeneral> findFirstByOrderByIdAsc();
}