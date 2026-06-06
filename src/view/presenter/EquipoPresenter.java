package view.presenter;

import model.Empleado;
import model.Empleado.Rol;
import model.GestorEquipo;
import model.IObservadorEquipo;
import view.navegador.Navegador;
import view2.PantallaEquipo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class EquipoPresenter implements IObservadorEquipo {

    private final PantallaEquipo vista;
    private final Navegador navegador;
    private final GestorEquipo gestor;
    private final int cantLideres;
    private final int cantArquitectos;
    private final int cantProgramadores;
    private final int cantTesters;

    // Datos compartidos notificados por el observer
    public static double combinaciones;
    public static double tiempoSeg;
    public static double promedio;
    public static HashMap<String, Object[]> mapaComparativa;

    public EquipoPresenter(PantallaEquipo vista, Navegador navegador,
                           int cantLideres, int cantArquitectos, int cantProgramadores, int cantTesters) {
        this.vista = vista;
        this.navegador = navegador;
        this.gestor = GestorEquipo.getInstancia();
        this.cantLideres = cantLideres;
        this.cantArquitectos = cantArquitectos;
        this.cantProgramadores = cantProgramadores;
        this.cantTesters = cantTesters;

        gestor.agregarObservador(this);
        cargarEmpleadosIniciales();
    }

    // ── Carga inicial ──────────────────────────────────────────────────────────

    private void cargarEmpleadosIniciales() {
        for (Empleado e : gestor.getEmpleados()) {
            vista.agregarEmpleadoAlCombo(formatarEmpleado(e));
            for (String legConflicto : e.getConflictos()) {
                Empleado conflictuado = gestor.buscarPorLegajo(legConflicto);
                if (conflictuado != null) {
                    vista.agregarConflictoAlCombo(
                            e.getLegajo() + " - " + e.getApellido() + " ---> "
                            + conflictuado.getLegajo() + " - " + conflictuado.getApellido());
                }
            }
        }
    }

    // ── Acciones del usuario ───────────────────────────────────────────────────

    public void onAgregarEmpleadoPulsado(String legajo, String nombre, String apellido,
                                          String sPuntaje, String sRol) {
        if (legajo.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || sPuntaje.isEmpty()) {
            vista.mostrarError("Todos los campos son obligatorios.");
            return;
        }
        int puntaje;
        try {
            puntaje = Integer.parseInt(sPuntaje);
            if (puntaje < 1 || puntaje > 10) {
                vista.mostrarError("El puntaje debe estar entre 1 y 10.");
                return;
            }
        } catch (NumberFormatException ex) {
            vista.mostrarError("El puntaje debe ser un número entero.");
            return;
        }

        Rol rol = Rol.valueOf(sRol);
        Empleado nuevo = new Empleado(legajo, nombre, apellido, puntaje, new HashSet<>(), rol, "");
        gestor.agregarEmpleado(nuevo);
        vista.agregarEmpleadoAlCombo(formatarEmpleado(nuevo));
    }

    public void onAgregarConflictoPulsado(int indice1, int indice2) {
        List<Empleado> lista = gestor.getEmpleados();
        if (indice1 == indice2) {
            vista.mostrarError("Debe seleccionar dos empleados distintos.");
            return;
        }
        Empleado e1 = lista.get(indice1);
        Empleado e2 = lista.get(indice2);
        e1.agregarConflicto(e2.getLegajo());
        e2.agregarConflicto(e1.getLegajo());
        vista.agregarConflictoAlCombo(e1.getLegajo() + " - " + e1.getApellido()
                + " ---> " + e2.getLegajo() + " - " + e2.getApellido());
    }

    public void onFuerzaBrutaPulsado() {
        new Thread(() -> {
            List<Empleado> resultado = gestor.generarEquipoPorFuerzaBruta(
                    cantLideres, cantArquitectos, cantProgramadores, cantTesters);
            javax.swing.SwingUtilities.invokeLater(() -> {
                vista.actualizarTablaEquipo(resultado);
                vista.actualizarStats(combinaciones, tiempoSeg, promedio);
            });
        }).start();
    }

    public void onRetrocesoPulsado() {
        new Thread(() -> {
            List<Empleado> resultado = gestor.generarEquipoPorRetroceso(
                    cantLideres, cantArquitectos, cantProgramadores, cantTesters);
            javax.swing.SwingUtilities.invokeLater(() -> {
                vista.actualizarTablaEquipo(resultado);
                vista.actualizarStats(combinaciones, tiempoSeg, promedio);
            });
        }).start();
    }

    public void onHeuristicaPulsado() {
        new Thread(() -> {
            List<Empleado> resultado = gestor.generarEquipoPorHeuristica(
                    cantLideres, cantArquitectos, cantProgramadores, cantTesters);
            javax.swing.SwingUtilities.invokeLater(() -> {
                vista.actualizarTablaEquipo(resultado);
                vista.actualizarStats(combinaciones, tiempoSeg, promedio);
            });
        }).start();
    }

    public void onComparativaPulsado() {
        new Thread(() -> {
            HashMap<String, Object[]> mapa = gestor.generarComparativa(
                    cantLideres, cantArquitectos, cantProgramadores, cantTesters);
            javax.swing.SwingUtilities.invokeLater(() -> navegador.lanzarComparativa(mapa));
        }).start();
    }

    // ── Observer ───────────────────────────────────────────────────────────────

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

    // ── Helpers ────────────────────────────────────────────────────────────────

    private String formatarEmpleado(Empleado e) {
        return e.getNombre() + " " + e.getApellido() + " - Rol: " + e.getRol() + ", Puntaje: " + e.getPuntaje();
    }
}
