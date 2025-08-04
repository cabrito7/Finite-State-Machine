/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;

import Modelo.Estado;
import java.util.ArrayList;

/**
 *
 * @author carlosmamut1
 */
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
        estados.add(nuevo);
        this.estado = nuevo;
        this.cPrincipal.getFachada().getvPrincipal().mostrarMensaje("Estado creado y agregado.");
    }

   
    public boolean estadosSonIguales(Estado e1, Estado e2) {
        return e1.getNombre().equals(e2.getNombre()) &&
               e1.isEstadoInicial() == e2.isEstadoInicial() &&
               e1.isEstadoFinal() == e2.isEstadoFinal();
    }
}



