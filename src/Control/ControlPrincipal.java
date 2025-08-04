// Updated ControlPrincipal.java
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

    public ControlPrincipal() {
        this.cEstado = new ControlEstado(this);
        this.cTransicion = new ControlTransicion(this);
        this.fachada = new Fachada(this);
        this.alfabeto = new ArrayList<>();
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

    // Método para procesar una cadena en el autómata
    public boolean procesarCadena(String cadena) {
        Estado estadoInicial = cEstado.getEstadoInicial();
        if (estadoInicial == null) {
            this.fachada.getvPrincipal().mostrarMensajeError("No hay estado inicial definido.");
            return false;
        }

        Estado estadoActual = estadoInicial;
        
        for (int i = 0; i < cadena.length(); i++) {
            String simbolo = String.valueOf(cadena.charAt(i));
            
            // Buscar transición desde el estado actual con el símbolo
            Transicion transicion = null;
            for (Transicion t : cTransicion.getTransicionesDesdeEstado(estadoActual)) {
                if (t.getSimbolo().equals(simbolo)) {
                    transicion = t;
                    break;
                }
            }
            
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

    // Validar si el autómata está bien formado
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
        
        // Verificar que hay al menos un estado final
        if (!cEstado.tieneEstadosFinales()) {
            this.fachada.getvPrincipal().mostrarMensajeError("El autómata debe tener al menos un estado final.");
            return false;
        }
        
        this.fachada.getvPrincipal().mostrarMensaje("El autómata está correctamente formado.");
        return true;
    }

    // Obtener información del autómata
    public String getInformacionAutomata() {
        StringBuilder info = new StringBuilder();
        info.append("=== INFORMACIÓN DEL AUTÓMATA ===\n");
        info.append("Estados: ").append(cEstado.getCantidadEstados()).append("\n");
        info.append("Transiciones: ").append(cTransicion.getCantidadTransiciones()).append("\n");
        info.append("Alfabeto: ").append(alfabeto.toString()).append("\n");
        
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

    // Limpiar todo el autómata
    public void limpiarAutomata() {
        cTransicion.limpiarTransiciones();
        cEstado.limpiarEstados();
        limpiarAlfabeto();
        this.fachada.getvPrincipal().mostrarMensaje("Autómata limpiado completamente.");
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

    // Generar tabla de transiciones
    public String generarTablaTransiciones() {
        StringBuilder tabla = new StringBuilder();
        tabla.append("=== TABLA DE TRANSICIONES ===\n");
        
        ArrayList<Estado> estados = cEstado.getEstados();
        ArrayList<String> simbolos = new ArrayList<>(alfabeto);
        
        if (estados.isEmpty() || simbolos.isEmpty()) {
            tabla.append("No hay estados o alfabeto definido.\n");
            return tabla.toString();
        }
        
        // Encabezado
        tabla.append(String.format("%-15s", "Estado"));
        for (String simbolo : simbolos) {
            tabla.append(String.format("%-10s", simbolo));
        }
        tabla.append("\n");
        
        // Línea separadora
        tabla.append("-".repeat(15 + simbolos.size() * 10)).append("\n");
        
        // Filas
        for (Estado estado : estados) {
            String nombreEstado = estado.getNombre();
            if (estado.isEstadoInicial()) nombreEstado += " (I)";
            if (estado.isEstadoFinal()) nombreEstado += " (F)";
            
            tabla.append(String.format("%-15s", nombreEstado));
            
            for (String simbolo : simbolos) {
                String destino = "-";
                for (Transicion t : cTransicion.getTransicionesDesdeEstado(estado)) {
                    if (t.getSimbolo().equals(simbolo)) {
                        destino = t.getEstadoHasta().getNombre();
                        break;
                    }
                }
                tabla.append(String.format("%-10s", destino));
            }
            tabla.append("\n");
        }
        
        return tabla.toString();
    }
}
