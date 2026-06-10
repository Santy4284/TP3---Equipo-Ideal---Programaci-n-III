package model;

import java.util.ArrayList;
import java.util.List;

public abstract class Algoritmo {

    protected List<Empleado> _empleados;
    protected int _cantidadLideres;
    protected int _cantidadArquitectos;
    protected int _cantidadProgramadores;
    protected int _cantidadTesters;
    protected List<Empleado> _mejorCombinacion;
    protected double _mejorPuntajePromedio;
    protected double _cantidadCombinaciones;
    protected long _tiempoEjecucion;

    public Algoritmo(List<Empleado> empleados, int cantidadLideres, int cantidadArquitectos, int cantidadProgramadores, int cantidadTesters) {
        _empleados = empleados;
        _cantidadLideres = cantidadLideres;
        _cantidadArquitectos = cantidadArquitectos;
        _cantidadProgramadores = cantidadProgramadores;
        _cantidadTesters = cantidadTesters;
        _mejorCombinacion = new ArrayList<>();
        _mejorPuntajePromedio = 0;
        _cantidadCombinaciones = 0;
        _tiempoEjecucion = 0;
    }
    
    public List<Empleado> encontrarMejorCombinacion() {
        long inicio = System.currentTimeMillis();
        List<Empleado> combinacion = new ArrayList<>();
        generarCombinacion(combinacion, 0);
        long fin = System.currentTimeMillis();
        _tiempoEjecucion = fin - inicio;
        return _mejorCombinacion;
    }

    protected abstract void generarCombinacion(List<Empleado> combinacion, int indiceActual);

    public void evaluarCombinacion(List<Empleado> combinacion) {
        if (esCombinacionValida(combinacion)) {
            double promedio = calcularPuntajePromedio(combinacion);
            if (promedio > _mejorPuntajePromedio && !tieneEmpleadosEnConflicto(combinacion)) {
                _mejorCombinacion = new ArrayList<>(combinacion);
                _mejorPuntajePromedio = promedio;
            }
        }
    }

    public boolean tieneEmpleadosEnConflicto(List<Empleado> combinacion) {
        for (Empleado empleado : combinacion)
            if (combinacionContieneConflicto(combinacion, empleado))
                return true;
        return false;
    }

    public boolean combinacionContieneConflicto(List<Empleado> combinacion, Empleado empleado) {
        for (Empleado e : combinacion)
            if (e.getConflictos().contains(empleado.getLegajo()))
                return true;
        return false;
    }

    public boolean esCombinacionValida(List<Empleado> combinacion) {
        int lideres = 0, arquitectos = 0, programadores = 0, testers = 0;
        for (Empleado e : combinacion) {
            switch (e.getRol()) {
                case Lider:
                	lideres++;
                	break;
                case Arquitecto:
                	arquitectos++;
                	break;
                case Programador:
                	programadores++;
                	break;
                case Tester:
                	testers++;
                	break;
            }
        }
        return lideres == _cantidadLideres
                && arquitectos == _cantidadArquitectos
                && programadores == _cantidadProgramadores
                && testers == _cantidadTesters;
    }

    protected double calcularPuntajePromedio(List<Empleado> combinacion) {
        int total = 0;
        for (Empleado e : combinacion)
            total += e.getPuntaje();
        return (double) total / combinacion.size();
    }

    public double getMejorPuntajePromedio() { 
    	return _mejorPuntajePromedio; 
    }
    
    public double getCantidadCombinaciones() { 
    	return _cantidadCombinaciones; 
    }
    
    public long getTiempoEjecucion() {
    	return _tiempoEjecucion; 
    }
    
    public List<Empleado> getMejorCombinacion() {
    	return _mejorCombinacion;
    }
}
