// Updated Transicion.java - Máquina de Mealy con OUTPUT
package Modelo;

public class Transicion {
    private Estado estadoDesde;
    private Estado estadoHasta;
    private String simbolo;
    private String output; // NUEVA FUNCIONALIDAD: Output de la transición

    public Transicion(Estado estadoDesde, Estado estadoHasta, String simbolo, String output) {
        this.estadoDesde = estadoDesde;
        this.estadoHasta = estadoHasta;
        this.simbolo = simbolo;
        this.output = output;
    }

    // Constructor para compatibilidad (output por defecto)
    public Transicion(Estado estadoDesde, Estado estadoHasta, String simbolo) {
        this(estadoDesde, estadoHasta, simbolo, "λ"); // λ = salida vacía
    }

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

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    @Override
    public String toString() {
        return "(" + estadoDesde.getNombre() + ", " + simbolo + ") -> " + estadoHasta.getNombre() + " / " + output;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Transicion that = (Transicion) obj;
        return estadoDesde.equals(that.estadoDesde) &&
               estadoHasta.equals(that.estadoHasta) &&
               simbolo.equals(that.simbolo) &&
               output.equals(that.output);
    }

    @Override
    public int hashCode() {
        return estadoDesde.hashCode() + estadoHasta.hashCode() + simbolo.hashCode() + output.hashCode();
    }
}