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
 * Actualizada para Máquina de Mealy con gestión completa de outputs
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

        // Prueba events - ACTUALIZADO para Máquina de Mealy
        this.vPrincipal.getBtnProbarCadena().addActionListener(this);
        this.vPrincipal.getBtnProbarCadenaConOutput().addActionListener(this);

        // Menú events
        this.vPrincipal.getItemNuevo().addActionListener(this);
        this.vPrincipal.getItemValidarAutomata().addActionListener(this);
        this.vPrincipal.getItemTablaTransiciones().addActionListener(this);
        this.vPrincipal.getItemLimpiarTodo().addActionListener(this);
        this.vPrincipal.getItemAcercaDe().addActionListener(this);
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
                
            case "Agregar Transición":
                manejarAgregarTransicion();
                break;
                
            case "Eliminar Transición":
                manejarEliminarTransicion();
                break;
                
            case "Agregar":
                manejarAgregarSimbolo();
                break;
                
            case "Eliminar":
                manejarEliminarSimbolo();
                break;
                
            case "Validar":
                manejarValidarCadena();
                break;
                
            case "Procesar con Output":
                manejarProbarCadenaConOutput();
                break;
                
            case "Nuevo":
                manejarNuevoAutomata();
                break;
                
            case "Validar Máquina":
                manejarValidarAutomata();
                break;
                
            case "Mostrar Tabla de Transiciones":
                manejarMostrarTablaTransiciones();
                break;
                
            case "Limpiar Todo":
                manejarLimpiarTodo();
                break;
                
            case "Acerca de":
                manejarAcercaDe();
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
        String output = vPrincipal.getTxtOutput().getText().trim();

        if (desde == null || hasta == null || simbolo == null) {
            vPrincipal.mostrarMensajeError("Debe seleccionar un estado de origen, un estado de destino y un símbolo.");
            return;
        }

        // Si no se especifica output, usar lambda (cadena vacía)
        if (output.isEmpty()) {
            output = "λ";
        }

        Estado estadoDesde = cPrincipal.getcEstado().buscarEstadoPorNombre(desde);
        Estado estadoHasta = cPrincipal.getcEstado().buscarEstadoPorNombre(hasta);

        if (estadoDesde == null || estadoHasta == null) {
            vPrincipal.mostrarMensajeError("Estados no válidos seleccionados.");
            return;
        }

        cPrincipal.getcTransicion().crearTransicion(estadoDesde, estadoHasta, simbolo, output);
        
        // Agregar output al alfabeto de salida automáticamente
        cPrincipal.agregarSimboloAlfabetoSalida(output);
        
        actualizarTablaTransiciones();
        
        // Limpiar campo de output después de agregar
        vPrincipal.getTxtOutput().setText("");
    }

    private void manejarEliminarTransicion() {
        int filaSeleccionada = vPrincipal.getTablaTransiciones().getSelectedRow();
        if (filaSeleccionada == -1) {
            vPrincipal.mostrarMensajeError("Debe seleccionar una transición para eliminar.");
            return;
        }

        String desde = (String) vPrincipal.getModeloTablaTransiciones().getValueAt(filaSeleccionada, 0);
        String simbolo = (String) vPrincipal.getModeloTablaTransiciones().getValueAt(filaSeleccionada, 1);
        String hastaConOutput = (String) vPrincipal.getModeloTablaTransiciones().getValueAt(filaSeleccionada, 2);
        
        // Separar estado hasta y output (formato: "Estado/Output")
        String[] partes = hastaConOutput.split("/");
        if (partes.length != 2) {
            vPrincipal.mostrarMensajeError("Error al procesar la transición seleccionada.");
            return;
        }
        
        String hasta = partes[0];
        String output = partes[1];

        Estado estadoDesde = cPrincipal.getcEstado().buscarEstadoPorNombre(desde);
        Estado estadoHasta = cPrincipal.getcEstado().buscarEstadoPorNombre(hasta);

        if (estadoDesde == null || estadoHasta == null) {
            vPrincipal.mostrarMensajeError("Error al identificar los estados de la transición.");
            return;
        }

        cPrincipal.getcTransicion().eliminarTransicion(estadoDesde, estadoHasta, simbolo, output);
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

        // Verificar si el símbolo está siendo usado en transiciones
        boolean enUso = false;
        for (Transicion t : cPrincipal.getcTransicion().getTransiciones()) {
            if (t.getSimbolo().equals(simboloSeleccionado)) {
                enUso = true;
                break;
            }
        }
        
        if (enUso) {
            vPrincipal.mostrarMensajeError("No se puede eliminar el símbolo. Está siendo usado en transiciones.");
            return;
        }

        cPrincipal.eliminarSimboloAlfabeto(simboloSeleccionado);
        vPrincipal.getModeloListaAlfabeto().removeElement(simboloSeleccionado);
        vPrincipal.getCmbSimbolo().removeItem(simboloSeleccionado);

        vPrincipal.mostrarMensaje("Símbolo eliminado del alfabeto.");
    }

    private void manejarValidarCadena() {
        String cadena = vPrincipal.getTxtCadenaPrueba().getText().trim();

        if (cadena.isEmpty()) {
            vPrincipal.mostrarMensajeError("Ingrese una cadena para validar.");
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

        // Usar el método tradicional de validación (solo aceptación/rechazo)
        boolean aceptada = cPrincipal.procesarCadena(cadena);
        
        JTextArea txtResultado = vPrincipal.getTxtResultado();
        if (aceptada) {
            txtResultado.setText("✔ CADENA ACEPTADA\n" +
                               "Cadena: " + cadena + "\n" +
                               "La cadena fue procesada exitosamente y terminó en un estado final.");
        } else {
            txtResultado.setText("✘ CADENA RECHAZADA\n" +
                               "Cadena: " + cadena + "\n" +
                               "La cadena fue rechazada (ver consola para detalles).");
        }
    }

    private void manejarProbarCadenaConOutput() {
        String cadena = vPrincipal.getTxtCadenaPrueba().getText().trim();

        if (cadena.isEmpty()) {
            vPrincipal.mostrarMensajeError("Ingrese una cadena para procesar.");
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
            txtResultado.setText("=== PROCESAMIENTO MÁQUINA DE MEALY ===\n" +
                               "✔ Cadena procesada exitosamente\n\n" +
                               "Cadena de entrada: " + cadena + "\n" +
                               "Cadena de salida:  " + resultado + "\n\n" +
                               "Ver ventana de mensajes para la traza completa de ejecución.");
        } else {
            txtResultado.setText("=== PROCESAMIENTO MÁQUINA DE MEALY ===\n" +
                               "✘ Error al procesar la cadena\n\n" +
                               "Cadena de entrada: " + cadena + "\n" +
                               "La cadena no pudo ser procesada completamente.\n" +
                               "Ver ventana de mensajes para detalles del error.");
        }
    }

    private void manejarNuevoAutomata() {
        int respuesta = javax.swing.JOptionPane.showConfirmDialog(
            vPrincipal,
            "¿Está seguro de que desea crear una nueva máquina? Se perderán todos los datos actuales.",
            "Nueva Máquina de Mealy",
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
            vPrincipal.mostrarMensaje("MÁQUINA DE MEALY VÁLIDA\n\n" + info);
        }
    }

    private void manejarMostrarTablaTransiciones() {
        String tabla = cPrincipal.generarTablaTransicionesMealy();
        
        // Crear una ventana de diálogo para mostrar la tabla
        javax.swing.JDialog dialogoTabla = new javax.swing.JDialog(vPrincipal, "Tabla de Transiciones - Máquina de Mealy", true);
        javax.swing.JTextArea areaTabla = new javax.swing.JTextArea(tabla);
        areaTabla.setFont(new java.awt.Font(java.awt.Font.MONOSPACED, java.awt.Font.PLAIN, 12));
        areaTabla.setEditable(false);
        
        javax.swing.JScrollPane scrollTabla = new javax.swing.JScrollPane(areaTabla);
        scrollTabla.setPreferredSize(new java.awt.Dimension(600, 400));
        
        dialogoTabla.add(scrollTabla);
        dialogoTabla.pack();
        dialogoTabla.setLocationRelativeTo(vPrincipal);
        dialogoTabla.setVisible(true);
    }

    private void manejarLimpiarTodo() {
        int respuesta = javax.swing.JOptionPane.showConfirmDialog(
            vPrincipal,
            "¿Está seguro de que desea limpiar todo? Se perderán todos los datos.",
            "Limpiar Máquina de Mealy",
            javax.swing.JOptionPane.YES_NO_OPTION
        );

        if (respuesta == javax.swing.JOptionPane.YES_OPTION) {
            cPrincipal.limpiarAutomata();
            actualizarTodosLosComponentes();
        }
    }

    private void manejarAcercaDe() {
        String mensaje = "EDITOR DE MÁQUINAS DE MEALY\n\n" +
                        "Versión: 2.0\n" +
                        "Autor: Sistema de Gestión de Autómatas\n\n" +
                        "Características principales:\n" +
                        "• Creación y edición de estados\n" +
                        "• Gestión de transiciones con outputs\n" +
                        "• Validación de máquinas de Mealy\n" +
                        "• Procesamiento de cadenas con salida\n" +
                        "• Generación de tablas de transiciones\n\n" +
                        "Una máquina de Mealy es un autómata finito que\n" +
                        "produce una salida para cada transición de estado.";
        
        vPrincipal.mostrarMensaje(mensaje);
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
        vPrincipal.getTxtOutput().setText("");
        vPrincipal.getTxtCadenaPrueba().setText("");
        vPrincipal.getTxtResultado().setText("");
    }
}
