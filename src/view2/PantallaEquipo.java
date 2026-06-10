package view2;

import model.Empleado;
import view.presenter.EquipoPresenter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PantallaEquipo {

    private JFrame frame;
    private JTextField txtLegajo;
    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtPuntaje;
    private JComboBox<String> comboRol;
    private JComboBox<String> comboListaEmpleados;
    private JComboBox<String> comboConflicto1;
    private JComboBox<String> comboConflicto2;
    private JComboBox<String> comboListaConflictos;
    private JTable tablaEquipo;
    private DefaultTableModel modeloTabla;
    private JLabel lblCombinaciones;
    private JLabel lblTiempo;
    private JLabel lblPromedio;
    private EquipoPresenter presenter;
    private JProgressBar barra;

    public PantallaEquipo() {
        initialize();
    }

    public void setPresenter(EquipoPresenter presenter) {
        this.presenter = presenter;
    }

    private void initialize() {
        frame = new JFrame("Programacion III - Constructor de Equipo");
        frame.getContentPane().setBackground(new Color(0, 204, 255));
        frame.setBounds(100, 100, 900, 820);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        Icon fondoIcon = new ImageIcon("/src/imagenes/fondo.jpg");
        JLabel fondoLabel = new JLabel(fondoIcon);
        fondoLabel.setBounds(0, 0, 800, 800);

        // ── Sección: Datos de nuevo empleado ──────────────────────────────────
        JLabel lblNuevoEmp = crearEtiqueta("GESTOR DE EMPLEADOS", 18, 70, 11, 300, 45);
        JLabel lblLeg = crearEtiqueta("Legajo", 14, 47, 67, 99, 29);
        JLabel lblNom = crearEtiqueta("Nombre", 14, 47, 107, 99, 29);
        JLabel lblApe = crearEtiqueta("Apellido", 14, 47, 147, 99, 29);
        JLabel lblPun = crearEtiqueta("Puntaje", 14, 47, 193, 99, 29);
        JLabel lblRol = crearEtiqueta("Rol", 14, 47, 233, 99, 29);

        txtLegajo   = crearCampo(176, 67, 148, 29);
        txtNombre   = crearCampo(176, 107, 148, 29);
        txtApellido = crearCampo(176, 147, 148, 29);
        txtPuntaje  = crearCampo(176, 193, 148, 29);
        comboRol = new JComboBox<>(new String[]{"Lider", "Arquitecto", "Programador", "Tester"});
        comboRol.setBounds(176, 233, 148, 29);

        JButton btnAgregarEmp = new JButton("Agregar Empleado");
        btnAgregarEmp.setBounds(176, 280, 148, 30);
        btnAgregarEmp.addActionListener(e -> {
            if (presenter != null) presenter.onAgregarEmpleadoPulsado(
                    txtLegajo.getText(), txtNombre.getText(), txtApellido.getText(),
                    txtPuntaje.getText(), (String) comboRol.getSelectedItem());
        });

        // ── Sección: Conflictos ───────────────────────────────────────────────
        JLabel lblConflictos = crearEtiqueta("GESTOR DE CONFLICTOS", 18, 580, 11, 300, 45);
        comboConflicto1 = new JComboBox<>();
        comboConflicto1.setBounds(470, 70, 175, 22);
        comboConflicto2 = new JComboBox<>();
        comboConflicto2.setBounds(470, 105, 175, 25);

        JButton btnAgregarConflicto = new JButton("Agregar Conflicto");
        btnAgregarConflicto.setBounds(680, 90, 160, 22);
        btnAgregarConflicto.addActionListener(e -> {
            if (presenter != null) presenter.onAgregarConflictoPulsado(
                    comboConflicto1.getSelectedIndex(), comboConflicto2.getSelectedIndex());
        });

        // ── Sección: Lista de empleados ────────────────────────────────────────
        JLabel lblListaEmp = crearEtiqueta("LISTA DE EMPLEADOS", 14, 560, 160, 200, 22);
        comboListaEmpleados = new JComboBox<>();
        comboListaEmpleados.setBounds(493, 200, 335, 22);

        JLabel lblListaConflictos = crearEtiqueta("LISTA DE CONFLICTOS", 14, 560, 260, 200, 22);
        comboListaConflictos = new JComboBox<>();
        comboListaConflictos.setBounds(493, 300, 335, 22);

        // ── Tabla resultados ───────────────────────────────────────────────────
        JLabel lblTabla = crearEtiqueta("LISTA COMPLETA DE EMPLEADOS", 14, 140, 400, 300, 22);
        modeloTabla = new DefaultTableModel(new Object[]{"Legajo", "Nombre", "Apellido", "Puntaje", "Rol"}, 0);
        tablaEquipo = new JTable(modeloTabla);
        JScrollPane scroll = new JScrollPane(tablaEquipo);
        scroll.setBounds(17, 430, 855, 260);

        // ── Stats ──────────────────────────────────────────────────────────────
        lblCombinaciones = crearEtiqueta("", 14, 450, 400, 300, 22);
        lblCombinaciones.setHorizontalAlignment(SwingConstants.CENTER);
        lblTiempo = crearEtiqueta("", 14, 284, 735, 452, 22);
        lblTiempo.setHorizontalAlignment(SwingConstants.CENTER);
        lblPromedio = crearEtiqueta("", 14, 284, 755, 452, 22);
        lblPromedio.setHorizontalAlignment(SwingConstants.CENTER);
        
        // ── Barra de Progreso ──────────────────────────────────────────────────────────────
        barra = new JProgressBar();
        barra.setBounds(620, 700, 250, 40);
        barra.setVisible(false);
        
        // ── Botones de algoritmos ──────────────────────────────────────────────
        JButton btnFuerzaBruta = new JButton("Fuerza Bruta");
        btnFuerzaBruta.setBounds(17, 700, 130, 40);
        btnFuerzaBruta.addActionListener(e -> { if (presenter != null) presenter.onFuerzaBrutaPulsado(); });

        JButton btnRetroceso = new JButton("BackTracking");
        btnRetroceso.setBounds(165, 700, 160, 40);
        btnRetroceso.addActionListener(e -> { if (presenter != null) presenter.onRetrocesoPulsado(); });

        JButton btnHeuristica = new JButton("Heuristica");
        btnHeuristica.setBounds(345, 700, 110, 40);
        btnHeuristica.addActionListener(e -> { if (presenter != null) presenter.onHeuristicaPulsado(); });

        JButton btnComparativa = new JButton("Comparativa");
        btnComparativa.setBounds(475, 700, 110, 40);
        btnComparativa.addActionListener(e -> { if (presenter != null) presenter.onComparativaPulsado(); });

        // ── Agregar todo al frame ──────────────────────────────────────────────
        for (Component c : new Component[]{
                lblNuevoEmp, lblLeg, lblNom, lblApe, lblPun, lblRol,
                txtLegajo, txtNombre, txtApellido, txtPuntaje, comboRol, btnAgregarEmp,
                lblConflictos, comboConflicto1, comboConflicto2, btnAgregarConflicto,
                lblListaEmp, comboListaEmpleados,
                lblListaConflictos, comboListaConflictos,
                lblTabla, scroll,
                lblCombinaciones, lblTiempo, lblPromedio,
                btnFuerzaBruta, btnRetroceso, btnHeuristica, btnComparativa,
                fondoLabel, barra
        }) {
            frame.getContentPane().add(c);
        }
    }

    // ── Métodos de actualización de UI ─────────────────────────────────────────

    public void mostrarBarra() {
    	barra.setVisible(true);
    	barra.setIndeterminate(true);
    }
    
    public void ocultarBarra() {
    	barra.setVisible(false);
    }
    
    public void agregarEmpleadoAlCombo(String descripcion) {
        comboListaEmpleados.addItem(descripcion);
        comboConflicto1.addItem(descripcion);
        comboConflicto2.addItem(descripcion);
    }

    public void actualizarTablaEquipo(List<Empleado> equipo) {
        modeloTabla.setRowCount(0);
        for (Empleado e : equipo) {
            modeloTabla.addRow(new Object[]{
                    e.getLegajo(), e.getNombre(), e.getApellido(), e.getPuntaje(), e.getRol()
            });
        }
    }

    public void agregarConflictoAlCombo(String descripcion) {
        comboListaConflictos.addItem(descripcion);
    }

    public void actualizarStats(double combinaciones, double tiempoSeg, double promedio) {
        lblCombinaciones.setText("Combinaciones evaluadas: " + (long) combinaciones);
        lblTiempo.setText(String.format("Tiempo de ejecucion: %.3f s", tiempoSeg));
        lblPromedio.setText(String.format("Puntaje promedio: %.2f", promedio));
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(frame, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public JFrame getFrame() { return frame; }

    // ── Helpers ────────────────────────────────────────────────────────────────

    private JTextField crearCampo(int x, int y, int ancho, int alto) {
        JTextField txt = new JTextField();
        txt.setBounds(x, y, ancho, alto);
        txt.setHorizontalAlignment(JTextField.CENTER);
        return txt;
    }

    private JLabel crearEtiqueta(String texto, int tamaño, int x, int y, int ancho, int alto) {
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(Color.BLACK);
        lbl.setFont(new Font("Tahoma", Font.BOLD, tamaño));
        lbl.setBounds(x, y, ancho, alto);
        return lbl;
    }
}
