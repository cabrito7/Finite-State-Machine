// Updated ControlPrincipal.java - Máquina de Mealy
package Control;

import Modelo.Estado;
import Modelo.Transicion;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.QuadCurve2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ControlPrincipal {
    private ControlEstado cEstado;
    private ControlTransicion cTransicion;
    private Fachada fachada;
    private ArrayList<String> alfabeto;
    private ArrayList<String> alfabetoSalida; // NUEVO: Alfabeto de salida

    public ControlPrincipal() {
        this.cEstado = new ControlEstado(this);
        this.cTransicion = new ControlTransicion(this);
        this.fachada = new Fachada(this);
        this.alfabeto = new ArrayList<>();
        this.alfabetoSalida = new ArrayList<>(); // NUEVO
    }

    public Fachada getFachada() {
        return fachada;
    }

    public ControlEstado getcEstado() {
        return cEstado;
    }

    public ControlTransicion getcTransicion() {
        return cTransicion;
    }

    public ArrayList<String> getAlfabeto() {
        return new ArrayList<>(alfabeto);
    }

    // NUEVO: Gestión del alfabeto de salida
    public ArrayList<String> getAlfabetoSalida() {
        return new ArrayList<>(alfabetoSalida);
    }

    public void agregarSimboloAlfabeto(String simbolo) {
        if (!alfabeto.contains(simbolo)) {
            alfabeto.add(simbolo);
        }
    }

    public void eliminarSimboloAlfabeto(String simbolo) {
        alfabeto.remove(simbolo);
    }

    public void limpiarAlfabeto() {
        alfabeto.clear();
    }

    // NUEVO: Gestión alfabeto de salida
    public void agregarSimboloAlfabetoSalida(String simbolo) {
        if (!alfabetoSalida.contains(simbolo)) {
            alfabetoSalida.add(simbolo);
        }
    }

    public void eliminarSimboloAlfabetoSalida(String simbolo) {
        alfabetoSalida.remove(simbolo);
    }

    public void limpiarAlfabetoSalida() {
        alfabetoSalida.clear();
    }

    // NUEVO: Procesar cadena con OUTPUT (Máquina de Mealy)
    public String procesarCadenaConOutput(String cadena) {
        Estado estadoInicial = cEstado.getEstadoInicial();
        if (estadoInicial == null) {
            this.fachada.getvPrincipal().mostrarMensajeError("No hay estado inicial definido.");
            return null;
        }

        Estado estadoActual = estadoInicial;
        StringBuilder output = new StringBuilder();
        StringBuilder trazaEjecucion = new StringBuilder();
        
        trazaEjecucion.append("=== TRAZA DE EJECUCIÓN ===\n");
        trazaEjecucion.append("Estado inicial: ").append(estadoActual.getNombre()).append("\n");
        trazaEjecucion.append("Cadena de entrada: ").append(cadena).append("\n\n");
        
        for (int i = 0; i < cadena.length(); i++) {
            String simbolo = String.valueOf(cadena.charAt(i));
            
            // Buscar transición desde el estado actual con el símbolo
            Transicion transicion = cTransicion.buscarTransicionPorEstadoYSimbolo(estadoActual, simbolo);
            
            if (transicion == null) {
                String mensaje = "ERROR: No hay transición para el símbolo '" + simbolo + "' desde el estado '" + estadoActual.getNombre() + "'";
                trazaEjecucion.append(mensaje).append("\n");
                this.fachada.getvPrincipal().mostrarMensaje(trazaEjecucion.toString());
                return null;
            }
            
            // Agregar output de la transición
            output.append(transicion.getOutput());
            
            // Traza de ejecución
            trazaEjecucion.append("Paso ").append(i + 1).append(": ");
            trazaEjecucion.append("δ(").append(estadoActual.getNombre()).append(", ").append(simbolo).append(") = ");
            trazaEjecucion.append(transicion.getEstadoHasta().getNombre());
            trazaEjecucion.append(" | Output: ").append(transicion.getOutput()).append("\n");
            
            estadoActual = transicion.getEstadoHasta();
        }
        
        String resultado = output.toString();
        trazaEjecucion.append("\n=== RESULTADO ===\n");
        trazaEjecucion.append("Estado final: ").append(estadoActual.getNombre()).append("\n");
        trazaEjecucion.append("Cadena de salida: ").append(resultado).append("\n");
        
        this.fachada.getvPrincipal().mostrarMensaje(trazaEjecucion.toString());
        
        return resultado;
    }

    // Método original para compatibilidad (solo validación)
    public boolean procesarCadena(String cadena) {
        Estado estadoInicial = cEstado.getEstadoInicial();
        if (estadoInicial == null) {
            this.fachada.getvPrincipal().mostrarMensajeError("No hay estado inicial definido.");
            return false;
        }

        Estado estadoActual = estadoInicial;
        
        for (int i = 0; i < cadena.length(); i++) {
            String simbolo = String.valueOf(cadena.charAt(i));
            
            Transicion transicion = cTransicion.buscarTransicionPorEstadoYSimbolo(estadoActual, simbolo);
            
            if (transicion == null) {
                this.fachada.getvPrincipal().mostrarMensaje("Cadena rechazada: No hay transición para el símbolo '" + simbolo + "' desde el estado '" + estadoActual.getNombre() + "'");
                return false;
            }
            
            estadoActual = transicion.getEstadoHasta();
        }
        
        boolean aceptada = estadoActual.isEstadoFinal();
        if (aceptada) {
            this.fachada.getvPrincipal().mostrarMensaje("Cadena aceptada. Estado final: " + estadoActual.getNombre());
        } else {
            this.fachada.getvPrincipal().mostrarMensaje("Cadena rechazada: El estado final '" + estadoActual.getNombre() + "' no es de aceptación.");
        }
        
        return aceptada;
    }

    // Validar si el autómata está bien formado (actualizado para Máquina de Mealy)
    public boolean validarAutomata() {
        // Verificar que hay al menos un estado
        if (cEstado.getCantidadEstados() == 0) {
            this.fachada.getvPrincipal().mostrarMensajeError("El autómata debe tener al menos un estado.");
            return false;
        }
        
        // Verificar que hay exactamente un estado inicial
        if (!cEstado.tieneEstadoInicial()) {
            this.fachada.getvPrincipal().mostrarMensajeError("El autómata debe tener exactamente un estado inicial.");
            return false;
        }
        
        // Para máquina de Mealy, verificar que todas las transiciones tengan definición completa
        for (Estado estado : cEstado.getEstados()) {
            for (String simbolo : alfabeto) {
                Transicion t = cTransicion.buscarTransicionPorEstadoYSimbolo(estado, simbolo);
                if (t == null) {
                    this.fachada.getvPrincipal().mostrarMensajeError("Función de transición incompleta: Falta definir δ(" + estado.getNombre() + ", " + simbolo + ")");
                    return false;
                }
            }
        }
        
        this.fachada.getvPrincipal().mostrarMensaje("La máquina de Mealy está correctamente formada.");
        return true;
    }

    // Obtener información del autómata (actualizado)
    public String getInformacionAutomata() {
        StringBuilder info = new StringBuilder();
        info.append("=== INFORMACIÓN DE LA MÁQUINA DE MEALY ===\n");
        info.append("Estados: ").append(cEstado.getCantidadEstados()).append("\n");
        info.append("Transiciones: ").append(cTransicion.getCantidadTransiciones()).append("\n");
        info.append("Alfabeto de entrada: ").append(alfabeto.toString()).append("\n");
        info.append("Alfabeto de salida: ").append(alfabetoSalida.toString()).append("\n");
        
        Estado inicial = cEstado.getEstadoInicial();
        if (inicial != null) {
            info.append("Estado inicial: ").append(inicial.getNombre()).append("\n");
        }
        
        ArrayList<Estado> finales = cEstado.getEstadosFinales();
        if (!finales.isEmpty()) {
            info.append("Estados finales: ");
            for (int i = 0; i < finales.size(); i++) {
                info.append(finales.get(i).getNombre());
                if (i < finales.size() - 1) info.append(", ");
            }
            info.append("\n");
        }
        
        return info.toString();
    }

    // Limpiar todo el autómata (actualizado)
    public void limpiarAutomata() {
        cTransicion.limpiarTransiciones();
        cEstado.limpiarEstados();
        limpiarAlfabeto();
        limpiarAlfabetoSalida(); // NUEVO
        this.fachada.getvPrincipal().mostrarMensaje("Máquina de Mealy limpiada completamente.");
    }

    // Verificar si hay estados inalcanzables
    public ArrayList<Estado> getEstadosInalcanzables() {
        ArrayList<Estado> inalcanzables = new ArrayList<>();
        Estado inicial = cEstado.getEstadoInicial();
        
        if (inicial == null) {
            return inalcanzables;
        }
        
        Set<Estado> alcanzables = new HashSet<>();
        Stack<Estado> pila = new Stack<>();
        
        pila.push(inicial);
        alcanzables.add(inicial);
        
        while (!pila.isEmpty()) {
            Estado actual = pila.pop();
            for (Transicion t : cTransicion.getTransicionesDesdeEstado(actual)) {
                Estado destino = t.getEstadoHasta();
                if (!alcanzables.contains(destino)) {
                    alcanzables.add(destino);
                    pila.push(destino);
                }
            }
        }
        
        for (Estado e : cEstado.getEstados()) {
            if (!alcanzables.contains(e)) {
                inalcanzables.add(e);
            }
        }
        
        return inalcanzables;
    }

    // NUEVO: Generar tabla de transiciones con outputs (Máquina de Mealy)
    public String generarTablaTransicionesMealy() {
        StringBuilder tabla = new StringBuilder();
        tabla.append("=== TABLA DE TRANSICIONES - MÁQUINA DE MEALY ===\n");
        
        ArrayList<Estado> estados = cEstado.getEstados();
        ArrayList<String> simbolos = new ArrayList<>(alfabeto);
        
        if (estados.isEmpty() || simbolos.isEmpty()) {
            tabla.append("No hay estados o alfabeto definido.\n");
            return tabla.toString();
        }
        
        // Encabezado
        tabla.append(String.format("%-15s", "Estado"));
        for (String simbolo : simbolos) {
            tabla.append(String.format("%-15s", simbolo));
        }
        tabla.append("\n");
        
        // Línea separadora
        tabla.append("-".repeat(15 + simbolos.size() * 15)).append("\n");
        
        // Filas
        for (Estado estado : estados) {
            String nombreEstado = estado.getNombre();
            if (estado.isEstadoInicial()) nombreEstado += " (I)";
            if (estado.isEstadoFinal()) nombreEstado += " (F)";
            
            tabla.append(String.format("%-15s", nombreEstado));
            
            for (String simbolo : simbolos) {
                String celda = "-";
                Transicion t = cTransicion.buscarTransicionPorEstadoYSimbolo(estado, simbolo);
                if (t != null) {
                    celda = t.getEstadoHasta().getNombre() + "/" + t.getOutput();
                }
                tabla.append(String.format("%-15s", celda));
            }
            tabla.append("\n");
        }
        
        return tabla.toString();
    }

    // Método original para compatibilidad
    public String generarTablaTransiciones() {
        return generarTablaTransicionesMealy();
    }
    
    // NUEVO: Método para generar visualización del grafo (agregar a ControlPrincipal.java)
public void mostrarGrafoMaquinaMealy() {
    ArrayList<Estado> estados = cEstado.getEstados();
    ArrayList<Transicion> transiciones = cTransicion.getTransiciones();
    
    if (estados.isEmpty()) {
        this.fachada.getvPrincipal().mostrarMensajeError("No hay estados definidos para mostrar el grafo.");
        return;
    }
    
    // Crear ventana para mostrar el grafo
    JFrame ventanaGrafo = new JFrame("Grafo de la Máquina de Mealy");
    ventanaGrafo.setSize(800, 600);
    ventanaGrafo.setLocationRelativeTo(this.fachada.getvPrincipal());
    
    // Panel personalizado para dibujar el grafo
    JPanel panelGrafo = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            dibujarGrafo(g2d, getWidth(), getHeight());
        }
    };
    
    panelGrafo.setBackground(Color.WHITE);
    
    JScrollPane scroll = new JScrollPane(panelGrafo);
    ventanaGrafo.add(scroll);
    
    // Botón para cerrar
    JPanel panelBotones = new JPanel();
    JButton btnCerrar = new JButton("Cerrar");
    btnCerrar.addActionListener(e -> ventanaGrafo.dispose());
    panelBotones.add(btnCerrar);
    
    // Botón para exportar (opcional)
    JButton btnExportar = new JButton("Exportar Imagen");
    btnExportar.addActionListener(e -> exportarGrafoComoImagen(panelGrafo));
    panelBotones.add(btnExportar);
    
    ventanaGrafo.add(panelBotones, BorderLayout.SOUTH);
    ventanaGrafo.setVisible(true);
}

