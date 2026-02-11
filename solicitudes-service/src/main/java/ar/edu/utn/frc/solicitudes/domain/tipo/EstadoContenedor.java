package ar.edu.utn.frc.solicitudes.domain.tipo;

public enum EstadoContenedor {

    /**
     * El contenedor está en la ubicación del cliente, esperando ser recogido.
     * Corresponde a "no retirado". 
     */
    PENDIENTE_RETIRO,

    /**
     * El contenedor está actualmente en un camión moviéndose.
     * Corresponde a "en viaje". 
     */
    EN_TRANSITO,

    /**
     * El contenedor fue descargado en una ubicación intermedia.
     * Corresponde a "en un depósito". 
     */
    EN_DEPOSITO,

    /**
     * El contenedor llegó a su destino final.
     * Corresponde a "ya fue entregado". 
     */
    ENTREGADO
}