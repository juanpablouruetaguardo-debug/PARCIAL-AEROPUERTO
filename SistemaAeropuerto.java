import java.util.*;

/**
 * Clase central que gestiona la operación del aeropuerto mediante las 8 colecciones
 * requeridas por el enunciado (RF-01 a RF-08).
 */
public class SistemaAeropuerto {

    // =========================================================================
    // COLECCIÓN 1: List<Pasajero> (ArrayList) - RF-01: Pasajeros del vuelo
    // =========================================================================
    private final List<Pasajero> listaPasajerosVuelo = new ArrayList<>();

    // =========================================================================
    // COLECCIÓN 2: LinkedList<String> - RF-02: Ruta de escalas
    // =========================================================================
    private final LinkedList<String> rutaEscalas = new LinkedList<>();

    // =========================================================================
    // COLECCIÓN 3: Set<String> (HashSet) - RF-03: Check-in de pasaportes
    // =========================================================================
    private final Set<String> checkInPasaportes = new HashSet<>();

    // =========================================================================
    // COLECCIÓN 4: Set<String> (LinkedHashSet) - RF-04: Destinos del día
    // =========================================================================
    private final Set<String> destinosDelDia = new LinkedHashSet<>();

    // =========================================================================
    // COLECCIÓN 5: PriorityQueue<SolicitudAterrizaje> - RF-05: Aterrizajes por prioridad
    // =========================================================================
    private final PriorityQueue<SolicitudAterrizaje> colaAterrizajes = new PriorityQueue<>();
    private int contadorLlegadaAterrizaje = 1;

    // =========================================================================
    // COLECCIÓN 6: Deque<Pasajero> (ArrayDeque) - RF-06: Fila de check-in
    // =========================================================================
    private final Deque<Pasajero> filaCheckIn = new ArrayDeque<>();

    // =========================================================================
    // COLECCIÓN 7: Map<String, Vuelo> (HashMap) - RF-07: Vuelos por código
    // =========================================================================
    private final Map<String, Vuelo> vuelosRegistrados = new HashMap<>();

