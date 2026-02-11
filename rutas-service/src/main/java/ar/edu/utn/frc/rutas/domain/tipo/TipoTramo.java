package ar.edu.utn.frc.rutas.domain.tipo;

public enum TipoTramo {
    ORIGEN_DEPOSITO,  // De cliente (origen Solicitud) a primer depósito
    DEPOSITO_DEPOSITO, // Entre dos depósitos
    DEPOSITO_DESTINO, // De último depósito a cliente (destino Solicitud)
    ORIGEN_DESTINO    // Viaje directo cliente-cliente
}