package ar.edu.utn.frc.solicitudes.domain.tipo;

public enum EstadoSolicitud {
    
    /**
     * El Cliente inició el pedido pero no lo confirmó.
     * Corresponde a "borrador". 
     */
    BORRADOR, 

    /**
     * El Administrador revisó la solicitud y le asignó una ruta.
     * Corresponde a "programada". [cite: 44, 47]
     */
    PROGRAMADA,

    /**
     * El primer tramo de la ruta ya se inició.
     * Corresponde a "en tránsito". [cite: 44, 51]
     */
    EN_TRANSITO,

    /**
     * El último tramo finalizó y se registraron los costos reales.
     * Corresponde a "entregada". [cite: 44, 56]
     */
    ENTREGADA,

    /**
     * Opcional, pero recomendado: La solicitud fue cancelada 
     * (antes de iniciar o durante el tránsito).
     */
    CANCELADA 
}