private void dibujarGrafo(Graphics2D g2d, int ancho, int alto) {
    ArrayList<Estado> estados = cEstado.getEstados();
    ArrayList<Transicion> transiciones = cTransicion.getTransiciones();
    
    if (estados.isEmpty()) return;
    
    // Configurar colores y fuentes
    Font fuenteEstado = new Font("Arial", Font.BOLD, 14);
    Font fuenteTransicion = new Font("Arial", Font.PLAIN, 12);
    Color colorEstadoNormal = new Color(173, 216, 230); // Azul claro
    Color colorEstadoInicial = new Color(144, 238, 144); // Verde claro
    Color colorEstadoFinal = new Color(255, 182, 193); // Rosa claro
    Color colorBorde = Color.BLACK;
    Color colorTexto = Color.BLACK;
    Color colorFlecha = Color.BLUE;
    
    // Calcular posiciones de los estados en círculo
    Map<Estado, Point> posiciones = new HashMap<>();
    int centroX = ancho / 2;
    int centroY = alto / 2;
    int radio = Math.min(ancho, alto) / 3;
    
    for (int i = 0; i < estados.size(); i++) {
        double angulo = 2 * Math.PI * i / estados.size();
        int x = centroX + (int) (radio * Math.cos(angulo));
        int y = centroY + (int) (radio * Math.sin(angulo));
        posiciones.put(estados.get(i), new Point(x, y));
    }
    
    // Dibujar transiciones (arcos)
    g2d.setColor(colorFlecha);
    for (Transicion t : transiciones) {
        Point desde = posiciones.get(t.getEstadoDesde());
        Point hasta = posiciones.get(t.getEstadoHasta());
        
        if (desde != null && hasta != null) {
            dibujarTransicion(g2d, desde, hasta, t.getSimbolo(), t.getOutput(), 
                            t.getEstadoDesde().equals(t.getEstadoHasta()), fuenteTransicion);
        }
    }
    
    // Dibujar estados (círculos)
    int radioEstado = 40;
    for (Estado estado : estados) {
        Point pos = posiciones.get(estado);
        if (pos == null) continue;
        
        // Determinar color del estado
        Color colorFondo = colorEstadoNormal;
        if (estado.isEstadoInicial() && estado.isEstadoFinal()) {
            colorFondo = new Color(255, 255, 0); // Amarillo para inicial y final
        } else if (estado.isEstadoInicial()) {
            colorFondo = colorEstadoInicial;
        } else if (estado.isEstadoFinal()) {
            colorFondo = colorEstadoFinal;
        }
        
        // Dibujar círculo del estado
        g2d.setColor(colorFondo);
        g2d.fillOval(pos.x - radioEstado, pos.y - radioEstado, radioEstado * 2, radioEstado * 2);
        
        g2d.setColor(colorBorde);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawOval(pos.x - radioEstado, pos.y - radioEstado, radioEstado * 2, radioEstado * 2);
        
        // Círculo doble para estados finales
        if (estado.isEstadoFinal()) {
            g2d.drawOval(pos.x - radioEstado + 5, pos.y - radioEstado + 5, 
                        (radioEstado - 5) * 2, (radioEstado - 5) * 2);
        }
        
        // Flecha de entrada para estado inicial
        if (estado.isEstadoInicial()) {
            g2d.setColor(colorFlecha);
            int[] xPoints = {pos.x - radioEstado - 30, pos.x - radioEstado - 10, pos.x - radioEstado - 10};
            int[] yPoints = {pos.y, pos.y - 8, pos.y + 8};
            g2d.fillPolygon(xPoints, yPoints, 3);
            g2d.drawLine(pos.x - radioEstado - 50, pos.y, pos.x - radioEstado - 30, pos.y);
        }
        
        // Texto del estado
        g2d.setColor(colorTexto);
        g2d.setFont(fuenteEstado);
        FontMetrics fm = g2d.getFontMetrics();
        String nombre = estado.getNombre();
        int anchoTexto = fm.stringWidth(nombre);
        int altoTexto = fm.getHeight();
        g2d.drawString(nombre, pos.x - anchoTexto / 2, pos.y + altoTexto / 4);
    }
    
    // Dibujar leyenda
    dibujarLeyenda(g2d, ancho, alto);
}

