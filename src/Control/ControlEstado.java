// Updated ControlEstado.java
package Control;

import Modelo.Estado;
import java.util.ArrayList;

public class ControlEstado {
    private ControlPrincipal cPrincipal;
    private Estado estado;
    private ArrayList<Estado> estados;

    public ControlEstado(ControlPrincipal cPrincipal) {
        this.cPrincipal = cPrincipal;
        this.estados = new ArrayList<>();
    }

    public void crearEstado(String nombre, boolean estadoInicial, boolean estadoFinal) {
        Estado nuevo = new Estado(nombre, estadoInicial, estadoFinal);
        for (Estado e : estados) {
            if (estadosSonIguales(e, nuevo)) {
                this.cPrincipal.getFachada().getvPrincipal().mostrarMensajeError("Ya existe un estado igual. No se agrega.");
                return;
            }
        }
        
        // Verificar que solo haya un estado inicial
        if (estadoInicial && tieneEstadoInicial()) {
            this.cPrincipal.getFachada().getvPrincipal().mostrarMensajeError("Ya existe un estado inicial. Solo puede haber uno.");
            return;
        }
        
        estados.add(nuevo);
        this.estado = nuevo;
        this.cPrincipal.getFachada().getvPrincipal().mostrarMensaje("Estado creado y agregado.");
    }

    public void eliminarEstado(String nombre) {
        Estado estadoAEliminar = buscarEstadoPorNombre(nombre);
        if (estadoAEliminar == null) {
            this.cPrincipal.getFachada().getvPrincipal().mostrarMensajeError("Estado no encontrado.");
            return;
        }
        
        // Verificar si el estado tiene transiciones
        if (this.cPrincipal.getcTransicion().estadoTieneTransiciones(estadoAEliminar)) {
            this.cPrincipal.getFachada().getvPrincipal().mostrarMensajeError("No se puede eliminar. El estado tiene transiciones asociadas.");
            return;
        }
        
        estados.remove(estadoAEliminar);
        this.cPrincipal.getFachada().getvPrincipal().mostrarMensaje("Estado eliminado.");
    }

    public void modificarEstado(String nombreAnterior, String nuevoNombre, boolean estadoInicial, boolean estadoFinal) {
        Estado estadoAModificar = buscarEstadoPorNombre(nombreAnterior);
        if (estadoAModificar == null) {
            this.cPrincipal.getFachada().getvPrincipal().mostrarMensajeError("Estado no encontrado.");
            return;
        }
        
        // Verificar estado inicial único
        if (estadoInicial && !estadoAModificar.isEstadoInicial() && tieneEstadoInicial()) {
            this.cPrincipal.getFachada().getvPrincipal().mostrarMensajeError("Ya existe un estado inicial. Solo puede haber uno.");
            return;
        }
        
        estadoAModificar.setNombre(nuevoNombre);
        estadoAModificar.setEstadoInicial(estadoInicial);
        estadoAModificar.setEstadoFinal(estadoFinal);
        this.cPrincipal.getFachada().getvPrincipal().mostrarMensaje("Estado modificado.");
    }

    public Estado buscarEstadoPorNombre(String nombre) {
        for (Estado e : estados) {
            if (e.getNombre().equals(nombre)) {
                return e;
            }
        }
        return null;
    }

    public ArrayList<Estado> getEstados() {
        return new ArrayList<>(estados);
    }

    public ArrayList<String> getNombresEstados() {
        ArrayList<String> nombres = new ArrayList<>();
        for (Estado e : estados) {
            nombres.add(e.getNombre());
        }
        return nombres;
    }

    public Estado getEstadoInicial() {
        for (Estado e : estados) {
            if (e.isEstadoInicial()) {
                return e;
            }
        }
        return null;
    }

    public ArrayList<Estado> getEstadosFinales() {
        ArrayList<Estado> finales = new ArrayList<>();
        for (Estado e : estados) {
            if (e.isEstadoFinal()) {
                finales.add(e);
            }
        }
        return finales;
    }

    public boolean tieneEstadoInicial() {
        return getEstadoInicial() != null;
    }

    public boolean tieneEstadosFinales() {
        return !getEstadosFinales().isEmpty();
    }

    public boolean estadosSonIguales(Estado e1, Estado e2) {
        return e1.getNombre().equals(e2.getNombre()) &&
               e1.isEstadoInicial() == e2.isEstadoInicial() &&
               e1.isEstadoFinal() == e2.isEstadoFinal();
    }

    public void limpiarEstados() {
        estados.clear();
        this.cPrincipal.getFachada().getvPrincipal().mostrarMensaje("Todos los estados han sido eliminados.");
    }

    public int getCantidadEstados() {
        return estados.size();
    }

    public boolean existeEstado(String nombre) {
        return buscarEstadoPorNombre(nombre) != null;
    }
}



