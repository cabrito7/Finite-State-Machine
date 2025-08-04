/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Vista;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 *
 * @author carlosmamut1
 */
public class VentanaPrincipal extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName());
    
    // Components for States Management
    private JPanel panelEstados;
    private JTextField txtNombreEstado;
    private JCheckBox chkEstadoInicial;
    private JCheckBox chkEstadoFinal;
    private JButton btnAgregarEstado;
    private JButton btnEliminarEstado;
    private JTable tablaEstados;
    private DefaultTableModel modeloTablaEstados;
    private JScrollPane scrollEstados;
    
    // Components for Transitions Management
    private JPanel panelTransiciones;
    private JComboBox<String> cmbEstadoDesde;
    private JComboBox<String> cmbEstadoHasta;
    private JComboBox<String> cmbSimbolo;
    private JButton btnAgregarTransicion;
    private JButton btnEliminarTransicion;
    private JTable tablaTransiciones;
    private DefaultTableModel modeloTablaTransiciones;
    private JScrollPane scrollTransiciones;
    
    // Components for Alphabet Management
    private JPanel panelAlfabeto;
    private JTextField txtSimbolo;
    private JButton btnAgregarSimbolo;
    private JButton btnEliminarSimbolo;
    private JList<String> listaAlfabeto;
    private DefaultListModel<String> modeloListaAlfabeto;
    private JScrollPane scrollAlfabeto;
    
    // Components for String Testing
    private JPanel panelPrueba;
    private JTextField txtCadenaPrueba;
    private JButton btnProbarCadena;
    private JTextArea txtResultado;
    private JScrollPane scrollResultado;
    
    // Menu components
    private JMenuBar menuBar;
    private JMenu menuArchivo;
    private JMenu menuAutomata;
    private JMenu menuAyuda;
    private JMenuItem itemNuevo;
    private JMenuItem itemAbrir;
    private JMenuItem itemGuardar;
    private JMenuItem itemSalir;
    private JMenuItem itemValidarAutomata;
    private JMenuItem itemLimpiarTodo;
    private JMenuItem itemAcercaDe;

    /**
     * Creates new form VentanaPrincipal
     */
    public VentanaPrincipal() {
        initComponents();
        initCustomComponents();
        setupLayout();
        setTitle("Editor de Autómatas Finitos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
    
    private void initCustomComponents() {
        // Initialize menu
        setupMenu();
        
        // Initialize States Panel
        setupStatesPanel();
        
        // Initialize Transitions Panel
        setupTransitionsPanel();
        
        // Initialize Alphabet Panel
        setupAlphabetPanel();
        
        // Initialize Testing Panel
        setupTestingPanel();
    }
    
    private void setupMenu() {
        menuBar = new JMenuBar();
        
        // File Menu
        menuArchivo = new JMenu("Archivo");
        itemNuevo = new JMenuItem("Nuevo");
        itemAbrir = new JMenuItem("Abrir");
        itemGuardar = new JMenuItem("Guardar");
        itemSalir = new JMenuItem("Salir");
        
        menuArchivo.add(itemNuevo);
        menuArchivo.add(itemAbrir);
        menuArchivo.add(itemGuardar);
        menuArchivo.addSeparator();
        menuArchivo.add(itemSalir);
        
        // Automaton Menu
        menuAutomata = new JMenu("Autómata");
        itemValidarAutomata = new JMenuItem("Validar Autómata");
        itemLimpiarTodo = new JMenuItem("Limpiar Todo");
        
        menuAutomata.add(itemValidarAutomata);
        menuAutomata.add(itemLimpiarTodo);
        
        // Help Menu
        menuAyuda = new JMenu("Ayuda");
        itemAcercaDe = new JMenuItem("Acerca de");
        menuAyuda.add(itemAcercaDe);
        
        menuBar.add(menuArchivo);
        menuBar.add(menuAutomata);
        menuBar.add(menuAyuda);
        
        setJMenuBar(menuBar);
    }
    
    private void setupStatesPanel() {
        panelEstados = new JPanel();
        panelEstados.setBorder(new TitledBorder("Gestión de Estados"));
        panelEstados.setLayout(new BorderLayout());
        
        // Input panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Nombre:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        txtNombreEstado = new JTextField(15);
        inputPanel.add(txtNombreEstado, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        chkEstadoInicial = new JCheckBox("Estado Inicial");
        inputPanel.add(chkEstadoInicial, gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        chkEstadoFinal = new JCheckBox("Estado Final");
        inputPanel.add(chkEstadoFinal, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        btnAgregarEstado = new JButton("Agregar Estado");
        inputPanel.add(btnAgregarEstado, gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        btnEliminarEstado = new JButton("Eliminar Estado");
        inputPanel.add(btnEliminarEstado, gbc);
        
        // Table
        String[] columnasEstados = {"Nombre", "Inicial", "Final"};
        modeloTablaEstados = new DefaultTableModel(columnasEstados, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaEstados = new JTable(modeloTablaEstados);
        scrollEstados = new JScrollPane(tablaEstados);
        scrollEstados.setPreferredSize(new Dimension(300, 150));
        
        panelEstados.add(inputPanel, BorderLayout.NORTH);
        panelEstados.add(scrollEstados, BorderLayout.CENTER);
    }
    
    private void setupTransitionsPanel() {
        panelTransiciones = new JPanel();
        panelTransiciones.setBorder(new TitledBorder("Gestión de Transiciones"));
        panelTransiciones.setLayout(new BorderLayout());
        
        // Input panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Estado Desde:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        cmbEstadoDesde = new JComboBox<>();
        cmbEstadoDesde.setPreferredSize(new Dimension(120, 25));
        inputPanel.add(cmbEstadoDesde, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Estado Hasta:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        cmbEstadoHasta = new JComboBox<>();
        cmbEstadoHasta.setPreferredSize(new Dimension(120, 25));
        inputPanel.add(cmbEstadoHasta, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(new JLabel("Símbolo:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        cmbSimbolo = new JComboBox<>();
        cmbSimbolo.setPreferredSize(new Dimension(120, 25));
        inputPanel.add(cmbSimbolo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        btnAgregarTransicion = new JButton("Agregar Transicion");
        inputPanel.add(btnAgregarTransicion, gbc);
        
        gbc.gridx = 1; gbc.gridy = 3;
        btnEliminarTransicion = new JButton("Eliminar Transicion");
        inputPanel.add(btnEliminarTransicion, gbc);
        
        // Table
        String[] columnasTransiciones = {"Estado Desde", "Símbolo", "Estado Hasta"};
        modeloTablaTransiciones = new DefaultTableModel(columnasTransiciones, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaTransiciones = new JTable(modeloTablaTransiciones);
        scrollTransiciones = new JScrollPane(tablaTransiciones);
        scrollTransiciones.setPreferredSize(new Dimension(300, 150));
        
        panelTransiciones.add(inputPanel, BorderLayout.NORTH);
        panelTransiciones.add(scrollTransiciones, BorderLayout.CENTER);
    }
    
    private void setupAlphabetPanel() {
        panelAlfabeto = new JPanel();
        panelAlfabeto.setBorder(new TitledBorder("Alfabeto"));
        panelAlfabeto.setLayout(new BorderLayout());
        
        // Input panel
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Símbolo:"));
        txtSimbolo = new JTextField(10);
        inputPanel.add(txtSimbolo);
        btnAgregarSimbolo = new JButton("Agregar");
        inputPanel.add(btnAgregarSimbolo);
        btnEliminarSimbolo = new JButton("Eliminar");
        inputPanel.add(btnEliminarSimbolo);
        
        // List
        modeloListaAlfabeto = new DefaultListModel<>();
        listaAlfabeto = new JList<>(modeloListaAlfabeto);
        listaAlfabeto.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollAlfabeto = new JScrollPane(listaAlfabeto);
        scrollAlfabeto.setPreferredSize(new Dimension(200, 150));
        
        panelAlfabeto.add(inputPanel, BorderLayout.NORTH);
        panelAlfabeto.add(scrollAlfabeto, BorderLayout.CENTER);
    }
    
    private void setupTestingPanel() {
        panelPrueba = new JPanel();
        panelPrueba.setBorder(new TitledBorder("Probar Cadenas"));
        panelPrueba.setLayout(new BorderLayout());
        
        // Input panel
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Cadena a probar:"));
        txtCadenaPrueba = new JTextField(20);
        inputPanel.add(txtCadenaPrueba);
        btnProbarCadena = new JButton("Probar");
        inputPanel.add(btnProbarCadena);
        
        // Result area
        txtResultado = new JTextArea(5, 30);
        txtResultado.setEditable(false);
        txtResultado.setBackground(getBackground());
        scrollResultado = new JScrollPane(txtResultado);
        
        panelPrueba.add(inputPanel, BorderLayout.NORTH);
        panelPrueba.add(scrollResultado, BorderLayout.CENTER);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Main content panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        
        // Top row: States and Transitions
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0.5; gbc.weighty = 0.4;
        mainPanel.add(panelEstados, gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        mainPanel.add(panelTransiciones, gbc);
        
        // Bottom row: Alphabet and Testing
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0.3; gbc.weighty = 0.6;
        mainPanel.add(panelAlfabeto, gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.weightx = 0.7;
        mainPanel.add(panelPrueba, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Status bar
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusBar.add(new JLabel("Listo"));
        add(statusBar, BorderLayout.SOUTH);
    }
    
    // Getter methods for accessing components from controllers
    public JTextField getTxtNombreEstado() {
        return txtNombreEstado;
    }
    
    public JCheckBox getChkEstadoInicial() {
        return chkEstadoInicial;
    }
    
    public JCheckBox getChkEstadoFinal() {
        return chkEstadoFinal;
    }
    
    public JButton getBtnAgregarEstado() {
        return btnAgregarEstado;
    }
    
    public JButton getBtnEliminarEstado() {
        return btnEliminarEstado;
    }
    
    public JTable getTablaEstados() {
        return tablaEstados;
    }
    
    public DefaultTableModel getModeloTablaEstados() {
        return modeloTablaEstados;
    }
    
    public JComboBox<String> getCmbEstadoDesde() {
        return cmbEstadoDesde;
    }
    
    public JComboBox<String> getCmbEstadoHasta() {
        return cmbEstadoHasta;
    }
    
    public JComboBox<String> getCmbSimbolo() {
        return cmbSimbolo;
    }
    
    public JButton getBtnAgregarTransicion() {
        return btnAgregarTransicion;
    }
    
    public JButton getBtnEliminarTransicion() {
        return btnEliminarTransicion;
    }
    
    public JTable getTablaTransiciones() {
        return tablaTransiciones;
    }
    
    public DefaultTableModel getModeloTablaTransiciones() {
        return modeloTablaTransiciones;
    }
    
    public JTextField getTxtSimbolo() {
        return txtSimbolo;
    }
    
    public JButton getBtnAgregarSimbolo() {
        return btnAgregarSimbolo;
    }
    
    public JButton getBtnEliminarSimbolo() {
        return btnEliminarSimbolo;
    }
    
    public JList<String> getListaAlfabeto() {
        return listaAlfabeto;
    }
    
    public DefaultListModel<String> getModeloListaAlfabeto() {
        return modeloListaAlfabeto;
    }
    
    public JTextField getTxtCadenaPrueba() {
        return txtCadenaPrueba;
    }
    
    public JButton getBtnProbarCadena() {
        return btnProbarCadena;
    }
    
    public JTextArea getTxtResultado() {
        return txtResultado;
    }
    
    public JMenuItem getItemNuevo() {
        return itemNuevo;
    }
    
    public JMenuItem getItemAbrir() {
        return itemAbrir;
    }
    
    public JMenuItem getItemGuardar() {
        return itemGuardar;
    }
    
    public JMenuItem getItemSalir() {
        return itemSalir;
    }
    
    public JMenuItem getItemValidarAutomata() {
        return itemValidarAutomata;
    }
    
    public JMenuItem getItemLimpiarTodo() {
        return itemLimpiarTodo;
    }
    
    public JMenuItem getItemAcercaDe() {
        return itemAcercaDe;
    }
    
    public void mostrarMensajeError(String msj) {
        JOptionPane.showMessageDialog(this, msj, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public void mostrarMensaje(String msj) {
        JOptionPane.showMessageDialog(this, msj, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
 

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
