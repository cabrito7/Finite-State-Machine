// Updated Transicion.java
package Modelo;

public class Transicion {
    private Estado estadoDesde;
    private Estado estadoHasta;
    private String simbolo;

    public Transicion(Estado estadoDesde, Estado estadoHasta, String simbolo) {
        this.estadoDesde = estadoDesde;
        this.estadoHasta = estadoHasta;
        this.simbolo = simbolo;
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

    @Override
    public String toString() {
        return "(" + estadoDesde.getNombre() + ", " + simbolo + ") -> " + estadoHasta.getNombre();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Transicion that = (Transicion) obj;
        return estadoDesde.equals(that.estadoDesde) &&
               estadoHasta.equals(that.estadoHasta) &&
               simbolo.equals(that.simbolo);
    }

    @Override
    public int hashCode() {
        return estadoDesde.hashCode() + estadoHasta.hashCode() + simbolo.hashCode();
    }
}
