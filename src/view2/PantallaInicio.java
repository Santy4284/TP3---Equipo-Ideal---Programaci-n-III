package view2;

import view.presenter.InicioPresenter;

import javax.swing.*;
import java.awt.*;

public class PantallaInicio {

    private JFrame frame;
    private JTextField txtLideres;
    private JTextField txtArquitectos;
    private JTextField txtProgramadores;
    private JTextField txtTesters;
    private InicioPresenter presenter;

    public PantallaInicio() {
        initialize();
    }

    public void setPresenter(InicioPresenter presenter) {
        this.presenter = presenter;
    }

    private void initialize() {
        frame = new JFrame("Programacion III - Equipo Ideal");
        frame.setBounds(100, 100, 505, 350);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        Icon fondoIcon = new ImageIcon("src/dal/imagenes/fondo1.png");
        JLabel fondoLabel = new JLabel(fondoIcon);
        fondoLabel.setBounds(0, 0, 600, 400);

        JLabel lblTitulo = crearEtiqueta("CONFIGURACIÓN DEL EQUIPO REQUERIDO", 20, 53, 39, 411, 50);
        JLabel lblLideres = crearEtiqueta("Lideres de Proyecto", 14, 53, 103, 200, 20);
        JLabel lblArqs = crearEtiqueta("Arquitectos", 14, 53, 134, 200, 20);
        JLabel lblProg = crearEtiqueta("Programadores", 14, 53, 165, 200, 20);
        JLabel lblTest = crearEtiqueta("Testers", 14, 53, 196, 200, 20);

        txtLideres      = crearCampoNumerico(313, 103);
        txtArquitectos  = crearCampoNumerico(313, 134);
        txtProgramadores = crearCampoNumerico(313, 165);
        txtTesters      = crearCampoNumerico(313, 196);

        JButton btnContinuar = new JButton("ARMAR EQUIPO");
        btnContinuar.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnContinuar.setBounds(165, 260, 170, 30);
        btnContinuar.addActionListener(e -> {
            if (presenter != null) presenter.onContinuarPulsado();
        });

        frame.getContentPane().add(lblTitulo);
        frame.getContentPane().add(lblLideres);
        frame.getContentPane().add(lblArqs);
        frame.getContentPane().add(lblProg);
        frame.getContentPane().add(lblTest);
        frame.getContentPane().add(txtLideres);
        frame.getContentPane().add(txtArquitectos);
        frame.getContentPane().add(txtProgramadores);
        frame.getContentPane().add(txtTesters);
        frame.getContentPane().add(btnContinuar);
        frame.getContentPane().add(fondoLabel);
    }

    private JTextField crearCampoNumerico(int x, int y) {
        JTextField campo = new JTextField();
        campo.setBounds(x, y, 107, 20);
        campo.setHorizontalAlignment(JTextField.CENTER);
        campo.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar())) e.consume();
            }
        });
        return campo;
    }

    private JLabel crearEtiqueta(String texto, int tamaño, int x, int y, int ancho, int alto) {
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Tahoma", Font.BOLD, tamaño));
        lbl.setBounds(x, y, ancho, alto);
        return lbl;
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(frame, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void cerrar() { frame.dispose(); }
    public JFrame getFrame() { return frame; }

    public String getCantidadLideres()       { return txtLideres.getText(); }
    public String getCantidadArquitectos()   { return txtArquitectos.getText(); }
    public String getCantidadProgramadores() { return txtProgramadores.getText(); }
    public String getCantidadTesters()       { return txtTesters.getText(); }
}
