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
 * Fachada que maneja la comunicación entre la vista y los controladores
 * @author carlosmamut1
 */
public class Fachada implements ActionListener {

    private ControlPrincipal cPrincipal;
    private VentanaPrincipal vPrincipal;

    public Fachada(ControlPrincipal cPrincipal) {
        this.cPrincipal = cPrincipal;
        this.vPrincipal = new VentanaPrincipal();
        this.vPrincipal.setVisible(true);
        
        // Estados events
        this.vPrincipal.getBtnAgregarEstado().addActionListener(this);
        this.vPrincipal.getBtnEliminarEstado().addActionListener(this);

        // Transiciones events
        this.vPrincipal.getBtnAgregarTransicion().addActionListener(this);
        this.vPrincipal.getBtnEliminarTransicion().addActionListener(this);

        // Alfabeto events
        this.vPrincipal.getBtnAgregarSimbolo().addActionListener(this);
        this.vPrincipal.getBtnEliminarSimbolo().addActionListener(this);

        // Prueba events
        this.vPrincipal.getBtnProbarCadena().addActionListener(this);

        // Menú events
        this.vPrincipal.getItemNuevo().addActionListener(this);
        this.vPrincipal.getItemValidarAutomata().addActionListener(this);
        this.vPrincipal.getItemLimpiarTodo().addActionListener(this);
    }

    public VentanaPrincipal getvPrincipal() {
        return vPrincipal;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String comando = ae.getActionCommand();
        
        switch (comando) {
            case "Agregar Estado":
                manejarAgregarEstado();
                break;
                
            case "Eliminar Estado":
                manejarEliminarEstado();
                break;
                
            case "Agregar Transicion":
                manejarAgregarTransicion();
                break;
                
            case "Eliminar Transicion":
                manejarEliminarTransicion();
                break;
                
            case "Agregar":
                manejarAgregarSimbolo();
                break;
                
            case "Eliminar":
                manejarEliminarSimbolo();
                break;
                
            case "Probar":
                manejarProbarCadena();
                break;
                
            case "Nuevo":
                manejarNuevoAutomata();
                break;
                
            case "Validar Autómata":
                manejarValidarAutomata();
                break;
                
            case "Limpiar Todo":
                manejarLimpiarTodo();
                break;
        }
    }

    private void manejarAgregarEstado() {
        String nombre = vPrincipal.getTxtNombreEstado().getText().trim();
        boolean esInicial = vPrincipal.getChkEstadoInicial().isSelected();
        boolean esFinal = vPrincipal.getChkEstadoFinal().isSelected();

        if (nombre.isEmpty()) {
            vPrincipal.mostrarMensajeError("El nombre del estado no puede estar vacío.");
            return;
        }

        cPrincipal.getcEstado().crearEstado(nombre, esInicial, esFinal);
        actualizarTablaEstados();
        actualizarComboBoxEstados();
        limpiarCamposEstado();
    }

    private void manejarEliminarEstado() {
        int filaSeleccionada = vPrincipal.getTablaEstados().getSelectedRow();
        if (filaSeleccionada == -1) {
            vPrincipal.mostrarMensajeError("Debe seleccionar un estado en la tabla para eliminarlo.");
            return;
        }

        String nombreEstado = (String) vPrincipal.getModeloTablaEstados().getValueAt(filaSeleccionada, 0);
        cPrincipal.getcEstado().eliminarEstado(nombreEstado);
        
        actualizarTablaEstados();
        actualizarComboBoxEstados();
        actualizarTablaTransiciones();
    }

    private void manejarAgregarTransicion() {
        String desde = (String) vPrincipal.getCmbEstadoDesde().getSelectedItem();
        String hasta = (String) vPrincipal.getCmbEstadoHasta().getSelectedItem();
        String simbolo = (String) vPrincipal.getCmbSimbolo().getSelectedItem();

        if (desde == null || hasta == null || simbolo == null) {
            vPrincipal.mostrarMensajeError("Debe seleccionar un estado de origen, un estado de destino y un símbolo.");
            return;
        }

        // Para máquina de Mealy, pedir el output
        String output = javax.swing.JOptionPane.showInputDialog(
            vPrincipal, 
            "Ingrese el output para esta transición:", 
            "Output de Transición", 
            javax.swing.JOptionPane.QUESTION_MESSAGE
        );

        if (output == null) {
            return; // Usuario canceló
        }

        if (output.trim().isEmpty()) {
            output = "λ"; // Output vacío
        }

        Estado estadoDesde = cPrincipal.getcEstado().buscarEstadoPorNombre(desde);
        Estado estadoHasta = cPrincipal.getcEstado().buscarEstadoPorNombre(hasta);

        if (estadoDesde == null || estadoHasta == null) {
            vPrincipal.mostrarMensajeError("Estados no válidos seleccionados.");
            return;
        }

        cPrincipal.getcTransicion().crearTransicion(estadoDesde, estadoHasta, simbolo, output.trim());
        actualizarTablaTransiciones();
    }

    private void manejarEliminarTransicion() {
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
        actualizarTablaTransiciones();
    }

    private void manejarAgregarSimbolo() {
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
        vPrincipal.getCmbSimbolo().addItem(simbolo);

        vPrincipal.mostrarMensaje("Símbolo agregado al alfabeto.");
        vPrincipal.getTxtSimbolo().setText("");
    }

