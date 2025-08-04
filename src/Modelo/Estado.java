/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author carlosmamut1
 */
public class Estado {
    private String nombre;
    private boolean estadoInicial;
    public boolean estadoFinal;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isEstadoInicial() {
        return estadoInicial;
    }

    public void setEstadoInicial(boolean estadoInicial) {
        this.estadoInicial = estadoInicial;
    }

    public boolean isEstadoFinal() {
        return estadoFinal;
    }

    public void setEstadoFinal(boolean estadoFinal) {
        this.estadoFinal = estadoFinal;
    }

    public Estado(String nombre, boolean estadoInicial, boolean estadoFinal) {
        this.nombre = nombre;
        this.estadoInicial = estadoInicial;
        this.estadoFinal = estadoFinal;
    }
    
    
            
}
