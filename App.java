import java.util.Scanner;

/**
 * Clase principal de entrada del programa.
 * Implementa el menú principal y los submenús por consola para cada proceso (RF-01 a RF-08),
 * con validación total de entradas numéricas para prevenir cierres inesperados.
 */
public class App {

    private static final Scanner scanner = new Scanner(System.in);
    private static final SistemaAeropuerto sistema = new SistemaAeropuerto();

    public static void main(String[] args) {
        boolean salir = false;

        while (!salir) {
            System.out.println("\n=======================================================");
            System.out.println("      SISTEMA DE OPERACIÓN DE AEROPUERTO (JAVA 17+)     ");
            System.out.println("=======================================================");
            System.out.println("1. RF-01 · Pasajeros del vuelo (ArrayList)");
            System.out.println("2. RF-02 · Ruta de escalas (LinkedList)");
            System.out.println("3. RF-03 · Check-in de pasaportes (HashSet)");
            System.out.println("4. RF-04 · Destinos del día (LinkedHashSet)");
            System.out.println("5. RF-05 · Aterrizajes por prioridad (PriorityQueue)");
            System.out.println("6. RF-06 · Fila de check-in (ArrayDeque)");
            System.out.println("7. RF-07 · Vuelos por código (HashMap)");
            System.out.println("8. RF-08 · Consulta rápida de vuelos (LinkedHashMap LRU)");
            System.out.println("9. EJECUTAR TURNO COMPLETO DE EJEMPLO (Simulación de guía)");
            System.out.println("0. Salir");
            System.out.println("-------------------------------------------------------");

            int opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1 -> menuRF01();
                case 2 -> menuRF02();
                case 3 -> menuRF03();
                case 4 -> menuRF04();
                case 5 -> menuRF05();
                case 6 -> menuRF06();
                case 7 -> menuRF07();
                case 8 -> menuRF08();
                case 9 -> ejecutarTurnoEjemplo();
                case 0 -> {
                    salir = true;
                    System.out.println("\n>> ¡Gracias por usar el sistema del aeropuerto! Finalizando ejecución.");
                }
                default -> System.out.println(">> Opción no reconocida. Ingrese una opción del 0 al 9.");
            }
        }
    }

    // =========================================================================
    // SUBMENÚ RF-01: Pasajeros del vuelo
    // =========================================================================
    private static void menuRF01() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- [RF-01] Pasajeros del vuelo ---");
            System.out.println("1. Agregar un pasajero al final de la lista");
            System.out.println("2. Mostrar pasajeros ordenados por apellido (sin alterar orden de llegada)");
            System.out.println("3. Mostrar pasajeros en orden original de llegada");
            System.out.println("4. Eliminar pasajero por posición (índice 0-based)");
            System.out.println("5. Mostrar total de pasajeros en el vuelo");
            System.out.println("0. Volver al menú principal");

            int op = leerEntero("Seleccione una opción: ");
            switch (op) {
                case 1 -> {
                    String pasaporte = leerTexto("Ingrese pasaporte: ");
                    String nombre = leerTexto("Ingrese nombre: ");
                    String apellido = leerTexto("Ingrese apellido: ");
                    sistema.agregarPasajeroVuelo(new Pasajero(pasaporte, nombre, apellido));
                }
                case 2 -> sistema.mostrarPasajerosOrdenadosPorApellido();
                case 3 -> sistema.mostrarPasajerosOrdenLlegada();
                case 4 -> {
                    int pos = leerEntero("Ingrese la posición a eliminar (comenzando en 0): ");
                    sistema.eliminarPasajeroPorPosicion(pos);
                }
                case 5 -> sistema.contarPasajerosVuelo();
                case 0 -> volver = true;
                default -> System.out.println(">> Opción no válida. Intente de nuevo.");
            }
        }
    }

    // =========================================================================
    // SUBMENÚ RF-02: Ruta de escalas
    // =========================================================================
    private static void menuRF02() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- [RF-02] Ruta de escalas ---");
            System.out.println("1. Agregar ciudad al final de la ruta");
            System.out.println("2. Insertar ciudad justo después de otra existente");
            System.out.println("3. Eliminar una escala");
            System.out.println("4. Mostrar la ruta en orden (de salida a destino)");
            System.out.println("0. Volver al menú principal");

            int op = leerEntero("Seleccione una opción: ");
            switch (op) {
                case 1 -> {
                    String ciudad = leerTexto("Nombre de la ciudad a agregar: ");
                    sistema.agregarCiudadFinalRuta(ciudad);
                }
                case 2 -> {
                    String existente = leerTexto("Ciudad después de la cual insertar: ");
                    String nueva = leerTexto("Nombre de la nueva ciudad: ");
                    sistema.insertarCiudadDespuesDe(existente, nueva);
                }
                case 3 -> {
                    String escala = leerTexto("Nombre de la escala a eliminar: ");
                    sistema.eliminarEscala(escala);
                }
                case 4 -> sistema.mostrarRuta();
                case 0 -> volver = true;
                default -> System.out.println(">> Opción no válida. Intente de nuevo.");
            }
        }
    }

    // =========================================================================
    // SUBMENÚ RF-03: Check-in de pasaportes
    // =========================================================================
    private static void menuRF03() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- [RF-03] Check-in de pasaportes ---");
            System.out.println("1. Registrar check-in de un pasaporte");
            System.out.println("2. Consultar si un pasaporte ya hizo check-in y total registrados");
            System.out.println("3. Mostrar total de pasaportes con check-in");
            System.out.println("0. Volver al menú principal");

            int op = leerEntero("Seleccione una opción: ");
            switch (op) {
                case 1 -> {
                    String pasaporte = leerTexto("Número de pasaporte: ");
                    sistema.registrarCheckIn(pasaporte);
                }
                case 2 -> {
                    String pasaporte = leerTexto("Número de pasaporte a consultar: ");
                    sistema.consultarCheckIn(pasaporte);
                }
                case 3 -> sistema.contarCheckIn();
                case 0 -> volver = true;
                default -> System.out.println(">> Opción no válida. Intente de nuevo.");
            }
        }
    }

    // =========================================================================
    // SUBMENÚ RF-04: Destinos del día
    // =========================================================================
    private static void menuRF04() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- [RF-04] Destinos del día ---");
            System.out.println("1. Registrar destino de un vuelo");
            System.out.println("2. Mostrar destinos en orden de primera aparición");
            System.out.println("3. Mostrar total de destinos distintos");
            System.out.println("0. Volver al menú principal");

            int op = leerEntero("Seleccione una opción: ");
            switch (op) {
                case 1 -> {
                    String destino = leerTexto("Nombre del destino: ");
                    sistema.registrarDestino(destino);
                }
                case 2 -> sistema.mostrarDestinos();
                case 3 -> sistema.contarDestinosDistintos();
                case 0 -> volver = true;
                default -> System.out.println(">> Opción no válida. Intente de nuevo.");
            }
        }
    }

    // =========================================================================
    // SUBMENÚ RF-05: Aterrizajes por prioridad
    // =========================================================================
    private static void menuRF05() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- [RF-05] Aterrizajes por prioridad ---");
            System.out.println("1. Recibir solicitud de aterrizaje");
            System.out.println("2. Autorizar aterrizaje (Torre de control)");
            System.out.println("3. Ver quién sigue sin autorizarlo y solicitudes en espera");
            System.out.println("0. Volver al menú principal");

            int op = leerEntero("Seleccione una opción: ");
            switch (op) {
                case 1 -> {
                    String cod = leerTexto("Código del vuelo (ej. AV10): ");
                    System.out.println("Prioridades: 1 = Emergencia | 2 = Combustible bajo | 3 = Normal");
                    int prio = leerEntero("Ingrese prioridad (1, 2 o 3): ");
                    sistema.recibirSolicitudAterrizaje(cod, prio);
                }
                case 2 -> sistema.autorizarAterrizaje();
                case 3 -> sistema.verSiguienteAterrizaje();
                case 0 -> volver = true;
                default -> System.out.println(">> Opción no válida. Intente de nuevo.");
            }
        }
    }

    // =========================================================================
    // SUBMENÚ RF-06: Fila de check-in
    // =========================================================================
    private static void menuRF06() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- [RF-06] Fila de check-in ---");
            System.out.println("1. Pasajero entra al final de la fila");
            System.out.println("2. Atender al pasajero que lleva más tiempo esperando");
            System.out.println("3. El último en llegar abandona la fila");
            System.out.println("4. Ver quién sigue y cuántas personas hay en la fila");
            System.out.println("0. Volver al menú principal");

            int op = leerEntero("Seleccione una opción: ");
            switch (op) {
                case 1 -> {
                    String pasaporte = leerTexto("Pasaporte: ");
                    String nombre = leerTexto("Nombre: ");
                    String apellido = leerTexto("Apellido: ");
                    sistema.entrarFilaCheckIn(new Pasajero(pasaporte, nombre, apellido));
                }
                case 2 -> sistema.atenderPasajeroFila();
                case 3 -> sistema.ultimoAbandonaFila();
                case 4 -> sistema.verSiguienteYTotalFila();
                case 0 -> volver = true;
                default -> System.out.println(">> Opción no válida. Intente de nuevo.");
            }
        }
    }

    // =========================================================================
    // SUBMENÚ RF-07: Vuelos por código
    // =========================================================================
    private static void menuRF07() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- [RF-07] Vuelos por código ---");
            System.out.println("1. Registrar un vuelo");
            System.out.println("2. Buscar vuelo por código y eliminarlo");
            System.out.println("3. Listar todos los vuelos registrados");
            System.out.println("0. Volver al menú principal");

            int op = leerEntero("Seleccione una opción: ");
            switch (op) {
                case 1 -> {
                    String cod = leerTexto("Código de vuelo (ej. AV10): ");
                    String destino = leerTexto("Destino (ej. Lima): ");
                    String puerta = leerTexto("Puerta de embarque (ej. B1): ");
                    sistema.registrarVuelo(new Vuelo(cod, destino, puerta));
                }
                case 2 -> {
                    String cod = leerTexto("Código del vuelo a eliminar: ");
                    sistema.buscarYEliminarVuelo(cod);
                }
                case 3 -> sistema.listarVuelos();
                case 0 -> volver = true;
                default -> System.out.println(">> Opción no válida. Intente de nuevo.");
            }
        }
    }

    // =========================================================================
    // SUBMENÚ RF-08: Consulta rápida de vuelos
    // =========================================================================
    private static void menuRF08() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- [RF-08] Consulta rápida de vuelos (Cache LRU máx 3) ---");
            System.out.println("1. Consultar vuelo por código");
            System.out.println("2. Mostrar códigos en memoria rápida (del menos al más recientemente consultado)");
            System.out.println("0. Volver al menú principal");

            int op = leerEntero("Seleccione una opción: ");
            switch (op) {
                case 1 -> {
                    String cod = leerTexto("Código del vuelo a consultar: ");
                    sistema.consultarVueloRapido(cod);
                }
                case 2 -> sistema.mostrarCodigosEnMemoria();
                case 0 -> volver = true;
                default -> System.out.println(">> Opción no válida. Intente de nuevo.");
            }
        }
    }

    // =========================================================================
    // SIMULACIÓN COMPLETA DEL TURNO DE PRUEBA (Oficial de la guía)
    // =========================================================================
    private static void ejecutarTurnoEjemplo() {
        System.out.println("\n=======================================================");
        System.out.println("  INICIANDO SIMULACIÓN COMPLETA DEL TURNO DE EJEMPLO   ");
        System.out.println("=======================================================");

        // Instancia fresca para el turno de prueba
        SistemaAeropuerto sim = new SistemaAeropuerto();

        // 1. RF-07 Vuelos
        System.out.println("\n--- [Paso 1] RF-07 Vuelos ---");
        System.out.println("Registrando: AV10 (Lima, B1), AV20 (Miami, C2), AV30 (Madrid, D3), AV40 (Quito, A4)...");
        sim.registrarVuelo(new Vuelo("AV10", "Lima", "B1"));
        sim.registrarVuelo(new Vuelo("AV20", "Miami", "C2"));
        sim.registrarVuelo(new Vuelo("AV30", "Madrid", "D3"));
        sim.registrarVuelo(new Vuelo("AV40", "Quito", "A4"));
        System.out.println("Intentando registrar duplicado AV10 (Bogotá, X1)...");
        sim.registrarVuelo(new Vuelo("AV10", "Bogotá", "X1"));
        System.out.println("Esperado: Los cuatro primeros se registran; el segundo AV10 se rechaza y no cambia.");

        // 2. RF-04 Destinos
        System.out.println("\n--- [Paso 2] RF-04 Destinos ---");
        System.out.println("Registrando destinos: Lima, Miami, Lima, Madrid...");
        sim.registrarDestino("Lima");
        sim.registrarDestino("Miami");
        sim.registrarDestino("Lima");
        sim.registrarDestino("Madrid");
        sim.mostrarDestinos();
        System.out.println("Esperado: Se anotan 3 destinos; el segundo Lima se avisa como repetido. Orden: Lima, Miami, Madrid.");

        // 3. RF-02 Ruta
        System.out.println("\n--- [Paso 3] RF-02 Ruta ---");
        System.out.println("Creando ruta base: Cartagena -> Bogotá -> Madrid...");
        sim.agregarCiudadFinalRuta("Cartagena");
        sim.agregarCiudadFinalRuta("Bogotá");
        sim.agregarCiudadFinalRuta("Madrid");
        System.out.println("Insertando Panamá después de Bogotá...");
        sim.insertarCiudadDespuesDe("Bogotá", "Panamá");
        System.out.println("Intentando insertar Quito después de Lima (que no está)...");
        sim.insertarCiudadDespuesDe("Lima", "Quito");
        sim.mostrarRuta();
        System.out.println("Esperado: La ruta queda Cartagena -> Bogotá -> Panamá -> Madrid. Lima no está en la ruta, se avisa.");

        // 4. RF-06 Fila
        System.out.println("\n--- [Paso 4] RF-06 Fila ---");
        System.out.println("Llegan Camila Rojas, Diego Pardo y Elena Vega...");
        sim.entrarFilaCheckIn(new Pasajero("P-100", "Camila", "Rojas"));
        sim.entrarFilaCheckIn(new Pasajero("P-101", "Diego", "Pardo"));
        sim.entrarFilaCheckIn(new Pasajero("P-102", "Elena", "Vega"));
        System.out.println("Se atiende a un pasajero...");
        sim.atenderPasajeroFila();
        System.out.println("El último en llegar abandona la fila...");
        sim.ultimoAbandonaFila();
        sim.verSiguienteYTotalFila();
        System.out.println("Esperado: Se atiende a Camila; Elena se retira; queda Diego esperando.");

        // 5. RF-03 Check-in
        System.out.println("\n--- [Paso 5] RF-03 Check-in ---");
        System.out.println("Camila hace check-in con pasaporte P-100...");
        sim.registrarCheckIn("P-100");
        System.out.println("Intenta hacer check-in otra vez con pasaporte P-100...");
        sim.registrarCheckIn("P-100");
        System.out.println("Esperado: El primero se registra; el segundo se rechaza por repetido.");

        // 6. RF-01 Pasajeros
        System.out.println("\n--- [Paso 6] RF-01 Pasajeros ---");
        System.out.println("Agregando al vuelo: Camila Rojas, Diego Pardo, Elena Vega...");
        sim.agregarPasajeroVuelo(new Pasajero("P-100", "Camila", "Rojas"));
        sim.agregarPasajeroVuelo(new Pasajero("P-101", "Diego", "Pardo"));
        sim.agregarPasajeroVuelo(new Pasajero("P-102", "Elena", "Vega"));
        sim.mostrarPasajerosOrdenadosPorApellido();
        System.out.println("Eliminando pasajero de la posición 1...");
        sim.eliminarPasajeroPorPosicion(1);
        System.out.println("Intentando eliminar pasajero de la posición 9...");
        sim.eliminarPasajeroPorPosicion(9);
        System.out.println("Esperado: Por apellido: Pardo, Rojas, Vega. Sale Diego Pardo; posición 9 no existe y se avisa.");

        // 7. RF-05 Aterrizajes
        System.out.println("\n--- [Paso 7] RF-05 Aterrizajes ---");
        System.out.println("Solicitudes de aterrizaje: AV20 (prio 3), AV10 (prio 2), AV30 (prio 3), AV40 (prio 1)...");
        sim.recibirSolicitudAterrizaje("AV20", 3);
        sim.recibirSolicitudAterrizaje("AV10", 2);
        sim.recibirSolicitudAterrizaje("AV30", 3);
        sim.recibirSolicitudAterrizaje("AV40", 1);
        System.out.println("Torre de control autoriza secuencialmente:");
        sim.autorizarAterrizaje();
        sim.autorizarAterrizaje();
        sim.autorizarAterrizaje();
        sim.autorizarAterrizaje();
        System.out.println("Esperado: Autorizados en este orden: AV40, AV10, AV20, AV30.");

        // 8. RF-08 Consulta rápida
        System.out.println("\n--- [Paso 8] RF-08 Consulta rápida ---");
        System.out.println("Consultando: AV10, AV20, AV30, otra vez AV20 y AV40...");
        sim.consultarVueloRapido("AV10");
        sim.consultarVueloRapido("AV20");
        sim.consultarVueloRapido("AV30");
        sim.consultarVueloRapido("AV20");
        sim.consultarVueloRapido("AV40");
        sim.mostrarCodigosEnMemoria();
        System.out.println("Esperado: Recuerda AV30, AV20, AV40 (del menos al más reciente). AV10 fue el que se olvidó.");

        System.out.println("\n=======================================================");
        System.out.println("     FIN DE LA SIMULACIÓN COMPLETA DEL TURNO           ");
        System.out.println("=======================================================\n");
    }

    // =========================================================================
    // UTILIDADES DE ENTRADA SEGURA POR CONSOLA
    // =========================================================================

    /**
     * Lee un entero por teclado de forma segura. Si el usuario ingresa texto no numérico,
     * no revienta con excepción; avisa amablemente y reintenta.
     */
    private static int leerEntero(String prompt) {
        while (true) {
            System.out.print(prompt);
            String linea = scanner.nextLine().trim();
            try {
                return Integer.parseInt(linea);
            } catch (NumberFormatException e) {
                System.out.println(">> Error de entrada: Se esperaba un número entero. Ingrese un valor válido.");
            }
        }
    }

    /**
     * Lee una cadena no vacía por teclado.
     */
    private static String leerTexto(String prompt) {
        while (true) {
            System.out.print(prompt);
            String linea = scanner.nextLine().trim();
            if (!linea.isEmpty()) {
                return linea;
            }
            System.out.println(">> El campo no puede estar vacío. Por favor ingrese un valor.");
        }
    }
}
