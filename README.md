# Caso Aeropuerto - Proyecto Completo para Parcial (Java 17+)

Sistema de gestión y operación de aeropuerto implementado en **Java 17 o superior**, utilizando exactamente las **8 colecciones del Java Collections Framework** y probado mediante una arquitectura de consola interactiva con menú principal y submenús en `App.java`.

---

## 📋 Mapeo de Requisitos y Colecciones Utilizadas

| Proceso | Requisito | Colección de Java | Justificación Técnica |
| :--- | :--- | :--- | :--- |
| **RF-01** | **Pasajeros del vuelo** | `ArrayList<Pasajero>` | Acceso rápido por índice (`get`/`remove`), inserción al final y ordenamiento sin alterar el orden original. |
| **RF-02** | **Ruta de escalas** | `LinkedList<String>` | Eficiente para inserciones intermedias (insertar una escala justo después de otra) y eliminaciones en lista ligada. |
| **RF-03** | **Check-in de pasaportes** | `HashSet<String>` | Búsqueda e inserción en tiempo constante $O(1)$ para validar unicidad estricta y evitar doble check-in. |
| **RF-04** | **Destinos del día** | `LinkedHashSet<String>` | Mantiene los elementos únicos sin duplicados y preserva el **orden exacto de inserción** (primera aparición). |
| **RF-05** | **Aterrizajes por prioridad** | `PriorityQueue<SolicitudAterrizaje>` | Cola de prioridad basada en montículo binario (`Heap`). Atiende emergencias (1) antes que combustible bajo (2) y normal (3); empates resueltos por orden de llegada (FIFO). |
| **RF-06** | **Fila de check-in** | `ArrayDeque<Pasajero>` | Implementa una cola de doble extremo (`Deque`). Permite atender por el frente (`pollFirst`), ingresar al final (`addLast`) y abandono por el final (`pollLast`). |
| **RF-07** | **Vuelos por código** | `HashMap<String, Vuelo>` | Búsqueda, inserción y eliminación instantánea en tiempo promedio $O(1)$ indexado por el código de vuelo. |
| **RF-08** | **Consulta rápida de vuelos** | `LinkedHashMap<String, Vuelo>` | Caché **LRU (Least Recently Used)** con `accessOrder = true` y capacidad máxima de 3 elementos. Desplaza y expulsa automáticamente el elemento menos consultado. |

---

## 🚀 Compilación y Ejecución

### 1. Compilar todo el proyecto:
```bash
javac -encoding UTF-8 *.java
```

### 2. Ejecutar la aplicación:
```bash
java App
```

---

## 🧪 Demostración Automática (Turno de Prueba del Parcial)

El programa incluye en el menú principal la **Opción 9: EJECUTAR TURNO COMPLETO DE EJEMPLO**.
Al seleccionarla, el sistema ejecuta de forma automática toda la secuencia descrita en la guía del examen:

1. **RF-07 (Vuelos):** Registra AV10, AV20, AV30 y AV40. Intenta registrar duplicado AV10 y lo rechaza.
2. **RF-04 (Destinos):** Registra Lima, Miami, Lima (repetido, se avisa) y Madrid. Muestra: `Lima, Miami, Madrid`.
3. **RF-02 (Ruta):** Registra Cartagena, Bogotá, Madrid. Inserta Panamá después de Bogotá. Intenta insertar Quito después de Lima (se avisa que Lima no está).
4. **RF-06 (Fila):** Llegan Camila Rojas, Diego Pardo y Elena Vega. Se atiende a Camila. Elena abandona la fila. Queda Diego esperando.
5. **RF-03 (Check-in):** Camila hace check-in con pasaporte P-100. Al reintentar, se rechaza por repetido.
6. **RF-01 (Pasajeros):** Se agregan Camila Rojas, Diego Pardo y Elena Vega. Se listan ordenados por apellido (`Pardo, Rojas, Vega`). Se elimina posición 1 (Diego Pardo). Se intenta eliminar posición 9 (se avisa sin fallar).
7. **RF-05 (Aterrizajes):** Piden aterrizar AV20 (prio 3), AV10 (prio 2), AV30 (prio 3) y AV40 (prio 1). La torre autoriza en orden: `AV40 -> AV10 -> AV20 -> AV30`.
8. **RF-08 (Consulta rápida):** Se consultan AV10, AV20, AV30, nuevamente AV20 y finalmente AV40. En memoria quedan: `AV30, AV20, AV40` (del menos al más reciente). AV10 fue expulsado.

---

## 🛡️ Robustez y Control de Errores
- Las entradas numéricas en consola se leen mediante un método seguro con `try-catch` sobre `NumberFormatException`. Si el usuario ingresa letras o símbolos, el sistema emite una advertencia y solicita el dato nuevamente sin cerrarse.
- Cada método de negocio incluye validaciones para casos límite (índices fuera de rango, elementos inexistentes, listas vacías, colas vacías).
