package Modelo;

/**
 * Clase que representa un estado en el autómata finito
 * @author carlosmamut1
 */
public class Estado {
    private String nombre;
    private boolean estadoInicial;
    private boolean estadoFinal; // Cambio: ahora es private

    public Estado(String nombre, boolean estadoInicial, boolean estadoFinal) {
        this.nombre = nombre;
        this.estadoInicial = estadoInicial;
        this.estadoFinal = estadoFinal;
    }

    // Getters y Setters
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

    @Override
    public String toString() {
        return nombre + (estadoInicial ? " (I)" : "") + (estadoFinal ? " (F)" : "");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Estado estado = (Estado) obj;
        return nombre.equals(estado.nombre);
    }

    @Override
    public int hashCode() {
        return nombre.hashCode();
    }
}