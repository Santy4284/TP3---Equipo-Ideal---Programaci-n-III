package view.presenter;

import model.Empleado;
import model.Empleado.Rol;
import model.GestorEquipo;
import view.navegador.Navegador;
import view2.PantallaEquipo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import dal.IObservadorEquipo;

public class EquipoPresenter implements IObservadorEquipo {

    private final PantallaEquipo _vista;
    private final Navegador _navegador;
    private final GestorEquipo _gestor;
    private final int _cantLideres;
    private final int _cantArquitectos;
    private final int _cantProgramadores;
    private final int _cantTesters;

    // Datos compartidos notificados por el observer
    public static double combinaciones;
    public static double tiempoSeg;
    public static double promedio;
    public static HashMap<String, Object[]> mapaComparativa;

    public EquipoPresenter(PantallaEquipo vista, Navegador navegador, int cantLideres, int cantArquitectos, int cantProgramadores, int cantTesters) {
        _vista = vista;
        _navegador = navegador;
        _gestor = GestorEquipo.getInstancia();
        _cantLideres = cantLideres;
        _cantArquitectos = cantArquitectos;
        _cantProgramadores = cantProgramadores;
        _cantTesters = cantTesters;

        _gestor.agregarObservador(this);
        cargarEmpleadosIniciales();
    }

    private void cargarEmpleadosIniciales() {
        for (Empleado e : _gestor.getEmpleados()) {
            _vista.agregarEmpleadoAlCombo(formatarEmpleado(e));
            for (String legConflicto : e.getConflictos()) {
                Empleado conflictuado = _gestor.buscarPorLegajo(legConflicto);
                if (conflictuado != null) {
                    _vista.agregarConflictoAlCombo(
                            e.getLegajo() + " - " + e.getApellido() + " ---> "
                            + conflictuado.getLegajo() + " - " + conflictuado.getApellido());
                }
            }
        }
    }



    public void onAgregarEmpleadoPulsado(String legajo, String nombre, String apellido, String sPuntaje, String sRol) {
        if (legajo.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || sPuntaje.isEmpty()) {
            _vista.mostrarError("Todos los campos son obligatorios.");
            return;
        }
        int puntaje;
        try {
            puntaje = Integer.parseInt(sPuntaje);
            if (puntaje < 1 || puntaje > 5) {
                _vista.mostrarError("El puntaje debe estar entre 1 y 5.");
                return;
            }
        } catch (NumberFormatException ex) {
            _vista.mostrarError("El puntaje debe ser un número entero.");
            return;
        }

        Rol rol = Rol.valueOf(sRol);
        Empleado nuevo = new Empleado(legajo, nombre, apellido, puntaje, new HashSet<>(), rol, "");
        _gestor.agregarEmpleado(nuevo);
        _vista.agregarEmpleadoAlCombo(formatarEmpleado(nuevo));
        _vista.mostrarExito("El empleado " + nombre + " " + apellido + " fue agregado correctamente.");
    }

    public void onAgregarConflictoPulsado(int indice1, int indice2) {
        List<Empleado> lista = _gestor.getEmpleados();
        if (indice1 == indice2) {
            _vista.mostrarError("Debe seleccionar dos empleados distintos.");
            return;
        }
        Empleado e1 = lista.get(indice1);
        Empleado e2 = lista.get(indice2);
        e1.agregarConflicto(e2.getLegajo());
        e2.agregarConflicto(e1.getLegajo());
        _vista.agregarConflictoAlCombo(e1.getLegajo() + " - " + e1.getApellido() + " ---> " + e2.getLegajo() + " - " + e2.getApellido());
        _vista.mostrarExito("Conflicto entre " + e1.getApellido() + " y " + e2.getApellido() + " se ha registrado correctamente.");
    }

    public void onFuerzaBrutaPulsado() {
    	_vista.mostrarBarra();
        new Thread(() -> {
            List<Empleado> resultado = _gestor.generarEquipoPorFuerzaBruta(_cantLideres, _cantArquitectos, _cantProgramadores, _cantTesters);
            javax.swing.SwingUtilities.invokeLater(() -> {
                _vista.actualizarTablaEquipo(resultado);
                _vista.actualizarStats(combinaciones, tiempoSeg, promedio);
                _vista.ocultarBarra();
            });
        }).start();
    }

    public void onRetrocesoPulsado() {
    	_vista.mostrarBarra();
        new Thread(() -> {
            List<Empleado> resultado = _gestor.generarEquipoPorRetroceso(_cantLideres, _cantArquitectos, _cantProgramadores, _cantTesters);
            javax.swing.SwingUtilities.invokeLater(() -> {
                _vista.actualizarTablaEquipo(resultado);
                _vista.actualizarStats(combinaciones, tiempoSeg, promedio);
                _vista.ocultarBarra();
            });
        }).start();
    }

    public void onHeuristicaPulsado() {
    	_vista.mostrarBarra();
        new Thread(() -> {
            List<Empleado> resultado = _gestor.generarEquipoPorHeuristica(_cantLideres, _cantArquitectos, _cantProgramadores, _cantTesters);
            javax.swing.SwingUtilities.invokeLater(() -> {
                _vista.actualizarTablaEquipo(resultado);
                _vista.actualizarStats(combinaciones, tiempoSeg, promedio);
                _vista.ocultarBarra();
            });
        }).start();
    }

    public void onComparativaPulsado() {
    	_vista.mostrarBarra();
        new Thread(() -> {
            HashMap<String, Object[]> mapa = _gestor.generarComparativa(_cantLideres, _cantArquitectos, _cantProgramadores, _cantTesters);
            javax.swing.SwingUtilities.invokeLater(() -> _navegador.lanzarComparativa(mapa));
            _vista.ocultarBarra();
        }).start();
    }


    @Override
    public void onEquipoGenerado(List<Empleado> equipo, double comb, long tiempo) {
        combinaciones = comb;
        tiempoSeg = TimeUnit.MILLISECONDS.toSeconds(tiempo) + (double)(tiempo % 1000) / 1000.0;
        int suma = 0;
        for (Empleado e : equipo) suma += e.getPuntaje();
        promedio = equipo.isEmpty() ? 0 : (double) suma / equipo.size();
    }

    @Override
    public void onComparativaGenerada(HashMap<String, Object[]> mapa) {
        mapaComparativa = mapa;
    }


    private String formatarEmpleado(Empleado e) {
        return e.getNombre() + " " + e.getApellido() + " - Rol: " + e.getRol() + ", Puntaje: " + e.getPuntaje();
    }
}
