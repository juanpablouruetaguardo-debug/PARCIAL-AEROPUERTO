/**
 * Representa la solicitud de un avión que pide permiso para aterrizar.
 * Prioridades:
 *  1: Emergencia
 *  2: Combustible bajo
 *  3: Normal
 * 
 * Número de llegada: Asignado automáticamente en orden correlativo (1, 2, 3...)
 */
public class SolicitudAterrizaje implements Comparable<SolicitudAterrizaje> {
    private final String codigoVuelo;
    private final int prioridad;
    private final int numeroLlegada;

    public SolicitudAterrizaje(String codigoVuelo, int prioridad, int numeroLlegada) {
        if (codigoVuelo == null || codigoVuelo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código de vuelo no puede estar vacío.");
        }
        if (prioridad < 1 || prioridad > 3) {
            throw new IllegalArgumentException("La prioridad debe ser 1 (Emergencia), 2 (Combustible bajo) o 3 (Normal).");
        }
        this.codigoVuelo = codigoVuelo.trim().toUpperCase();
        this.prioridad = prioridad;
        this.numeroLlegada = numeroLlegada;
    }

    public String getCodigoVuelo() {
        return codigoVuelo;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public int getNumeroLlegada() {
        return numeroLlegada;
    }

    public String getDescripcionPrioridad() {
        return switch (prioridad) {
            case 1 -> "1 (Emergencia)";
            case 2 -> "2 (Combustible bajo)";
            case 3 -> "3 (Normal)";
            default -> String.valueOf(prioridad);
        };
    }

    @Override
    public int compareTo(SolicitudAterrizaje otro) {
        // Primero por prioridad (menor número = mayor urgencia)
        if (this.prioridad != otro.prioridad) {
            return Integer.compare(this.prioridad, otro.prioridad);
        }
        // Desempate: el que llegó primero (menor número de llegada)
        return Integer.compare(this.numeroLlegada, otro.numeroLlegada);
    }

    @Override
    public String toString() {
        return String.format("Vuelo %s [Prioridad: %s, Solicitud #%d]",
                codigoVuelo, getDescripcionPrioridad(), numeroLlegada);
    }
}
