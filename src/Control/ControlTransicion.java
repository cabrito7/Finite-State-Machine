// Updated ControlTransicion.java
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

    public void crearTransicion(String estadoDesde, String estadoHasta, String simbolo) {
        Estado desde = this.cPrincipal.getcEstado().buscarEstadoPorNombre(estadoDesde);
        Estado hasta = this.cPrincipal.getcEstado().buscarEstadoPorNombre(estadoHasta);
        
        if (desde == null || hasta == null) {
            this.cPrincipal.getFachada().getvPrincipal().mostrarMensajeError("Uno o ambos estados no existen.");
            return;
        }
        
        // Verificar si ya existe una transición igual
        if (existeTransicion(desde, hasta, simbolo)) {
            this.cPrincipal.getFachada().getvPrincipal().mostrarMensajeError("Ya existe una transición igual.");
            return;
        }
        
        Transicion nueva = new Transicion(desde, hasta, simbolo);
        transiciones.add(nueva);
        
        // Agregar símbolo al alfabeto si no existe
        this.cPrincipal.agregarSimboloAlfabeto(simbolo);
        
        this.cPrincipal.getFachada().getvPrincipal().mostrarMensaje("Transición creada.");
    }

    public void eliminarTransicion(String estadoDesde, String estadoHasta, String simbolo) {
        Estado desde = this.cPrincipal.getcEstado().buscarEstadoPorNombre(estadoDesde);
        Estado hasta = this.cPrincipal.getcEstado().buscarEstadoPorNombre(estadoHasta);
        
        if (desde == null || hasta == null) {
            this.cPrincipal.getFachada().getvPrincipal().mostrarMensajeError("Uno o ambos estados no existen.");
            return;
        }
        
        Transicion aEliminar = buscarTransicion(desde, hasta, simbolo);
        if (aEliminar == null) {
            this.cPrincipal.getFachada().getvPrincipal().mostrarMensajeError("Transición no encontrada.");
            return;
        }
        
        transiciones.remove(aEliminar);
        this.cPrincipal.getFachada().getvPrincipal().mostrarMensaje("Transición eliminada.");
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

    public Transicion buscarTransicion(Estado desde, Estado hasta, String simbolo) {
        for (Transicion t : transiciones) {
            if (t.getEstadoDesde().equals(desde) && 
                t.getEstadoHasta().equals(hasta) && 
                t.getSimbolo().equals(simbolo)) {
                return t;
            }
        }
        return null;
    }

    public boolean existeTransicion(Estado desde, Estado hasta, String simbolo) {
        return buscarTransicion(desde, hasta, simbolo) != null;
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
}
