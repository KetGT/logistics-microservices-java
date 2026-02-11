package ar.edu.utn.frc.rutas.domain.tipo;

public enum EstadoTramo {
    ESTIMADO,   // Recién creado, sin camión, costos aprox.
    ASIGNADO,   // Camión asignado, fechas por iniciar
    INICIADO,   // Viaje en curso
    FINALIZADO  // Viaje completado, costos reales calculados
}