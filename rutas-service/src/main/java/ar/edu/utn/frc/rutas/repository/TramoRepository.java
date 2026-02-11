package ar.edu.utn.frc.rutas.repository;

import ar.edu.utn.frc.rutas.domain.Tramo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TramoRepository extends JpaRepository<Tramo, Long> {

    /**
     * Lista todos los tramos con sus asociaciones necesarias para serializar
     * sin LazyInitializationException (origenDeposito, destinoDeposito, camion).
     */
    @Query("""
        select t
        from Tramo t
        left join fetch t.origenDeposito
        left join fetch t.destinoDeposito
        left join fetch t.camion
    """)
    List<Tramo> findAllWithAssociations();

    /**
     * Busca un tramo por id trayendo las asociaciones (fetch) para poder
     * devolverlo por el endpoint /api/tramos/{id} sin lazy exceptions.
     */
    @Query("""
        select t
        from Tramo t
        left join fetch t.origenDeposito
        left join fetch t.destinoDeposito
        left join fetch t.camion
        where t.id = :id
    """)
    Optional<Tramo> findByIdWithAssociations(@Param("id") Long id);
}