    private void manejarEliminarSimbolo() {
        String simboloSeleccionado = vPrincipal.getListaAlfabeto().getSelectedValue();

        if (simboloSeleccionado == null) {
            vPrincipal.mostrarMensajeError("Debe seleccionar un símbolo de la lista para eliminarlo.");
            return;
        }

        cPrincipal.eliminarSimboloAlfabeto(simboloSeleccionado);
        vPrincipal.getModeloListaAlfabeto().removeElement(simboloSeleccionado);
        vPrincipal.getCmbSimbolo().removeItem(simboloSeleccionado);

        vPrincipal.mostrarMensaje("Símbolo eliminado del alfabeto.");
    }

    private void manejarProbarCadena() {
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

        // Procesar la cadena con output (Máquina de Mealy)
        String resultado = cPrincipal.procesarCadenaConOutput(cadena);

        // Mostrar el resultado en el área de texto
        JTextArea txtResultado = vPrincipal.getTxtResultado();
        
        if (resultado != null) {
            txtResultado.setText("✔ Cadena procesada exitosamente.\n" +
                               "Cadena de entrada: " + cadena + "\n" +
                               "Cadena de salida: " + resultado);
        } else {
            txtResultado.setText("✘ Error al procesar la cadena.");
        }
    }

    private void manejarNuevoAutomata() {
        int respuesta = javax.swing.JOptionPane.showConfirmDialog(
            vPrincipal,
            "¿Está seguro de que desea crear un nuevo autómata? Se perderán todos los datos actuales.",
            "Nuevo Autómata",
            javax.swing.JOptionPane.YES_NO_OPTION
        );

        if (respuesta == javax.swing.JOptionPane.YES_OPTION) {
            cPrincipal.limpiarAutomata();
            actualizarTodosLosComponentes();
        }
    }

    private void manejarValidarAutomata() {
        boolean esValido = cPrincipal.validarAutomata();
        if (esValido) {
            String info = cPrincipal.getInformacionAutomata();
            String tabla = cPrincipal.generarTablaTransicionesMealy();
            vPrincipal.mostrarMensaje(info + "\n" + tabla);
        }
    }

    private void manejarLimpiarTodo() {
        int respuesta = javax.swing.JOptionPane.showConfirmDialog(
            vPrincipal,
            "¿Está seguro de que desea limpiar todo? Se perderán todos los datos.",
            "Limpiar Todo",
            javax.swing.JOptionPane.YES_NO_OPTION
        );

        if (respuesta == javax.swing.JOptionPane.YES_OPTION) {
            cPrincipal.limpiarAutomata();
            actualizarTodosLosComponentes();
        }
    }

    // Métodos auxiliares para actualizar la interfaz
    private void actualizarTablaEstados() {
        vPrincipal.getModeloTablaEstados().setRowCount(0);
        for (Estado e : cPrincipal.getcEstado().getEstados()) {
            vPrincipal.getModeloTablaEstados().addRow(new Object[]{
                e.getNombre(),
                e.isEstadoInicial() ? "Sí" : "No",
                e.isEstadoFinal() ? "Sí" : "No"
            });
        }
    }

    private void actualizarComboBoxEstados() {
        JComboBox<String> desde = vPrincipal.getCmbEstadoDesde();
        JComboBox<String> hasta = vPrincipal.getCmbEstadoHasta();
        
        desde.removeAllItems();
        hasta.removeAllItems();
        
        for (String nombreEstado : cPrincipal.getcEstado().getNombresEstados()) {
            desde.addItem(nombreEstado);
            hasta.addItem(nombreEstado);
        }
    }

    private void actualizarTablaTransiciones() {
        vPrincipal.getModeloTablaTransiciones().setRowCount(0);
        for (Transicion t : cPrincipal.getcTransicion().getTransiciones()) {
            vPrincipal.getModeloTablaTransiciones().addRow(new Object[]{
                t.getEstadoDesde().getNombre(),
                t.getSimbolo(),
                t.getEstadoHasta().getNombre() + "/" + t.getOutput()
            });
        }
    }

    private void limpiarCamposEstado() {
        vPrincipal.getTxtNombreEstado().setText("");
        vPrincipal.getChkEstadoInicial().setSelected(false);
        vPrincipal.getChkEstadoFinal().setSelected(false);
    }

    private void actualizarTodosLosComponentes() {
        // Limpiar tablas
        vPrincipal.getModeloTablaEstados().setRowCount(0);
        vPrincipal.getModeloTablaTransiciones().setRowCount(0);
        
        // Limpiar ComboBoxes
        vPrincipal.getCmbEstadoDesde().removeAllItems();
        vPrincipal.getCmbEstadoHasta().removeAllItems();
        vPrincipal.getCmbSimbolo().removeAllItems();
        
        // Limpiar lista de alfabeto
        vPrincipal.getModeloListaAlfabeto().clear();
        
        // Limpiar campos de texto
        limpiarCamposEstado();
        vPrincipal.getTxtSimbolo().setText("");
        vPrincipal.getTxtCadenaPrueba().setText("");
        vPrincipal.getTxtResultado().setText("");
    }
}
