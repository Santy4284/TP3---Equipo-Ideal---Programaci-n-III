package view.navegador;

import dal.CargaDatos;
import dal.ICargaDatos;
import dal.ObservadorArchivoEquipo;
import model.GestorEquipo;
import view.presenter.EquipoPresenter;
import view.presenter.InicioPresenter;
import view2.PantallaComparativa;
import view2.PantallaEquipo;
import view2.PantallaInicio;

import javax.swing.JFrame;
import java.util.HashMap;

public class Navegador {

    public Navegador() {
        // Inicializar el gestor con datos y observadores al arrancar
        GestorEquipo gestor = GestorEquipo.getInstancia();
        gestor.agregarObservador(new ObservadorArchivoEquipo());

        ICargaDatos cargador = new CargaDatos();
        gestor.setEmpleados(cargador.cargarEmpleadosDesdeJSON());
    }

    public void lanzarInicio() {
        PantallaInicio vista = new PantallaInicio();
        InicioPresenter presenter = new InicioPresenter(vista, this);
        vista.setPresenter(presenter);
        configurarVentana(vista.getFrame());
    }

    public void lanzarEquipo(int lideres, int arquitectos, int programadores, int testers) {
        PantallaEquipo vista = new PantallaEquipo();
        EquipoPresenter presenter = new EquipoPresenter(vista, this, lideres, arquitectos, programadores, testers);
        vista.setPresenter(presenter);
        configurarVentana(vista.getFrame());
    }

    public void lanzarComparativa(HashMap<String, Object[]> mapa) {
        PantallaComparativa vista = new PantallaComparativa();
        vista.mostrarComparativa(mapa);
        configurarVentana(vista.getFrame());
    }

    private void configurarVentana(JFrame frame) {
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
    }
}
