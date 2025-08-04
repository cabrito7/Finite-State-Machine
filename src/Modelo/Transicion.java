package Modelo;

/**
 * Clase que representa una transición en una Máquina de Mealy
 * Incluye estado origen, estado destino, símbolo de entrada y output
 * @author carlosmamut1
 */
public class Transicion {
    private Estado estadoDesde;
    private Estado estadoHasta;
    private String simbolo;
    private String output; // NUEVO: Output para Máquina de Mealy

    /**
     * Constructor para Máquina de Mealy con output
     * @param estadoDesde Estado origen de la transición
     * @param estadoHasta Estado destino de la transición
     * @param simbolo Símbolo de entrada que activa la transición
     * @param output Salida producida por la transición
     */
    public Transicion(Estado estadoDesde, Estado estadoHasta, String simbolo, String output) {
        this.estadoDesde = estadoDesde;
        this.estadoHasta = estadoHasta;
        this.simbolo = simbolo;
        this.output = output != null ? output : "λ"; // Default a lambda si es null
    }

    /**
     * Constructor de compatibilidad (sin output explícito)
     * @param estadoDesde Estado origen
     * @param estadoHasta Estado destino
     * @param simbolo Símbolo de entrada
     */
    public Transicion(Estado estadoDesde, Estado estadoHasta, String simbolo) {
        this(estadoDesde, estadoHasta, simbolo, "λ");
    }

    // Getters y Setters
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

    /**
     * Obtiene el output de la transición
     * @return Output de la transición
     */
    public String getOutput() {
        return output;
    }

    /**
     * Establece el output de la transición
     * @param output Nuevo output
     */
    public void setOutput(String output) {
        this.output = output != null ? output : "λ";
    }

    /**
     * Verifica si esta transición es aplicable desde un estado con un símbolo dado
     * @param estado Estado origen
     * @param simboloEntrada Símbolo de entrada
     * @return true si la transición es aplicable
     */
    public boolean esAplicable(Estado estado, String simboloEntrada) {
        return this.estadoDesde.equals(estado) && this.simbolo.equals(simboloEntrada);
    }

    /**
     * Ejecuta la transición y retorna el estado destino
     * @return Estado destino de la transición
     */
    public Estado ejecutar() {
        return this.estadoHasta;
    }

    /**
     * Representación en cadena de la transición para Máquina de Mealy
     * @return Representación textual: δ(q1, a) = q2 / output
     */
    @Override
    public String toString() {
        return String.format("δ(%s, %s) = %s / %s", 
                           estadoDesde.getNombre(), 
                           simbolo, 
                           estadoHasta.getNombre(),
                           output);
    }

    /**
     * Representación compacta de la transición
     * @return Formato: origen-simbolo->destino/output
     */
    public String toCompactString() {
        return String.format("%s-%s->%s/%s", 
                           estadoDesde.getNombre(), 
                           simbolo, 
                           estadoHasta.getNombre(),
                           output);
    }

    /**
     * Compara dos transiciones para verificar igualdad
     * @param obj Objeto a comparar
     * @return true si las transiciones son iguales
     */
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

    /**
     * Genera código hash para la transición
     * @return Código hash
     */
    @Override
    public int hashCode() {
        int result = estadoDesde.hashCode();
        result = 31 * result + estadoHasta.hashCode();
        result = 31 * result + simbolo.hashCode();
        result = 31 * result + output.hashCode();
        return result;
    }

    /**
     * Verifica si la transición produce una salida específica
     * @param expectedOutput Salida esperada
     * @return true si la transición produce la salida esperada
     */
    public boolean produceOutput(String expectedOutput) {
        return this.output.equals(expectedOutput);
    }

    /**
     * Verifica si la transición tiene salida vacía (lambda)
     * @return true si el output es lambda o cadena vacía
     */
    public boolean tieneOutputVacio() {
        return output.equals("λ") || output.equals("") || output.isEmpty();
    }

    /**
     * Crea una copia de esta transición
     * @return Nueva instancia de Transicion con los mismos valores
     */
    public Transicion clonar() {
        return new Transicion(this.estadoDesde, this.estadoHasta, this.simbolo, this.output);
    }

    /**
     * Representa la transición en formato de función de transición extendida
     * Útil para mostrar en tablas de transiciones
     * @return Formato: estado_destino/output
     */
    public String toTableFormat() {
        return estadoHasta.getNombre() + "/" + output;
    }
}