/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;

import Vista.VentanaPrincipal;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author carlosmamut1
 */
public class Fachada implements ActionListener{
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
        if("Agregar Estado".equals(ae.getActionCommand())) {
            System.out.println("Agregar Estado");
        }
        if("Eliminar Estado".equals(ae.getActionCommand())) {
            System.out.println("Eliminar Estado");
        }
        if("Agregar Transicion".equals(ae.getActionCommand())) {
            System.out.println("Agregar Transicion");
        }
        if("Eliminar Transicion".equals(ae.getActionCommand())) {
            System.out.println("Eliminar Transicion");
        }
        if("Agregar".equals(ae.getActionCommand())) {
            System.out.println("Agregar Simbolo");
        }
        if("Eliminar".equals(ae.getActionCommand())) {
            System.out.println("Eliminar Simbolo");
        }
        if("Probar".equals(ae.getActionCommand())) {
            System.out.println("Probar Cadena");
        }
    }
    
   
}
