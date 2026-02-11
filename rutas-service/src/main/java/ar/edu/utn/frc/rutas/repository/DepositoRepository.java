package ar.edu.utn.frc.rutas.repository;

import ar.edu.utn.frc.rutas.domain.Deposito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepositoRepository extends JpaRepository<Deposito, Long> {
    // Métodos CRUD (save, findById, findAll, delete) ya incluidos.
}