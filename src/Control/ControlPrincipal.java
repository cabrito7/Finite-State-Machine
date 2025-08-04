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
public class ControlPrincipal {
   private ControlEstado cEstado;
   private ControlTransicion cTransicion;
   private Fachada fachada;
   private ArrayList<String> alfabeto;

    public ControlPrincipal() {
        this.cEstado = new ControlEstado(this);
        this.cTransicion = new ControlTransicion(this);
        this.fachada = new Fachada(this);
        
    }
    public Fachada getFachada() {
        return fachada;
    }
    
   
   
   
}
