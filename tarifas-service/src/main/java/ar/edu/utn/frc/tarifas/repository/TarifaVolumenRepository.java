// En: tarifas-service/src/main/java/ar/edu/utn/frc/tarifas/repository/TarifaVolumenRepository.java

package ar.edu.utn.frc.tarifas.repository;

import ar.edu.utn.frc.tarifas.domain.TarifaVolumen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TarifaVolumenRepository extends JpaRepository<TarifaVolumen, Long> {

    /**
     * Busca una tarifa donde el volumen dado esté entre el min y el max.
     * Ej: findByVolumen(25) -> encuentra la fila (volumenMin=0, volumenMax=30)
     */
    Optional<TarifaVolumen> findByVolumenMinLessThanEqualAndVolumenMaxGreaterThanEqual(
        Double volumen, Double volumen2);
}