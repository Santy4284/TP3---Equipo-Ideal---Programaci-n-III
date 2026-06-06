package view2;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import model.Empleado;

public class PantallaComparativa {

    private JFrame frame;
    private JPanel panelContenido;

    public PantallaComparativa() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Comparativa de Algoritmos");
        frame.setSize(900, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());

        panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.setBackground(new Color(30, 30, 60));

        JScrollPane scroll = new JScrollPane(panelContenido);
        frame.getContentPane().add(scroll, BorderLayout.CENTER);
    }

    public void mostrarComparativa(HashMap<String, Object[]> mapa) {
        panelContenido.removeAll();

        JLabel titulo = new JLabel("COMPARATIVA DE ALGORITMOS");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Tahoma", Font.BOLD, 20));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        panelContenido.add(titulo);

        for (java.util.Map.Entry<String, Object[]> entrada : mapa.entrySet()) {
            String algoritmo = entrada.getKey();
            Object[] valores = entrada.getValue();
            @SuppressWarnings("unchecked")
            List<Empleado> equipo  = (List<Empleado>) valores[0];
            double combinaciones   = (double) valores[1];
            long tiempo            = (long) valores[2];
            double promedio        = (double) valores[3];

            JPanel bloque = new JPanel();
            bloque.setLayout(new BoxLayout(bloque, BoxLayout.Y_AXIS));
            bloque.setBackground(new Color(50, 50, 80));
            bloque.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.CYAN, 1),
                    BorderFactory.createEmptyBorder(10, 20, 10, 20)));
            bloque.setAlignmentX(Component.CENTER_ALIGNMENT);
            bloque.setMaximumSize(new Dimension(840, 999));

            bloque.add(crearLinea("▶  " + algoritmo, 16, Color.CYAN));
            bloque.add(crearLinea("Combinaciones evaluadas: " + (long) combinaciones, 13, Color.WHITE));
            bloque.add(crearLinea(String.format("Tiempo de ejecucion: %d ms", tiempo), 13, Color.WHITE));
            bloque.add(crearLinea(String.format("Puntaje promedio del equipo: %.2f", promedio), 13, Color.WHITE));
            bloque.add(Box.createVerticalStrut(6));
            for (Empleado e : equipo) {
                bloque.add(crearLinea("  • " + e.getNombre() + " " + e.getApellido()
                        + " | Rol: " + e.getRol() + " | Puntaje: " + e.getPuntaje(), 12, Color.LIGHT_GRAY));
            }

            panelContenido.add(Box.createVerticalStrut(10));
            panelContenido.add(bloque);
        }

        panelContenido.revalidate();
        panelContenido.repaint();
    }

    private JLabel crearLinea(String texto, int tamaño, Color color) {
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(color);
        lbl.setFont(new Font("Tahoma", Font.PLAIN, tamaño));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    public JFrame getFrame() { return frame; }
}