    // =========================================================================
    // COLECCIÓN 8: LinkedHashMap<String, Vuelo> - RF-08: Consulta rápida (Cache LRU máx 3)
    // =========================================================================
    private final LinkedHashMap<String, Vuelo> cacheConsultaRapida = new LinkedHashMap<>(16, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, Vuelo> eldest) {
            return size() > 3;
        }
    };

    // =========================================================================
    // MÉTODOS RF-01 · Pasajeros del vuelo
    // =========================================================================

    /**
     * Agrega un pasajero al final de la lista del vuelo.
     */
    public void agregarPasajeroVuelo(Pasajero pasajero) {
        if (pasajero == null) {
            System.out.println(">> Error: No se puede agregar un pasajero nulo.");
            return;
        }
        listaPasajerosVuelo.add(pasajero);
        System.out.println(">> Éxito: Pasajero agregado al final del vuelo: " + pasajero);
    }

    /**
     * Muestra los pasajeros ordenados por apellido, sin cambiar el orden en que llegaron.
     */
    public void mostrarPasajerosOrdenadosPorApellido() {
        if (listaPasajerosVuelo.isEmpty()) {
            System.out.println(">> Aviso: No hay pasajeros registrados en la lista del vuelo.");
            return;
        }

        // Se crea una copia para NO alterar el orden original de llegada
        List<Pasajero> ordenados = new ArrayList<>(listaPasajerosVuelo);
        ordenados.sort(Comparator
                .comparing(Pasajero::getApellido, String.CASE_INSENSITIVE_ORDER)
                .thenComparing(Pasajero::getNombre, String.CASE_INSENSITIVE_ORDER));

        System.out.println(">> Pasajeros del vuelo ordenados por apellido (el orden de llegada se conserva):");
        for (int i = 0; i < ordenados.size(); i++) {
            System.out.println("   " + (i + 1) + ". " + ordenados.get(i).getApellido() + ", "
                    + ordenados.get(i).getNombre() + " (Pasaporte: " + ordenados.get(i).getPasaporte() + ")");
        }
    }

    /**
     * Muestra la lista original en orden de llegada (para verificar que no cambió).
     */
    public void mostrarPasajerosOrdenLlegada() {
        if (listaPasajerosVuelo.isEmpty()) {
            System.out.println(">> Aviso: No hay pasajeros en la lista del vuelo.");
            return;
        }
        System.out.println(">> Pasajeros del vuelo en orden de llegada:");
        for (int i = 0; i < listaPasajerosVuelo.size(); i++) {
            System.out.println("   [Posición " + i + "] " + listaPasajerosVuelo.get(i));
        }
    }

    /**
     * Elimina al pasajero que está en una posición dada (la primera es la 0) y lo muestra.
     * Si esa posición no existe, avisa sin fallar.
     */
    public Pasajero eliminarPasajeroPorPosicion(int posicion) {
        if (posicion < 0 || posicion >= listaPasajerosVuelo.size()) {
            System.out.println(">> Aviso: La posición " + posicion + " no existe. " +
                    (listaPasajerosVuelo.isEmpty() ? "La lista está vacía." : "Posiciones válidas: de 0 a " + (listaPasajerosVuelo.size() - 1) + "."));
            return null;
        }
        Pasajero eliminado = listaPasajerosVuelo.remove(posicion);
        System.out.println(">> Éxito: Pasajero eliminado de la posición " + posicion + ": " + eliminado);
        return eliminado;
    }

    /**
     * Muestra cuántos pasajeros hay en la lista del vuelo.
     */
    public int contarPasajerosVuelo() {
        int total = listaPasajerosVuelo.size();
        System.out.println(">> Total de pasajeros en el vuelo: " + total);
        return total;
    }

    // =========================================================================
    // MÉTODOS RF-02 · Ruta de escalas
    // =========================================================================

    /**
     * Agrega una ciudad al final de la ruta.
     */
    public void agregarCiudadFinalRuta(String ciudad) {
        if (ciudad == null || ciudad.trim().isEmpty()) {
            System.out.println(">> Error: El nombre de la ciudad no puede estar vacío.");
            return;
        }
        String ciudadLimpia = ciudad.trim();
        rutaEscalas.add(ciudadLimpia);
        System.out.println(">> Éxito: Ciudad '" + ciudadLimpia + "' agregada al final de la ruta.");
    }

    /**
     * Inserta una ciudad nueva justo después de otra que ya está en la ruta.
     * Si esa ciudad no está, avisa sin fallar.
     */
    public boolean insertarCiudadDespuesDe(String ciudadExistente, String nuevaCiudad) {
        if (ciudadExistente == null || nuevaCiudad == null || ciudadExistente.trim().isEmpty() || nuevaCiudad.trim().isEmpty()) {
            System.out.println(">> Error: Los nombres de ciudad no pueden estar vacíos.");
            return false;
        }
        String existenteLimpia = ciudadExistente.trim();
        String nuevaLimpia = nuevaCiudad.trim();

        int indice = -1;
        for (int i = 0; i < rutaEscalas.size(); i++) {
            if (rutaEscalas.get(i).equalsIgnoreCase(existenteLimpia)) {
                indice = i;
                break;
            }
        }

        if (indice == -1) {
            System.out.println(">> Aviso: La ciudad '" + existenteLimpia + "' no está en la ruta. No se insertó '" + nuevaLimpia + "'.");
            return false;
        }

        rutaEscalas.add(indice + 1, nuevaLimpia);
        System.out.println(">> Éxito: Ciudad '" + nuevaLimpia + "' insertada justo después de '" + rutaEscalas.get(indice) + "'.");
        return true;
    }

    /**
     * Elimina una escala. Si no estaba, avisa sin fallar.
     */
    public boolean eliminarEscala(String ciudad) {
        if (ciudad == null || ciudad.trim().isEmpty()) {
            System.out.println(">> Error: El nombre de la ciudad no puede estar vacío.");
            return false;
        }
        String ciudadLimpia = ciudad.trim();
        Iterator<String> it = rutaEscalas.iterator();
        while (it.hasNext()) {
            if (it.next().equalsIgnoreCase(ciudadLimpia)) {
                it.remove();
                System.out.println(">> Éxito: Escala '" + ciudadLimpia + "' eliminada de la ruta.");
                return true;
            }
        }
        System.out.println(">> Aviso: La escala '" + ciudadLimpia + "' no estaba en la ruta.");
        return false;
    }

    /**
     * Muestra la ruta en orden, de la salida al destino.
     */
    public void mostrarRuta() {
        if (rutaEscalas.isEmpty()) {
            System.out.println(">> Aviso: La ruta no tiene escalas registradas actualmente.");
            return;
        }
        System.out.println(">> Ruta de vuelo (Salida -> Destino): " + String.join(" -> ", rutaEscalas));
    }

    // =========================================================================
    // MÉTODOS RF-03 · Check-in de pasaportes
    // =========================================================================

    /**
     * Registra el check-in de un pasaporte.
     * Un mismo pasaporte no puede hacer check-in dos veces: el sistema avisa.
     */
    public boolean registrarCheckIn(String pasaporte) {
        if (pasaporte == null || pasaporte.trim().isEmpty()) {
            System.out.println(">> Error: El número de pasaporte no puede estar vacío.");
            return false;
        }
        String pLimpio = pasaporte.trim().toUpperCase();
        if (checkInPasaportes.contains(pLimpio)) {
            System.out.println(">> Aviso: El pasaporte " + pLimpio + " ya hizo check-in. Rechazado por repetido.");
            return false;
        }
        checkInPasaportes.add(pLimpio);
        System.out.println(">> Éxito: Check-in completado para el pasaporte " + pLimpio + ".");
        return true;
    }

    /**
     * Consulta si un pasaporte ya hizo check-in y cuántos hay registrados.
     */
    public void consultarCheckIn(String pasaporte) {
        if (pasaporte == null || pasaporte.trim().isEmpty()) {
            System.out.println(">> Error: Ingrese un pasaporte válido.");
            return;
        }
        String pLimpio = pasaporte.trim().toUpperCase();
        if (checkInPasaportes.contains(pLimpio)) {
            System.out.println(">> El pasaporte " + pLimpio + " YA hizo check-in.");
        } else {
            System.out.println(">> El pasaporte " + pLimpio + " NO ha hecho check-in.");
        }
        System.out.println(">> Total de pasaportes con check-in registrado: " + checkInPasaportes.size());
    }

    /**
     * Muestra el total de pasaportes registrados con check-in.
     */
    public int contarCheckIn() {
        int total = checkInPasaportes.size();
        System.out.println(">> Total de pasajeros con check-in: " + total);
        return total;
    }

    // =========================================================================
    // MÉTODOS RF-04 · Destinos del día
    // =========================================================================

    /**
     * Registra el destino de un vuelo.
     * Si es la primera vez que aparece ese día, se anota; si ya estaba, se avisa y no se repite.
     */
    public boolean registrarDestino(String destino) {
        if (destino == null || destino.trim().isEmpty()) {
            System.out.println(">> Error: El destino no puede estar vacío.");
            return false;
        }
        String dLimpio = destino.trim();
        // Búsqueda case-insensitive para no duplicar por mayúsculas/minúsculas
        for (String reg : destinosDelDia) {
            if (reg.equalsIgnoreCase(dLimpio)) {
                System.out.println(">> Aviso: El destino '" + dLimpio + "' ya estaba registrado hoy. No se repite.");
                return false;
            }
        }

        destinosDelDia.add(dLimpio);
        System.out.println(">> Éxito: Destino '" + dLimpio + "' registrado como destino del día.");
        return true;
    }

    /**
     * Muestra los destinos en el orden en que aparecieron por primera vez.
     */
    public void mostrarDestinos() {
        if (destinosDelDia.isEmpty()) {
            System.out.println(">> Aviso: No hay destinos registrados el día de hoy.");
            return;
        }
        System.out.println(">> Destinos del día (en orden de aparición por primera vez):");
        int i = 1;
        for (String d : destinosDelDia) {
            System.out.println("   " + (i++) + ". " + d);
        }
    }

    /**
     * Muestra cuántos destinos distintos hay.
     */
    public int contarDestinosDistintos() {
        int total = destinosDelDia.size();
        System.out.println(">> Total de destinos distintos hoy: " + total);
        return total;
    }

    // =========================================================================
    // MÉTODOS RF-05 · Aterrizajes por prioridad
    // =========================================================================

    /**
     * Recibe la solicitud de aterrizaje de un avión con su prioridad (1, 2 o 3).
     * El sistema asigna automáticamente el número de llegada correlativo.
     */
    public SolicitudAterrizaje recibirSolicitudAterrizaje(String codigoVuelo, int prioridad) {
        if (codigoVuelo == null || codigoVuelo.trim().isEmpty()) {
            System.out.println(">> Error: El código de vuelo no puede estar vacío.");
            return null;
        }
        if (prioridad < 1 || prioridad > 3) {
            System.out.println(">> Error: Prioridad inválida (" + prioridad + "). Debe ser 1 (Emergencia), 2 (Combustible bajo) o 3 (Normal).");
            return null;
        }

        int numeroLlegada = contadorLlegadaAterrizaje++;
        SolicitudAterrizaje solicitud = new SolicitudAterrizaje(codigoVuelo, prioridad, numeroLlegada);
        colaAterrizajes.add(solicitud);
        System.out.println(">> Éxito: Solicitud recibida -> " + solicitud);
        return solicitud;
    }

    /**
     * La torre autoriza primero la solicitud más urgente: prioridad 1 antes que 2, y 2 antes que 3.
     * Si dos tienen la misma prioridad, autoriza la que llegó primero.
     * Si no hay solicitudes, avisa sin fallar.
     */
    public SolicitudAterrizaje autorizarAterrizaje() {
        if (colaAterrizajes.isEmpty()) {
            System.out.println(">> Aviso: No hay solicitudes de aterrizaje pendientes en la torre de control.");
            return null;
        }
        SolicitudAterrizaje autorizada = colaAterrizajes.poll();
        System.out.println(">> Torre de Control AUTORIZA aterrizaje: " + autorizada);
        return autorizada;
    }

    /**
     * Ver quién sigue sin autorizarlo todavía, y cuántas solicitudes esperan.
     * Si no hay solicitudes, avisa sin fallar.
     */
    public void verSiguienteAterrizaje() {
        if (colaAterrizajes.isEmpty()) {
            System.out.println(">> Aviso: No hay solicitudes de aterrizaje pendientes.");
            return;
        }
        SolicitudAterrizaje siguiente = colaAterrizajes.peek();
        System.out.println(">> Siguiente avión en turno para aterrizar: " + siguiente);
        System.out.println(">> Solicitudes esperando en total: " + colaAterrizajes.size());
    }

    // =========================================================================
    // MÉTODOS RF-06 · Fila de check-in
    // =========================================================================

    /**
     * Un pasajero que llega entra por el final de la fila.
     */
    public void entrarFilaCheckIn(Pasajero pasajero) {
        if (pasajero == null) {
            System.out.println(">> Error: Pasajero no válido.");
            return;
        }
        filaCheckIn.addLast(pasajero);
        System.out.println(">> Éxito: Pasajero ingresó al final de la fila: " + pasajero);
    }

    /**
     * Se atiende al que lleva más tiempo esperando (frente de la fila).
     * Si la fila está vacía, avisa sin fallar.
     */
    public Pasajero atenderPasajeroFila() {
        if (filaCheckIn.isEmpty()) {
            System.out.println(">> Aviso: La fila de check-in está vacía. No hay pasajeros por atender.");
            return null;
        }
        Pasajero atendido = filaCheckIn.pollFirst();
        System.out.println(">> Atendiendo al pasajero: " + atendido);
        return atendido;
    }

    /**
     * El último en llegar puede abandonar la fila antes de ser atendido.
     * Si la fila está vacía, avisa sin fallar.
     */
    public Pasajero ultimoAbandonaFila() {
        if (filaCheckIn.isEmpty()) {
            System.out.println(">> Aviso: La fila de check-in está vacía. Nadie puede abandonar.");
            return null;
        }
        Pasajero abandono = filaCheckIn.pollLast();
        System.out.println(">> El último en llegar abandonó la fila: " + abandono);
        return abandono;
    }

    /**
     * Ver quién sigue sin atenderlo y cuántas personas hay en la fila.
     * Si la fila está vacía, avisa sin fallar.
     */
    public void verSiguienteYTotalFila() {
        if (filaCheckIn.isEmpty()) {
            System.out.println(">> Aviso: La fila de check-in está vacía (0 personas esperando).");
            return;
        }
        Pasajero siguiente = filaCheckIn.peekFirst();
        System.out.println(">> Siguiente pasajero a ser atendido: " + siguiente);
        System.out.println(">> Personas actualmente en la fila: " + filaCheckIn.size());
    }

    // =========================================================================
    // MÉTODOS RF-07 · Vuelos por código
    // =========================================================================

    /**
     * Registrar un vuelo. Si ya existe uno con ese código, no se reemplaza y se avisa.
     */
    public boolean registrarVuelo(Vuelo vuelo) {
        if (vuelo == null) {
            System.out.println(">> Error: Vuelo nulo.");
            return false;
        }
        String codigo = vuelo.getCodigo();
        if (vuelosRegistrados.containsKey(codigo)) {
            System.out.println(">> Aviso: Ya existe un vuelo con el código '" + codigo + "'. No se reemplaza y el original no cambia.");
            return false;
        }
        vuelosRegistrados.put(codigo, vuelo);
        System.out.println(">> Éxito: Vuelo registrado: " + vuelo);
        return true;
    }

    /**
     * Buscar un vuelo por su código y eliminarlo. Si no existe, avisarlo.
     */
    public Vuelo buscarYEliminarVuelo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            System.out.println(">> Error: Debe proporcionar un código de vuelo.");
            return null;
        }
        String cod = codigo.trim().toUpperCase();
        if (!vuelosRegistrados.containsKey(cod)) {
            System.out.println(">> Aviso: El vuelo con código '" + cod + "' no existe en el sistema.");
            return null;
        }
        Vuelo eliminado = vuelosRegistrados.remove(cod);
        // También lo removemos del cache rápido si estuviera presente
        cacheConsultaRapida.remove(cod);
        System.out.println(">> Éxito: Vuelo eliminado: " + eliminado);
        return eliminado;
    }

    /**
     * Listar todos los vuelos registrados.
     */
    public void listarVuelos() {
        if (vuelosRegistrados.isEmpty()) {
            System.out.println(">> Aviso: No hay vuelos registrados en el sistema.");
            return;
        }
        System.out.println(">> Vuelos registrados en el sistema (" + vuelosRegistrados.size() + " en total):");
        for (Vuelo v : vuelosRegistrados.values()) {
            System.out.println("   - " + v);
        }
    }

    public Vuelo obtenerVueloRegistrado(String codigo) {
        if (codigo == null) return null;
        return vuelosRegistrados.get(codigo.trim().toUpperCase());
    }

    // =========================================================================
    // MÉTODOS RF-08 · Consulta rápida de vuelos (Cache LRU)
    // =========================================================================

    /**
     * Consulta un vuelo de forma rápida:
     * - Si consultan un vuelo que ya recuerda, lo entrega sin buscarlo otra vez y pasa a ser el más reciente.
     * - Si consultan uno que no recuerda, lo busca en los vuelos registrados (RF-07) y lo recuerda.
     * - Cuando entra un cuarto vuelo, olvida el que lleva más tiempo sin consultarse (máximo 3).
     * - Si el vuelo no existe, avisarlo y no recordar nada.
     */
    public Vuelo consultarVueloRapido(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            System.out.println(">> Error: Debe proporcionar un código de vuelo.");
            return null;
        }
        String cod = codigo.trim().toUpperCase();

        // 1. ¿Está ya en el cache?
        if (cacheConsultaRapida.containsKey(cod)) {
            // Al hacer get en un LinkedHashMap con accessOrder=true, pasa automáticamente a ser el más reciente
            Vuelo enCache = cacheConsultaRapida.get(cod);
            System.out.println(">> [Cache HIT] Vuelo entregado directamente desde consulta rápida: " + enCache);
            return enCache;
        }

        // 2. No está en cache, buscarlo en vuelos registrados (RF-07)
        Vuelo enRegistrados = vuelosRegistrados.get(cod);
        if (enRegistrados == null) {
            System.out.println(">> Aviso: El vuelo '" + cod + "' no existe en el sistema. No se recuerda nada.");
            return null;
        }

        // 3. Existe en RF-07: se agrega a la memoria rápida
        cacheConsultaRapida.put(cod, enRegistrados);
        System.out.println(">> [Cache MISS] Vuelo localizado en RF-07 y guardado en consulta rápida: " + enRegistrados);
        return enRegistrados;
    }

    /**
     * Muestra los códigos que recuerda, del menos al más recientemente consultado.
     */
    public void mostrarCodigosEnMemoria() {
        if (cacheConsultaRapida.isEmpty()) {
            System.out.println(">> Aviso: El panel de consulta rápida está vacío.");
            return;
        }
        // En LinkedHashMap con accessOrder=true, el keySet() itera del menos reciente al más reciente
        System.out.println(">> Códigos en memoria rápida (del menos al más recientemente consultado):");
        List<String> codigos = new ArrayList<>(cacheConsultaRapida.keySet());
        System.out.println("   " + String.join(", ", codigos));
    }
}
