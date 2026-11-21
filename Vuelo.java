import java.util.Objects;

/**
 * Representa un vuelo programado del día.
 * El código es el identificador único del vuelo.
 */
public class Vuelo {
    private final String codigo;
    private final String destino;
    private final String puerta;

    public Vuelo(String codigo, String destino, String puerta) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código de vuelo no puede estar vacío.");
        }
        this.codigo = codigo.trim().toUpperCase();
        this.destino = destino != null ? destino.trim() : "";
        this.puerta = puerta != null ? puerta.trim().toUpperCase() : "";
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDestino() {
        return destino;
    }

    public String getPuerta() {
        return puerta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vuelo)) return false;
        Vuelo vuelo = (Vuelo) o;
        return Objects.equals(codigo, vuelo.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public String toString() {
        return String.format("Vuelo %s -> Destino: %s, Puerta: %s", codigo, destino, puerta);
    }
}
