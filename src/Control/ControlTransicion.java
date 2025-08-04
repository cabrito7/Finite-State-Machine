// Updated ControlTransicion.java - Máquina de Mealy
package Control;

import Modelo.Transicion;
import Modelo.Estado;
import java.util.ArrayList;

public class ControlTransicion {
    private ControlPrincipal cPrincipal;
    private ArrayList<Transicion> transiciones;

    public ControlTransicion(ControlPrincipal cPrincipal) {
        this.cPrincipal = cPrincipal;
        this.transiciones = new ArrayList<>();
    }

    // NUEVO: Crear transición con OUTPUT
    public void crearTransicion(Estado estadoDesde, Estado estadoHasta, String simbolo, String output) {
        Estado desde = this.cPrincipal.getcEstado().buscarEstadoPorNombre(estadoDesde.getNombre());
        Estado hasta = this.cPrincipal.getcEstado().buscarEstadoPorNombre(estadoHasta.getNombre());
        
        if (desde == null || hasta == null) {
            this.cPrincipal.getFachada().getvPrincipal().mostrarMensajeError("Uno o ambos estados no existen.");
            return;
        }
        
        // Verificar si ya existe una transición con el mismo estado desde y símbolo
        if (existeTransicionDesdeEstadoConSimbolo(desde, simbolo)) {
            this.cPrincipal.getFachada().getvPrincipal().mostrarMensajeError("Ya existe una transición desde el estado '" + desde.getNombre() + "' con el símbolo '" + simbolo + "'. Las máquinas de Mealy son deterministas.");
            return;
        }
        
        Transicion nueva = new Transicion(desde, hasta, simbolo, output);
        transiciones.add(nueva);
        
        // Agregar símbolo al alfabeto si no existe
        this.cPrincipal.agregarSimboloAlfabeto(simbolo);
        
        this.cPrincipal.getFachada().getvPrincipal().mostrarMensaje("Transición creada: " + nueva.toString());
    }

    // Método de compatibilidad (sin output explícito)
    public void crearTransicion(Estado estadoDesde, Estado estadoHasta, String simbolo) {
        crearTransicion(estadoDesde, estadoHasta, simbolo, "λ");
    }

    public void eliminarTransicion(Estado estadoDesde, Estado estadoHasta, String simbolo, String output) {
        Estado desde = this.cPrincipal.getcEstado().buscarEstadoPorNombre(estadoDesde.getNombre());
        Estado hasta = this.cPrincipal.getcEstado().buscarEstadoPorNombre(estadoHasta.getNombre());
        
        if (desde == null || hasta == null) {
            this.cPrincipal.getFachada().getvPrincipal().mostrarMensajeError("Uno o ambos estados no existen.");
            return;
        }
        
        Transicion aEliminar = buscarTransicion(desde, hasta, simbolo, output);
        if (aEliminar == null) {
            this.cPrincipal.getFachada().getvPrincipal().mostrarMensajeError("Transición no encontrada.");
            return;
        }
        
        transiciones.remove(aEliminar);
        this.cPrincipal.getFachada().getvPrincipal().mostrarMensaje("Transición eliminada.");
    }

    // Método de compatibilidad
    public void eliminarTransicion(Estado estadoDesde, Estado estadoHasta, String simbolo) {
        // Buscar cualquier transición con estos parámetros (sin importar output)
        for (Transicion t : new ArrayList<>(transiciones)) {
            if (t.getEstadoDesde().equals(estadoDesde) && 
                t.getEstadoHasta().equals(estadoHasta) && 
                t.getSimbolo().equals(simbolo)) {
                transiciones.remove(t);
                this.cPrincipal.getFachada().getvPrincipal().mostrarMensaje("Transición eliminada.");
                return;
            }
        }
        this.cPrincipal.getFachada().getvPrincipal().mostrarMensajeError("Transición no encontrada.");
    }

    public ArrayList<Transicion> getTransiciones() {
        return new ArrayList<>(transiciones);
    }

    public ArrayList<Transicion> getTransicionesDesdeEstado(Estado estado) {
        ArrayList<Transicion> resultado = new ArrayList<>();
        for (Transicion t : transiciones) {
            if (t.getEstadoDesde().equals(estado)) {
                resultado.add(t);
            }
        }
        return resultado;
    }

    public ArrayList<Transicion> getTransicionesHaciaEstado(Estado estado) {
        ArrayList<Transicion> resultado = new ArrayList<>();
        for (Transicion t : transiciones) {
            if (t.getEstadoHasta().equals(estado)) {
                resultado.add(t);
            }
        }
        return resultado;
    }

    public Transicion buscarTransicion(Estado desde, Estado hasta, String simbolo, String output) {
        for (Transicion t : transiciones) {
            if (t.getEstadoDesde().equals(desde) && 
                t.getEstadoHasta().equals(hasta) && 
                t.getSimbolo().equals(simbolo) &&
                t.getOutput().equals(output)) {
                return t;
            }
        }
        return null;
    }

    // NUEVO: Buscar transición por estado desde y símbolo (para máquina determinista)
    public Transicion buscarTransicionPorEstadoYSimbolo(Estado desde, String simbolo) {
        for (Transicion t : transiciones) {
            if (t.getEstadoDesde().equals(desde) && t.getSimbolo().equals(simbolo)) {
                return t;
            }
        }
        return null;
    }

    public boolean existeTransicion(Estado desde, Estado hasta, String simbolo, String output) {
        return buscarTransicion(desde, hasta, simbolo, output) != null;
    }

    // NUEVO: Verificar si existe transición desde un estado con un símbolo específico
    public boolean existeTransicionDesdeEstadoConSimbolo(Estado desde, String simbolo) {
        return buscarTransicionPorEstadoYSimbolo(desde, simbolo) != null;
    }

    public boolean estadoTieneTransiciones(Estado estado) {
        for (Transicion t : transiciones) {
            if (t.getEstadoDesde().equals(estado) || t.getEstadoHasta().equals(estado)) {
                return true;
            }
        }
        return false;
    }

    public void limpiarTransiciones() {
        transiciones.clear();
        this.cPrincipal.getFachada().getvPrincipal().mostrarMensaje("Todas las transiciones han sido eliminadas.");
    }

    public int getCantidadTransiciones() {
        return transiciones.size();
    }

    public ArrayList<String> getSimbolosUsados() {
        ArrayList<String> simbolos = new ArrayList<>();
        for (Transicion t : transiciones) {
            if (!simbolos.contains(t.getSimbolo())) {
                simbolos.add(t.getSimbolo());
            }
        }
        return simbolos;
    }

    // NUEVO: Obtener todos los outputs únicos usados
    public ArrayList<String> getOutputsUsados() {
        ArrayList<String> outputs = new ArrayList<>();
        for (Transicion t : transiciones) {
            if (!outputs.contains(t.getOutput())) {
                outputs.add(t.getOutput());
            }
        }
        return outputs;
    }
}