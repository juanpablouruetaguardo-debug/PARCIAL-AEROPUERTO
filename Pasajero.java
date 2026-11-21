import java.util.Objects;

/**
 * Representa a un pasajero en el sistema del aeropuerto.
 * El pasaporte es el identificador único del pasajero.
 */
public class Pasajero {
    private final String pasaporte;
    private final String nombre;
    private final String apellido;

    public Pasajero(String pasaporte, String nombre, String apellido) {
        if (pasaporte == null || pasaporte.trim().isEmpty()) {
            throw new IllegalArgumentException("El pasaporte no puede ser vacío.");
        }
        this.pasaporte = pasaporte.trim();
        this.nombre = nombre != null ? nombre.trim() : "";
        this.apellido = apellido != null ? apellido.trim() : "";
    }

    public String getPasaporte() {
        return pasaporte;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pasajero)) return false;
        Pasajero pasajero = (Pasajero) o;
        return Objects.equals(pasaporte.toUpperCase(), pasajero.pasaporte.toUpperCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(pasaporte.toUpperCase());
    }

    @Override
    public String toString() {
        return String.format("%s %s (Pasaporte: %s)", nombre, apellido, pasaporte);
    }
}
