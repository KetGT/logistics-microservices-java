package ar.edu.utn.frc.rutas.repository;

import ar.edu.utn.frc.rutas.domain.Ruta;
import ar.edu.utn.frc.rutas.domain.Tramo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RutaRepository extends JpaRepository<Ruta, Long> {

    /**
     * Busca todas las Rutas y trae (FETCH) todas las asociaciones
     * anidadas para evitar LazyInitializationExceptions.
     * * 1. Usamos 'DISTINCT' para evitar Rutas duplicadas por el JOIN.
     * 2. Usamos 't.' para referenciar las asociaciones del Tramo.
     */
    @Query("SELECT DISTINCT r FROM Ruta r " +
           "LEFT JOIN FETCH r.tramos t " +
           "LEFT JOIN FETCH t.origenDeposito " +  // <-- ¡CORREGIDO!
           "LEFT JOIN FETCH t.destinoDeposito " + // <-- ¡CORREGIDO!
           "LEFT JOIN FETCH t.camion")            // <-- ¡CORREGIDO!
    List<Ruta> findAllWithTramos();
    
    /**
     * Busca una Ruta por ID y trae (FETCH) todas las asociaciones anidadas.
     */
    @Query("SELECT DISTINCT r FROM Ruta r " +
           "LEFT JOIN FETCH r.tramos t " +
           "LEFT JOIN FETCH t.origenDeposito " +
           "LEFT JOIN FETCH t.destinoDeposito " +
           "LEFT JOIN FETCH t.camion " +
           "WHERE r.id = :id")
    Optional<Ruta> findByIdWithTramos(Long id);

    Optional<Ruta> findByTramosContains(Tramo tramo);
}