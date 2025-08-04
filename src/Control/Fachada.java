/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;

import Vista.VentanaPrincipal;

/**
 *
 * @author carlosmamut1
 */
public class Fachada {
    private ControlPrincipal cPrincipal;
    private VentanaPrincipal vPrincipal;
    public Fachada(ControlPrincipal cPrincipal) {
        this.cPrincipal = cPrincipal;
    }

    public VentanaPrincipal getvPrincipal() {
        return vPrincipal;
    }
    
   
}
