package ar.edu.utn.frc.solicitudes.service;

import ar.edu.utn.frc.solicitudes.domain.Contenedor;
import ar.edu.utn.frc.solicitudes.domain.tipo.EstadoContenedor;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class ContenedorSpecification {

    public static Specification<Contenedor> withFilters(EstadoContenedor estado, Long clienteId) {
        return (root, query, cb) -> {
            
            List<Predicate> predicates = new ArrayList<>();

            // 1. FILTRO BASE: "Pendientes de entrega"
            // Siempre excluimos los que ya fueron ENTREGADOS.
            predicates.add(cb.notEqual(root.get("estado"), EstadoContenedor.ENTREGADO));

            // 2. FILTRO OPCIONAL: Por estado específico
            if (estado != null) {
                // Si el usuario pasa un estado, filtramos por ese
                predicates.add(cb.equal(root.get("estado"), estado));
            }

            // 3. FILTRO OPCIONAL: Por cliente
            if (clienteId != null) {
                // root.get("cliente") accede al campo 'cliente' de Contenedor
                // .get("id") accede al 'id' dentro de ese Cliente
                predicates.add(cb.equal(root.get("cliente").get("id"), clienteId));
            }

            // Combinamos todos los predicados con un AND
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}