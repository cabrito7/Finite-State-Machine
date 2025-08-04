// Updated ControlPrincipal.java - Máquina de Mealy
package Control;

import Modelo.Estado;
import Modelo.Transicion;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class ControlPrincipal {
    private ControlEstado cEstado;
    private ControlTransicion cTransicion;
    private Fachada fachada;
    private ArrayList<String> alfabeto;
    private ArrayList<String> alfabetoSalida; // NUEVO: Alfabeto de salida

    public ControlPrincipal() {
        this.cEstado = new ControlEstado(this);
        this.cTransicion = new ControlTransicion(this);
        this.fachada = new Fachada(this);
        this.alfabeto = new ArrayList<>();
        this.alfabetoSalida = new ArrayList<>(); // NUEVO
    }

    public Fachada getFachada() {
        return fachada;
    }

    public ControlEstado getcEstado() {
        return cEstado;
    }

    public ControlTransicion getcTransicion() {
        return cTransicion;
    }

    public ArrayList<String> getAlfabeto() {
        return new ArrayList<>(alfabeto);
    }

    // NUEVO: Gestión del alfabeto de salida
    public ArrayList<String> getAlfabetoSalida() {
        return new ArrayList<>(alfabetoSalida);
    }

    public void agregarSimboloAlfabeto(String simbolo) {
        if (!alfabeto.contains(simbolo)) {
            alfabeto.add(simbolo);
        }
    }

    public void eliminarSimboloAlfabeto(String simbolo) {
        alfabeto.remove(simbolo);
    }

    public void limpiarAlfabeto() {
        alfabeto.clear();
    }

    // NUEVO: Gestión alfabeto de salida
    public void agregarSimboloAlfabetoSalida(String simbolo) {
        if (!alfabetoSalida.contains(simbolo)) {
            alfabetoSalida.add(simbolo);
        }
    }

    public void eliminarSimboloAlfabetoSalida(String simbolo) {
        alfabetoSalida.remove(simbolo);
    }

    public void limpiarAlfabetoSalida() {
        alfabetoSalida.clear();
    }

    // NUEVO: Procesar cadena con OUTPUT (Máquina de Mealy)
    public String procesarCadenaConOutput(String cadena) {
        Estado estadoInicial = cEstado.getEstadoInicial();
        if (estadoInicial == null) {
            this.fachada.getvPrincipal().mostrarMensajeError("No hay estado inicial definido.");
            return null;
        }

        Estado estadoActual = estadoInicial;
        StringBuilder output = new StringBuilder();
        StringBuilder trazaEjecucion = new StringBuilder();
        
        trazaEjecucion.append("=== TRAZA DE EJECUCIÓN ===\n");
        trazaEjecucion.append("Estado inicial: ").append(estadoActual.getNombre()).append("\n");
        trazaEjecucion.append("Cadena de entrada: ").append(cadena).append("\n\n");
        
        for (int i = 0; i < cadena.length(); i++) {
            String simbolo = String.valueOf(cadena.charAt(i));
            
            // Buscar transición desde el estado actual con el símbolo
            Transicion transicion = cTransicion.buscarTransicionPorEstadoYSimbolo(estadoActual, simbolo);
            
            if (transicion == null) {
                String mensaje = "ERROR: No hay transición para el símbolo '" + simbolo + "' desde el estado '" + estadoActual.getNombre() + "'";
                trazaEjecucion.append(mensaje).append("\n");
                this.fachada.getvPrincipal().mostrarMensaje(trazaEjecucion.toString());
                return null;
            }
            
            // Agregar output de la transición
            output.append(transicion.getOutput());
            
            // Traza de ejecución
            trazaEjecucion.append("Paso ").append(i + 1).append(": ");
            trazaEjecucion.append("δ(").append(estadoActual.getNombre()).append(", ").append(simbolo).append(") = ");
            trazaEjecucion.append(transicion.getEstadoHasta().getNombre());
            trazaEjecucion.append(" | Output: ").append(transicion.getOutput()).append("\n");
            
            estadoActual = transicion.getEstadoHasta();
        }
        
        String resultado = output.toString();
        trazaEjecucion.append("\n=== RESULTADO ===\n");
        trazaEjecucion.append("Estado final: ").append(estadoActual.getNombre()).append("\n");
        trazaEjecucion.append("Cadena de salida: ").append(resultado).append("\n");
        
        this.fachada.getvPrincipal().mostrarMensaje(trazaEjecucion.toString());
        
        return resultado;
    }

    // Método original para compatibilidad (solo validación)
    public boolean procesarCadena(String cadena) {
        Estado estadoInicial = cEstado.getEstadoInicial();
        if (estadoInicial == null) {
            this.fachada.getvPrincipal().mostrarMensajeError("No hay estado inicial definido.");
            return false;
        }

        Estado estadoActual = estadoInicial;
        
        for (int i = 0; i < cadena.length(); i++) {
            String simbolo = String.valueOf(cadena.charAt(i));
            
            Transicion transicion = cTransicion.buscarTransicionPorEstadoYSimbolo(estadoActual, simbolo);
            
            if (transicion == null) {
                this.fachada.getvPrincipal().mostrarMensaje("Cadena rechazada: No hay transición para el símbolo '" + simbolo + "' desde el estado '" + estadoActual.getNombre() + "'");
                return false;
            }
            
            estadoActual = transicion.getEstadoHasta();
        }
        
        boolean aceptada = estadoActual.isEstadoFinal();
        if (aceptada) {
            this.fachada.getvPrincipal().mostrarMensaje("Cadena aceptada. Estado final: " + estadoActual.getNombre());
        } else {
            this.fachada.getvPrincipal().mostrarMensaje("Cadena rechazada: El estado final '" + estadoActual.getNombre() + "' no es de aceptación.");
        }
        
        return aceptada;
    }

    // Validar si el autómata está bien formado (actualizado para Máquina de Mealy)
    public boolean validarAutomata() {
        // Verificar que hay al menos un estado
        if (cEstado.getCantidadEstados() == 0) {
            this.fachada.getvPrincipal().mostrarMensajeError("El autómata debe tener al menos un estado.");
            return false;
        }
        
        // Verificar que hay exactamente un estado inicial
        if (!cEstado.tieneEstadoInicial()) {
            this.fachada.getvPrincipal().mostrarMensajeError("El autómata debe tener exactamente un estado inicial.");
            return false;
        }
        
        // Para máquina de Mealy, verificar que todas las transiciones tengan definición completa
        for (Estado estado : cEstado.getEstados()) {
            for (String simbolo : alfabeto) {
                Transicion t = cTransicion.buscarTransicionPorEstadoYSimbolo(estado, simbolo);
                if (t == null) {
                    this.fachada.getvPrincipal().mostrarMensajeError("Función de transición incompleta: Falta definir δ(" + estado.getNombre() + ", " + simbolo + ")");
                    return false;
                }
            }
        }
        
        this.fachada.getvPrincipal().mostrarMensaje("La máquina de Mealy está correctamente formada.");
        return true;
    }

    // Obtener información del autómata (actualizado)
    public String getInformacionAutomata() {
        StringBuilder info = new StringBuilder();
        info.append("=== INFORMACIÓN DE LA MÁQUINA DE MEALY ===\n");
        info.append("Estados: ").append(cEstado.getCantidadEstados()).append("\n");
        info.append("Transiciones: ").append(cTransicion.getCantidadTransiciones()).append("\n");
        info.append("Alfabeto de entrada: ").append(alfabeto.toString()).append("\n");
        info.append("Alfabeto de salida: ").append(alfabetoSalida.toString()).append("\n");
        
        Estado inicial = cEstado.getEstadoInicial();
        if (inicial != null) {
            info.append("Estado inicial: ").append(inicial.getNombre()).append("\n");
        }
        
        ArrayList<Estado> finales = cEstado.getEstadosFinales();
        if (!finales.isEmpty()) {
            info.append("Estados finales: ");
            for (int i = 0; i < finales.size(); i++) {
                info.append(finales.get(i).getNombre());
                if (i < finales.size() - 1) info.append(", ");
            }
            info.append("\n");
        }
        
        return info.toString();
    }

    // Limpiar todo el autómata (actualizado)
    public void limpiarAutomata() {
        cTransicion.limpiarTransiciones();
        cEstado.limpiarEstados();
        limpiarAlfabeto();
        limpiarAlfabetoSalida(); // NUEVO
        this.fachada.getvPrincipal().mostrarMensaje("Máquina de Mealy limpiada completamente.");
    }

    // Verificar si hay estados inalcanzables
    public ArrayList<Estado> getEstadosInalcanzables() {
        ArrayList<Estado> inalcanzables = new ArrayList<>();
        Estado inicial = cEstado.getEstadoInicial();
        
        if (inicial == null) {
            return inalcanzables;
        }
        
        Set<Estado> alcanzables = new HashSet<>();
        Stack<Estado> pila = new Stack<>();
        
        pila.push(inicial);
        alcanzables.add(inicial);
        
        while (!pila.isEmpty()) {
            Estado actual = pila.pop();
            for (Transicion t : cTransicion.getTransicionesDesdeEstado(actual)) {
                Estado destino = t.getEstadoHasta();
                if (!alcanzables.contains(destino)) {
                    alcanzables.add(destino);
                    pila.push(destino);
                }
            }
        }
        
        for (Estado e : cEstado.getEstados()) {
            if (!alcanzables.contains(e)) {
                inalcanzables.add(e);
            }
        }
        
        return inalcanzables;
    }

    // NUEVO: Generar tabla de transiciones con outputs (Máquina de Mealy)
    public String generarTablaTransicionesMealy() {
        StringBuilder tabla = new StringBuilder();
        tabla.append("=== TABLA DE TRANSICIONES - MÁQUINA DE MEALY ===\n");
        
        ArrayList<Estado> estados = cEstado.getEstados();
        ArrayList<String> simbolos = new ArrayList<>(alfabeto);
        
        if (estados.isEmpty() || simbolos.isEmpty()) {
            tabla.append("No hay estados o alfabeto definido.\n");
            return tabla.toString();
        }
        
        // Encabezado
        tabla.append(String.format("%-15s", "Estado"));
        for (String simbolo : simbolos) {
            tabla.append(String.format("%-15s", simbolo));
        }
        tabla.append("\n");
        
        // Línea separadora
        tabla.append("-".repeat(15 + simbolos.size() * 15)).append("\n");
        
        // Filas
        for (Estado estado : estados) {
            String nombreEstado = estado.getNombre();
            if (estado.isEstadoInicial()) nombreEstado += " (I)";
            if (estado.isEstadoFinal()) nombreEstado += " (F)";
            
            tabla.append(String.format("%-15s", nombreEstado));
            
            for (String simbolo : simbolos) {
                String celda = "-";
                Transicion t = cTransicion.buscarTransicionPorEstadoYSimbolo(estado, simbolo);
                if (t != null) {
                    celda = t.getEstadoHasta().getNombre() + "/" + t.getOutput();
                }
                tabla.append(String.format("%-15s", celda));
            }
            tabla.append("\n");
        }
        
        return tabla.toString();
    }

    // Método original para compatibilidad
    public String generarTablaTransiciones() {
        return generarTablaTransicionesMealy();
    }
}