private void dibujarTransicion(Graphics2D g2d, Point desde, Point hasta, String simbolo, 
                              String output, boolean esLoop, Font fuente) {
    g2d.setFont(fuente);
    g2d.setStroke(new BasicStroke(1.5f));
    
    if (esLoop) {
        // Dibujar loop (círculo pequeño encima del estado)
        int radioLoop = 25;
        int centroLoopX = desde.x;
        int centroLoopY = desde.y - 65;
        
        g2d.drawOval(centroLoopX - radioLoop, centroLoopY - radioLoop, radioLoop * 2, radioLoop * 2);
        
        // Flecha del loop
        double anguloFlecha = Math.PI / 4;
        int xFlecha = centroLoopX + (int) (radioLoop * Math.cos(anguloFlecha));
        int yFlecha = centroLoopY + (int) (radioLoop * Math.sin(anguloFlecha));
        
        dibujarPuntaFlecha(g2d, xFlecha, yFlecha, anguloFlecha + Math.PI / 2);
        
        // Etiqueta del loop
        String etiqueta = simbolo + "/" + output;
        FontMetrics fm = g2d.getFontMetrics();
        int anchoEtiqueta = fm.stringWidth(etiqueta);
        g2d.drawString(etiqueta, centroLoopX - anchoEtiqueta / 2, centroLoopY - radioLoop - 10);
        
    } else {
        // Calcular puntos en el borde de los círculos
        int radioEstado = 40;
        double angulo = Math.atan2(hasta.y - desde.y, hasta.x - desde.x);
        
        int x1 = desde.x + (int) (radioEstado * Math.cos(angulo));
        int y1 = desde.y + (int) (radioEstado * Math.sin(angulo));
        int x2 = hasta.x - (int) (radioEstado * Math.cos(angulo));
        int y2 = hasta.y - (int) (radioEstado * Math.sin(angulo));
        
        // Dibujar línea curva para evitar superposición
        int controlX = (x1 + x2) / 2 + (int) (30 * Math.cos(angulo + Math.PI / 2));
        int controlY = (y1 + y2) / 2 + (int) (30 * Math.sin(angulo + Math.PI / 2));
        
        QuadCurve2D.Double curva = new QuadCurve2D.Double(x1, y1, controlX, controlY, x2, y2);
        g2d.draw(curva);
        
        // Punta de flecha
        dibujarPuntaFlecha(g2d, x2, y2, angulo);
        
        // Etiqueta de la transición
        String etiqueta = simbolo + "/" + output;
        FontMetrics fm = g2d.getFontMetrics();
        int anchoEtiqueta = fm.stringWidth(etiqueta);
        
        // Posicionar etiqueta en el punto de control de la curva
        g2d.setColor(Color.WHITE);
        g2d.fillRect(controlX - anchoEtiqueta / 2 - 2, controlY - fm.getHeight() / 2 - 2,
                    anchoEtiqueta + 4, fm.getHeight() + 4);
        
        g2d.setColor(Color.BLACK);
        g2d.drawString(etiqueta, controlX - anchoEtiqueta / 2, controlY + fm.getHeight() / 4);
    }
}

