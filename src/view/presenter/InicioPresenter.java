package view.presenter;

import view.navegador.Navegador;
import view2.PantallaInicio;

public class InicioPresenter {

    private final PantallaInicio vista;
    private final Navegador navegador;

    public InicioPresenter(PantallaInicio vista, Navegador navegador) {
        this.vista = vista;
        this.navegador = navegador;
    }

    public void onContinuarPulsado() {
        String sLideres      = vista.getCantidadLideres();
        String sArquitectos  = vista.getCantidadArquitectos();
        String sProgramadores = vista.getCantidadProgramadores();
        String sTesters      = vista.getCantidadTesters();

        if (sLideres.isEmpty() || sArquitectos.isEmpty() || sProgramadores.isEmpty() || sTesters.isEmpty()) {
            vista.mostrarError("Debe completar todos los campos de configuración del equipo.");
            return;
        }

        int lideres      = Integer.parseInt(sLideres);
        int arquitectos  = Integer.parseInt(sArquitectos);
        int programadores = Integer.parseInt(sProgramadores);
        int testers      = Integer.parseInt(sTesters);

        if (lideres <= 0 || arquitectos <= 0 || programadores <= 0 || testers <= 0) {
            vista.mostrarError("Todos los valores deben ser mayores a cero.");
            return;
        }

        vista.cerrar();
        navegador.lanzarEquipo(lideres, arquitectos, programadores, testers);
    }
}
