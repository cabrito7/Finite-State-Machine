/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import Modelo.Estado;

/**
 *
 * @author carlosmamut1
 */
public class Transicion {
    private Estado estadoDesde;
    private Estado estadoHasta;
    private String simbolo;

    

    public Estado getEstadoDesde() {
        return estadoDesde;
    }

    public void setEstadoDesde(Estado estadoDesde) {
        this.estadoDesde = estadoDesde;
    }

    public Estado getEstadoHasta() {
        return estadoHasta;
    }

    public void setEstadoHasta(Estado estadoHasta) {
        this.estadoHasta = estadoHasta;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }
    
    
}