private void dibujarPuntaFlecha(Graphics2D g2d, int x, int y, double angulo) {
    int longitudFlecha = 12;
    double anguloFlecha = Math.PI / 6;
    
    int x1 = x - (int) (longitudFlecha * Math.cos(angulo - anguloFlecha));
    int y1 = y - (int) (longitudFlecha * Math.sin(angulo - anguloFlecha));
    int x2 = x - (int) (longitudFlecha * Math.cos(angulo + anguloFlecha));
    int y2 = y - (int) (longitudFlecha * Math.sin(angulo + anguloFlecha));
    
    g2d.drawLine(x, y, x1, y1);
    g2d.drawLine(x, y, x2, y2);
}

private void dibujarLeyenda(Graphics2D g2d, int ancho, int alto) {
    g2d.setColor(Color.BLACK);
    g2d.setFont(new Font("Arial", Font.PLAIN, 11));
    
    int x = 10;
    int y = alto - 100;
    
    g2d.drawString("LEYENDA:", x, y);
    y += 15;
    
    // Estado normal
    g2d.setColor(new Color(173, 216, 230));
    g2d.fillOval(x, y, 20, 20);
    g2d.setColor(Color.BLACK);
    g2d.drawOval(x, y, 20, 20);
    g2d.drawString("Estado normal", x + 25, y + 14);
    y += 25;
    
    // Estado inicial
    g2d.setColor(new Color(144, 238, 144));
    g2d.fillOval(x, y, 20, 20);
    g2d.setColor(Color.BLACK);
    g2d.drawOval(x, y, 20, 20);
    g2d.drawString("Estado inicial", x + 25, y + 14);
    y += 25;
    
    // Estado final
    g2d.setColor(new Color(255, 182, 193));
    g2d.fillOval(x, y, 20, 20);
    g2d.setColor(Color.BLACK);
    g2d.drawOval(x, y, 20, 20);
    g2d.drawOval(x + 3, y + 3, 14, 14);
    g2d.drawString("Estado final", x + 25, y + 14);
}

private void exportarGrafoComoImagen(JPanel panel) {
    try {
        BufferedImage imagen = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = imagen.createGraphics();
        panel.printAll(g2d);
        g2d.dispose();
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PNG Images", "png"));
        
        if (fileChooser.showSaveDialog(this.fachada.getvPrincipal()) == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            if (!archivo.getName().toLowerCase().endsWith(".png")) {
                archivo = new File(archivo.getAbsolutePath() + ".png");
            }
            
            javax.imageio.ImageIO.write(imagen, "png", archivo);
            this.fachada.getvPrincipal().mostrarMensaje("Grafo exportado exitosamente como: " + archivo.getName());
        }
    } catch (Exception e) {
        this.fachada.getvPrincipal().mostrarMensajeError("Error al exportar el grafo: " + e.getMessage());
    }
}
}