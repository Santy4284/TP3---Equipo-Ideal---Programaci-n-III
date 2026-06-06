package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class GestorEquipo {

    private static GestorEquipo instancia;
    private List<Empleado> empleados;
    private List<IObservadorEquipo> observadores;

    public static synchronized GestorEquipo getInstancia() {
        if (instancia == null) {
            instancia = new GestorEquipo();
        }
        return instancia;
    }

    private GestorEquipo() {
        empleados = new ArrayList<>();
        observadores = new ArrayList<>();
    }

    // ── Generación de equipos ──────────────────────────────────────────────────

    public List<Empleado> generarEquipoPorFuerzaBruta(int lideres, int arquitectos,
                                                       int programadores, int testers) {
        FuerzaBruta fb = new FuerzaBruta(empleados, lideres, arquitectos, programadores, testers);
        List<Empleado> resultado = fb.encontrarMejorCombinacion();
        notificarEquipoGenerado(resultado, fb.getCantidadCombinaciones(), fb.getTiempoEjecucion());
        return resultado;
    }

    public List<Empleado> generarEquipoPorRetroceso(int lideres, int arquitectos,
                                                     int programadores, int testers) {
    	BackTracking rp = new BackTracking(empleados, lideres, arquitectos, programadores, testers);
        List<Empleado> resultado = rp.encontrarMejorCombinacion();
        notificarEquipoGenerado(resultado, rp.getCantidadCombinaciones(), rp.getTiempoEjecucion());
        return resultado;
    }

    public List<Empleado> generarEquipoPorHeuristica(int lideres, int arquitectos,
                                                      int programadores, int testers) {
        Heuristica h = new Heuristica(empleados, lideres, arquitectos, programadores, testers,
                construirComparadorPorCoeficiente());
        List<Empleado> resultado = h.encontrarMejorCombinacion();
        notificarEquipoGenerado(resultado, h.getCantidadCombinaciones(), h.getTiempoEjecucion());
        return resultado;
    }

    public HashMap<String, Object[]> generarComparativa(int lideres, int arquitectos,
                                                         int programadores, int testers) {
        HashMap<String, Object[]> mapa = new HashMap<>();
        Comparator<Empleado> comp = construirComparadorPorCoeficiente();

        FuerzaBruta fb = new FuerzaBruta(empleados, lideres, arquitectos, programadores, testers);
        mapa.put("Fuerza Bruta", new Object[]{ fb.encontrarMejorCombinacion(),
                fb.getCantidadCombinaciones(), fb.getTiempoEjecucion(), fb.getMejorPuntajePromedio() });

        BackTracking rp = new BackTracking(empleados, lideres, arquitectos, programadores, testers);
        mapa.put("Retroceso Progresivo", new Object[]{ rp.encontrarMejorCombinacion(),
                rp.getCantidadCombinaciones(), rp.getTiempoEjecucion(), rp.getMejorPuntajePromedio() });

        Heuristica h = new Heuristica(empleados, lideres, arquitectos, programadores, testers, comp);
        mapa.put("Heuristica", new Object[]{ h.encontrarMejorCombinacion(),
                h.getCantidadCombinaciones(), h.getTiempoEjecucion(), h.getMejorPuntajePromedio() });

        notificarComparativaGenerada(mapa);
        return mapa;
    }

    // ── Observadores ──────────────────────────────────────────────────────────

    public void agregarObservador(IObservadorEquipo obs) {
        if (observadores == null) observadores = new ArrayList<>();
        observadores.add(obs);
    }

    public void quitarObservador(IObservadorEquipo obs) {
        if (observadores != null) observadores.remove(obs);
    }

    private void notificarEquipoGenerado(List<Empleado> equipo, double combinaciones, long tiempo) {
        if (observadores != null)
            observadores.forEach(o -> o.onEquipoGenerado(equipo, combinaciones, tiempo));
    }

    private void notificarComparativaGenerada(HashMap<String, Object[]> mapa) {
        if (observadores != null)
            observadores.forEach(o -> o.onComparativaGenerada(mapa));
    }

    // ── Acceso a datos ────────────────────────────────────────────────────────

    public Empleado buscarPorLegajo(String legajo) {
        return empleados.stream()
                .filter(e -> e.getLegajo().equals(legajo))
                .findFirst().orElse(null);
    }

    @SuppressWarnings("unchecked")
    public List<Empleado> getEmpleados() {
        if (empleados == null) return new ArrayList<>();
        return (List<Empleado>) ((ArrayList<Empleado>) empleados).clone();
    }

    public void agregarEmpleado(Empleado empleado) {
        if (empleados == null) empleados = new ArrayList<>();
        empleados.add(empleado);
    }

    public void setEmpleados(List<Empleado> empleados) {
        this.empleados = (empleados != null) ? empleados : new ArrayList<>();
    }

    // ── Auxiliares ────────────────────────────────────────────────────────────

    private Comparator<Empleado> construirComparadorPorCoeficiente() {
        return (e1, e2) -> Double.compare(calcularCoeficiente(e2), calcularCoeficiente(e1));
    }

    private double calcularCoeficiente(Empleado e) {
        return e.getPuntaje() - e.getConflictos().size();
    }
}
