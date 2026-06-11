package view2;
 
import model.Empleado;

 
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
 
public class PantallaComparativa extends JFrame {
 
    private static final long serialVersionUID = 1L;
    private static final String[] CLAVES = {"Fuerza Bruta", "Backtracking", "Heuristica"};
 
    private HashMap<String, Object[]> mapa;
 
    public PantallaComparativa(HashMap<String, Object[]> mapa) {
        this.mapa = mapa;
 
        setTitle("Programacion III - Comparativa de Algoritmos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 920, 530);
        setResizable(false);
        getContentPane().setLayout(null);
        getContentPane().setBackground(new Color(240, 240, 240));
 
        agregarTitulos();
        agregarTablasPorAlgoritmo();
        agregarTablaResumen();
        agregarGrafico();
    }
 
 
    private void agregarTitulos() {
        String[] titulos = {"Fuerza Bruta", "Backtracking", "Heurística"};
        int[] posX = {15, 310, 605};
        for (int i = 0; i < titulos.length; i++) {
            JLabel lbl = crearEtiqueta(titulos[i], 13, posX[i], 8, 280, 22);
            lbl.setHorizontalAlignment(SwingConstants.CENTER);
            getContentPane().add(lbl);
        }
    }
 
    private void agregarTablasPorAlgoritmo() {
        String[] columnas = {"Legajo", "Rol", "Apellido", "Puntaje"};
 
        for (int i = 0; i < CLAVES.length; i++) {
            DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
            JTable tabla = new JTable(modelo);
            tabla.setEnabled(false);
            centrarColumnas(tabla);
            bloquearAncho(tabla);
            poblarTablaEquipo(tabla, CLAVES[i]);
 
            JScrollPane scroll = new JScrollPane(tabla);
            scroll.setBounds(10 + i * 298, 33, 285, 260);
            getContentPane().add(scroll);
        }
    }
 
    private void poblarTablaEquipo(JTable tabla, String clave) {
        if (!mapa.containsKey(clave)) return;
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0);
        @SuppressWarnings("unchecked")
        List<Empleado> equipo = (List<Empleado>) mapa.get(clave)[0];
        for (Empleado e : equipo) {
            modelo.addRow(new Object[]{e.getLegajo(), e.getRol(), e.getApellido(), e.getPuntaje()});
        }
    }
 
    private void agregarTablaResumen() {
        String[] columnas = {"Algoritmos", "Combinaciones", "Tiempo de Ejecución", "Puntaje Promedio"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
        JTable tabla = new JTable(modelo);
        tabla.setEnabled(false);
        centrarColumnas(tabla);
        bloquearAncho(tabla);
        poblarTablaResumen(tabla);
 
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBounds(430, 370, 468, 90);
        getContentPane().add(scroll);
    }
 
    private void poblarTablaResumen(JTable tabla) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0);
        DecimalFormat df = new DecimalFormat("#");
        df.setRoundingMode(RoundingMode.DOWN);
 
        for (Map.Entry<String, Object[]> entrada : mapa.entrySet()) {
            String algoritmo     = entrada.getKey();
            Object[] valores     = entrada.getValue();
            String combinaciones = df.format((double) valores[1]);
            long tiempoMs        = (long) valores[2];
            double tiempoSeg     = TimeUnit.MILLISECONDS.toSeconds(tiempoMs) + (tiempoMs % 1000) / 1000.0;
            String tiempo        = String.format("%.4fs", tiempoSeg);
            String promedio      = String.format("%.4f", valores[3]);
            modelo.addRow(new Object[]{algoritmo, combinaciones, tiempo, promedio});
        }
    }
 
    private void agregarGrafico() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<String, Object[]> entrada : mapa.entrySet()) {
            double promedio = (double) entrada.getValue()[3];
            dataset.addValue(promedio, "Puntaje Promedio", entrada.getKey());
        }
 
        JFreeChart grafico = ChartFactory.createBarChart(
                "Comparación de Puntaje Promedio",
                "Algoritmo",
                "Puntaje Promedio",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false);
 
        CategoryPlot plot = grafico.getCategoryPlot();
        plot.setBackgroundPaint(Color.LIGHT_GRAY);
        plot.getRangeAxis().setRange(0, 5);
 
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(30, 100, 200));
 
        JPanel panelGrafico = new JPanel(new FlowLayout());
        panelGrafico.setBounds(10, 310, 405, 175);
        getContentPane().add(panelGrafico);
 
        ChartPanel chartPanel = new ChartPanel(grafico);
        chartPanel.setPreferredSize(new Dimension(400, 165));
        panelGrafico.add(chartPanel);
    }
 
 
    private void centrarColumnas(JTable tabla) {
        DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
        centrado.setHorizontalAlignment(SwingConstants.CENTER);
        tabla.setDefaultRenderer(Object.class, centrado);
    }
 
    private void bloquearAncho(JTable tabla) {
        TableColumnModel colModel = tabla.getColumnModel();
        for (int i = 0; i < colModel.getColumnCount(); i++) {
            TableColumn col = colModel.getColumn(i);
            col.setResizable(false);
        }
    }
 
    private JLabel crearEtiqueta(String texto, int tamaño, int x, int y, int ancho, int alto) {
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(Color.BLACK);
        lbl.setFont(new Font("Tahoma", Font.BOLD, tamaño));
        lbl.setBounds(x, y, ancho, alto);
        return lbl;
    }
 
    public void mostrarComparativa(HashMap<String, Object[]> mapa) {
        this.mapa = mapa;
    }
}
