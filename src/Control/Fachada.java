/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;

import Modelo.Estado;
import Modelo.Transicion;
import Vista.VentanaPrincipal;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JTextArea;

/**
 *
 * @author carlosmamut1
 */
public class Fachada implements ActionListener {

    private ControlPrincipal cPrincipal;
    private VentanaPrincipal vPrincipal;

    public Fachada(ControlPrincipal cPrincipal) {
        this.cPrincipal = cPrincipal;
        this.vPrincipal = new VentanaPrincipal();
        this.vPrincipal.setVisible(true);
        // States events
        this.vPrincipal.getBtnAgregarEstado().addActionListener(this);

        this.vPrincipal.getBtnEliminarEstado().addActionListener(this);

        // Transitions events
        this.vPrincipal.getBtnAgregarTransicion().addActionListener(this);

        this.vPrincipal.getBtnEliminarTransicion().addActionListener(this);

        // Alphabet events
        this.vPrincipal.getBtnAgregarSimbolo().addActionListener(this);

        this.vPrincipal.getBtnEliminarSimbolo().addActionListener(this);

        this.vPrincipal.getBtnProbarCadena().addActionListener(this);

    }

    public VentanaPrincipal getvPrincipal() {
        return vPrincipal;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if ("Agregar Estado".equals(ae.getActionCommand())) {
            String nombre = vPrincipal.getTxtNombreEstado().getText().trim();
            boolean esInicial = vPrincipal.getChkEstadoInicial().isSelected();
            boolean esFinal = vPrincipal.getChkEstadoFinal().isSelected();

            if (nombre.isEmpty()) {
                vPrincipal.mostrarMensajeError("El nombre del estado no puede estar vacío.");
                return;
            }

            cPrincipal.getcEstado().crearEstado(nombre, esInicial, esFinal);

            // Actualizar tabla de estados
            vPrincipal.getModeloTablaEstados().setRowCount(0); // Limpiar tabla
            for (Estado e : cPrincipal.getcEstado().getEstados()) {
                vPrincipal.getModeloTablaEstados().addRow(new Object[]{
                    e.getNombre(),
                    e.isEstadoInicial() ? "Sí" : "No",
                    e.isEstadoFinal() ? "Sí" : "No"
                });
            }

            // Actualizar ComboBoxes de transiciones
            JComboBox<String> desde = vPrincipal.getCmbEstadoDesde();
            JComboBox<String> hasta = vPrincipal.getCmbEstadoHasta();
            desde.removeAllItems();
            hasta.removeAllItems();
            for (String nombreEstado : cPrincipal.getcEstado().getNombresEstados()) {
                desde.addItem(nombreEstado);
                hasta.addItem(nombreEstado);
            }

            // Limpiar campos
            vPrincipal.getTxtNombreEstado().setText("");
            vPrincipal.getChkEstadoInicial().setSelected(false);
            vPrincipal.getChkEstadoFinal().setSelected(false);
        }

        if ("Eliminar Estado".equals(ae.getActionCommand())) {
            int filaSeleccionada = vPrincipal.getTablaEstados().getSelectedRow();
            if (filaSeleccionada == -1) {
                vPrincipal.mostrarMensajeError("Debe seleccionar un estado en la tabla para eliminarlo.");
                return;
            }

            String nombreEstado = (String) vPrincipal.getModeloTablaEstados().getValueAt(filaSeleccionada, 0);

            cPrincipal.getcEstado().eliminarEstado(nombreEstado);

            // Actualizar tabla de estados
            vPrincipal.getModeloTablaEstados().setRowCount(0);
            for (Estado e : cPrincipal.getcEstado().getEstados()) {
                vPrincipal.getModeloTablaEstados().addRow(new Object[]{
                    e.getNombre(),
                    e.isEstadoInicial() ? "Sí" : "No",
                    e.isEstadoFinal() ? "Sí" : "No"
                });
            }

            // Actualizar ComboBoxes de transiciones
            JComboBox<String> desde = vPrincipal.getCmbEstadoDesde();
            JComboBox<String> hasta = vPrincipal.getCmbEstadoHasta();
            desde.removeAllItems();
            hasta.removeAllItems();
            for (String nombre : cPrincipal.getcEstado().getNombresEstados()) {
                desde.addItem(nombre);
                hasta.addItem(nombre);
            }
        }

        if ("Agregar Transicion".equals(ae.getActionCommand())) {
            String desde = (String) vPrincipal.getCmbEstadoDesde().getSelectedItem();
            String hasta = (String) vPrincipal.getCmbEstadoHasta().getSelectedItem();
            String simbolo = (String) vPrincipal.getCmbSimbolo().getSelectedItem();

            // Validaciones
            if (desde == null || hasta == null || simbolo == null) {
                vPrincipal.mostrarMensajeError("Debe seleccionar un estado de origen, un estado de destino y un símbolo.");
                return;
            }

            Estado estadoDesde = cPrincipal.getcEstado().buscarEstadoPorNombre(desde);
            Estado estadoHasta = cPrincipal.getcEstado().buscarEstadoPorNombre(hasta);

            if (estadoDesde == null || estadoHasta == null) {
                vPrincipal.mostrarMensajeError("Estados no válidos seleccionados.");
                return;
            }

            // Crear la transición
            cPrincipal.getcTransicion().crearTransicion(estadoDesde, estadoHasta, simbolo);

            // Actualizar la tabla de transiciones
            vPrincipal.getModeloTablaTransiciones().setRowCount(0);
            for (Transicion t : cPrincipal.getcTransicion().getTransiciones()) {
                vPrincipal.getModeloTablaTransiciones().addRow(new Object[]{
                    t.getEstadoDesde().getNombre(),
                    t.getSimbolo(),
                    t.getEstadoHasta().getNombre()
                });
            }

            // Opcional: mostrar mensaje
            vPrincipal.mostrarMensaje("Transición agregada exitosamente.");
        }

        if ("Eliminar Transicion".equals(ae.getActionCommand())) {
            int filaSeleccionada = vPrincipal.getTablaTransiciones().getSelectedRow();
            if (filaSeleccionada == -1) {
                vPrincipal.mostrarMensajeError("Debe seleccionar una transición para eliminar.");
                return;
            }

            String desde = (String) vPrincipal.getModeloTablaTransiciones().getValueAt(filaSeleccionada, 0);
            String simbolo = (String) vPrincipal.getModeloTablaTransiciones().getValueAt(filaSeleccionada, 1);
            String hasta = (String) vPrincipal.getModeloTablaTransiciones().getValueAt(filaSeleccionada, 2);

            Estado estadoDesde = cPrincipal.getcEstado().buscarEstadoPorNombre(desde);
            Estado estadoHasta = cPrincipal.getcEstado().buscarEstadoPorNombre(hasta);

            if (estadoDesde == null || estadoHasta == null) {
                vPrincipal.mostrarMensajeError("Error al identificar los estados de la transición.");
                return;
            }

            cPrincipal.getcTransicion().eliminarTransicion(estadoDesde, estadoHasta, simbolo);

            // Actualizar tabla
            vPrincipal.getModeloTablaTransiciones().setRowCount(0);
            for (Transicion t : cPrincipal.getcTransicion().getTransiciones()) {
                vPrincipal.getModeloTablaTransiciones().addRow(new Object[]{
                    t.getEstadoDesde().getNombre(),
                    t.getSimbolo(),
                    t.getEstadoHasta().getNombre()
                });
            }

            vPrincipal.mostrarMensaje("Transición eliminada exitosamente.");
        }
// Agregar símbolo al alfabeto
        if ("Agregar".equals(ae.getActionCommand())) {
            String simbolo = vPrincipal.getTxtSimbolo().getText().trim();

            if (simbolo.isEmpty()) {
                vPrincipal.mostrarMensajeError("El símbolo no puede estar vacío.");
                return;
            }

            if (simbolo.length() > 1) {
                vPrincipal.mostrarMensajeError("Solo se permite un carácter como símbolo.");
                return;
            }

            if (cPrincipal.getAlfabeto().contains(simbolo)) {
                vPrincipal.mostrarMensajeError("El símbolo ya existe en el alfabeto.");
                return;
            }

            cPrincipal.agregarSimboloAlfabeto(simbolo);
            vPrincipal.getModeloListaAlfabeto().addElement(simbolo);

            // Actualizar ComboBox de símbolo
            vPrincipal.getCmbSimbolo().addItem(simbolo);

            vPrincipal.mostrarMensaje("Símbolo agregado al alfabeto.");
            vPrincipal.getTxtSimbolo().setText("");
        }

// Eliminar símbolo del alfabeto
        if ("Eliminar".equals(ae.getActionCommand())) {
            String simboloSeleccionado = vPrincipal.getListaAlfabeto().getSelectedValue();

            if (simboloSeleccionado == null) {
                vPrincipal.mostrarMensajeError("Debe seleccionar un símbolo de la lista para eliminarlo.");
                return;
            }

            cPrincipal.eliminarSimboloAlfabeto(simboloSeleccionado);
            vPrincipal.getModeloListaAlfabeto().removeElement(simboloSeleccionado);

            // Quitar del ComboBox también
            vPrincipal.getCmbSimbolo().removeItem(simboloSeleccionado);

            vPrincipal.mostrarMensaje("Símbolo eliminado del alfabeto.");
        }

        if ("Probar".equals(ae.getActionCommand())) {
            String cadena = vPrincipal.getTxtCadenaPrueba().getText().trim();

            if (cadena.isEmpty()) {
                vPrincipal.mostrarMensajeError("Ingrese una cadena para probar.");
                return;
            }

            // Validar que todos los símbolos existan en el alfabeto
            ArrayList<String> alfabeto = cPrincipal.getAlfabeto();
            for (char c : cadena.toCharArray()) {
                String s = String.valueOf(c);
                if (!alfabeto.contains(s)) {
                    vPrincipal.mostrarMensajeError("El símbolo '" + s + "' no pertenece al alfabeto.");
                    return;
                }
            }

            // Procesar la cadena
            boolean aceptada = cPrincipal.procesarCadena(cadena);

            // Mostrar el resultado en el área de texto
            JTextArea txtResultado = vPrincipal.getTxtResultado();
            txtResultado.setText(""); // Limpiar antes de mostrar

            if (aceptada) {
                txtResultado.setText("✔ La cadena fue **aceptada** por el autómata.");
            } else {
                txtResultado.setText("✘ La cadena fue **rechazada** por el autómata.");
            }
        }

    }

}
