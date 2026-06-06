package model;

import java.util.ArrayList;
import java.util.List;

public abstract class Algoritmo {

    protected List<Empleado> empleados;
    protected int cantidadLideres;
    protected int cantidadArquitectos;
    protected int cantidadProgramadores;
    protected int cantidadTesters;
    protected List<Empleado> mejorCombinacion;
    protected double mejorPuntajePromedio;
    protected double cantidadCombinaciones;
    protected long tiempoEjecucion;

    public Algoritmo(List<Empleado> empleados, int cantidadLideres, int cantidadArquitectos,
                     int cantidadProgramadores, int cantidadTesters) {
        this.empleados = empleados;
        this.cantidadLideres = cantidadLideres;
        this.cantidadArquitectos = cantidadArquitectos;
        this.cantidadProgramadores = cantidadProgramadores;
        this.cantidadTesters = cantidadTesters;
        this.mejorCombinacion = new ArrayList<>();
        this.mejorPuntajePromedio = 0;
        this.cantidadCombinaciones = 0;
        this.tiempoEjecucion = 0;
    }

    public List<Empleado> encontrarMejorCombinacion() {
        long inicio = System.currentTimeMillis();
        List<Empleado> combinacion = new ArrayList<>();
        generarCombinacion(combinacion, 0);
        long fin = System.currentTimeMillis();
        tiempoEjecucion = fin - inicio;
        return mejorCombinacion;
    }

    protected abstract void generarCombinacion(List<Empleado> combinacion, int indiceActual);

    public void evaluarCombinacion(List<Empleado> combinacion) {
        if (esCombinacionValida(combinacion)) {
            double promedio = calcularPuntajePromedio(combinacion);
            if (promedio > mejorPuntajePromedio && !tieneEmpleadosEnConflicto(combinacion)) {
                mejorCombinacion = new ArrayList<>(combinacion);
                mejorPuntajePromedio = promedio;
            }
        }
    }

    public boolean tieneEmpleadosEnConflicto(List<Empleado> combinacion) {
        for (Empleado empleado : combinacion) {
            if (combinacionContieneConflicto(combinacion, empleado)) {
                return true;
            }
        }
        return false;
    }

    public boolean combinacionContieneConflicto(List<Empleado> combinacion, Empleado empleado) {
        for (Empleado e : combinacion) {
            if (e.getConflictos().contains(empleado.getLegajo())) {
                return true;
            }
        }
        return false;
    }

    public boolean esCombinacionValida(List<Empleado> combinacion) {
        int lideres = 0, arquitectos = 0, programadores = 0, testers = 0;
        for (Empleado e : combinacion) {
            switch (e.getRol()) {
                case Lider:       lideres++;       break;
                case Arquitecto:  arquitectos++;   break;
                case Programador: programadores++; break;
                case Tester:      testers++;       break;
            }
        }
        return lideres == cantidadLideres
                && arquitectos == cantidadArquitectos
                && programadores == cantidadProgramadores
                && testers == cantidadTesters;
    }

    protected double calcularPuntajePromedio(List<Empleado> combinacion) {
        int total = 0;
        for (Empleado e : combinacion) {
            total += e.getPuntaje();
        }
        return (double) total / combinacion.size();
    }

    public double getMejorPuntajePromedio() { return mejorPuntajePromedio; }
    public double getCantidadCombinaciones() { return cantidadCombinaciones; }
    public long getTiempoEjecucion() { return tiempoEjecucion; }
    public List<Empleado> getMejorCombinacion() { return mejorCombinacion; }
}
