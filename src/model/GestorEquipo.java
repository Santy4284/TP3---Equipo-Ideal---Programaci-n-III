package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import dal.IObservadorEquipo;

public class GestorEquipo {

    private static GestorEquipo _instancia;
    private List<Empleado> _empleados;
    private List<IObservadorEquipo> _observadores;

    public static synchronized GestorEquipo getInstancia() {
        if (_instancia == null)
            _instancia = new GestorEquipo();
        return _instancia;
    }

    private GestorEquipo() {
        _empleados = new ArrayList<>();
        _observadores = new ArrayList<>();
    }

    public List<Empleado> generarEquipoPorFuerzaBruta(int lideres, int arquitectos, int programadores, int testers) {
        FuerzaBruta fb = new FuerzaBruta(_empleados, lideres, arquitectos, programadores, testers);
        List<Empleado> resultado = fb.encontrarMejorCombinacion();
        notificarEquipoGenerado(resultado, fb.getCantidadCombinaciones(), fb.getTiempoEjecucion());
        return resultado;
    }

    public List<Empleado> generarEquipoPorRetroceso(int lideres, int arquitectos, int programadores, int testers) {
    	BackTracking rp = new BackTracking(_empleados, lideres, arquitectos, programadores, testers);
        List<Empleado> resultado = rp.encontrarMejorCombinacion();
        notificarEquipoGenerado(resultado, rp.getCantidadCombinaciones(), rp.getTiempoEjecucion());
        return resultado;
    }

    public List<Empleado> generarEquipoPorHeuristica(int lideres, int arquitectos, int programadores, int testers) {
        Heuristica h = new Heuristica(_empleados, lideres, arquitectos, programadores, testers, construirComparadorPorCoeficiente());
        List<Empleado> resultado = h.encontrarMejorCombinacion();
        notificarEquipoGenerado(resultado, h.getCantidadCombinaciones(), h.getTiempoEjecucion());
        return resultado;
    }

    public HashMap<String, Object[]> generarComparativa(int lideres, int arquitectos, int programadores, int testers) {
        HashMap<String, Object[]> mapa = new HashMap<>();
        Comparator<Empleado> comp = construirComparadorPorCoeficiente();

        FuerzaBruta fb = new FuerzaBruta(_empleados, lideres, arquitectos, programadores, testers);
        mapa.put("Fuerza Bruta", new Object[]{ fb.encontrarMejorCombinacion(), fb.getCantidadCombinaciones(), fb.getTiempoEjecucion(), fb.getMejorPuntajePromedio() });

        BackTracking rp = new BackTracking(_empleados, lideres, arquitectos, programadores, testers);
        mapa.put("Retroceso Progresivo", new Object[]{ rp.encontrarMejorCombinacion(), rp.getCantidadCombinaciones(), rp.getTiempoEjecucion(), rp.getMejorPuntajePromedio() });

        Heuristica h = new Heuristica(_empleados, lideres, arquitectos, programadores, testers, comp);
        mapa.put("Heuristica", new Object[]{ h.encontrarMejorCombinacion(), h.getCantidadCombinaciones(), h.getTiempoEjecucion(), h.getMejorPuntajePromedio() });

        notificarComparativaGenerada(mapa);
        return mapa;
    }

    public void agregarObservador(IObservadorEquipo obs) {
        _observadores.add(obs);
    }

    public void quitarObservador(IObservadorEquipo obs) {
        _observadores.remove(obs);
    }

    private void notificarEquipoGenerado(List<Empleado> equipo, double combinaciones, long tiempo) {
        _observadores.forEach(o -> o.onEquipoGenerado(equipo, combinaciones, tiempo));
    }

    private void notificarComparativaGenerada(HashMap<String, Object[]> mapa) {
        _observadores.forEach(o -> o.onComparativaGenerada(mapa));
    }

    public Empleado buscarPorLegajo(String legajo) {
        return _empleados.stream()
                .filter(e -> e.getLegajo().equals(legajo))
                .findFirst().orElse(null);
    }

    public List<Empleado> getEmpleados() {
        return new ArrayList<>(_empleados);
    }

    public void agregarEmpleado(Empleado empleado) {
        _empleados.add(empleado);
    }

    public void setEmpleados(List<Empleado> empleados) {
        this._empleados = (empleados != null) ? empleados : new ArrayList<>();
    }


    private Comparator<Empleado> construirComparadorPorCoeficiente() {
        return (e1, e2) -> Double.compare(calcularCoeficiente(e2), calcularCoeficiente(e1));
    }

    private double calcularCoeficiente(Empleado e) {
        return e.getPuntaje() - e.getConflictos().size();
    }
}
