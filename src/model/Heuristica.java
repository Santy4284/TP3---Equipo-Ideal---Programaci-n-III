package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Heuristica extends Algoritmo {

    private int lideresAgregados;
    private int arquitectosAgregados;
    private int programadoresAgregados;
    private int testersAgregados;
    private Comparator<Empleado> comparador;
    private List<Empleado> equipoFinal;

    public Heuristica(List<Empleado> empleados, int cantidadLideres, int cantidadArquitectos,
                      int cantidadProgramadores, int cantidadTesters, Comparator<Empleado> comparador) {
        super(empleados, cantidadLideres, cantidadArquitectos, cantidadProgramadores, cantidadTesters);
        this.lideresAgregados = 0;
        this.arquitectosAgregados = 0;
        this.programadoresAgregados = 0;
        this.testersAgregados = 0;
        this.comparador = comparador;
        this.equipoFinal = new ArrayList<>();
    }

    @Override
    public List<Empleado> encontrarMejorCombinacion() {
        long inicio = System.currentTimeMillis();
        List<Empleado> candidatos = new ArrayList<>(empleados);
        candidatos.sort(comparador);
        generarCombinacion(candidatos, 0);
        long fin = System.currentTimeMillis();
        tiempoEjecucion = fin - inicio;
        mejorPuntajePromedio = calcularPuntajePromedio(equipoFinal);
        return equipoFinal;
    }

    @Override
    protected void generarCombinacion(List<Empleado> candidatos, int indiceActual) {
        for (Empleado empleado : candidatos) {
            if (rolDisponible(empleado) && !hayConflictoConEquipo(equipoFinal, empleado)) {
                incrementarConteoRol(empleado.getRol());
                equipoFinal.add(empleado);
            }
        }
        cantidadCombinaciones++;
    }

    private void incrementarConteoRol(Empleado.Rol rol) {
        switch (rol) {
            case Lider:       lideresAgregados++;       break;
            case Arquitecto:  arquitectosAgregados++;   break;
            case Programador: programadoresAgregados++; break;
            case Tester:      testersAgregados++;       break;
        }
    }

    private boolean rolDisponible(Empleado empleado) {
        switch (empleado.getRol()) {
            case Lider:       return lideresAgregados < cantidadLideres;
            case Arquitecto:  return arquitectosAgregados < cantidadArquitectos;
            case Programador: return programadoresAgregados < cantidadProgramadores;
            case Tester:      return testersAgregados < cantidadTesters;
            default:          return false;
        }
    }

    public boolean hayConflictoConEquipo(List<Empleado> equipo, Empleado candidato) {
        for (Empleado e : equipo) {
            if (e.getConflictos().contains(candidato.getLegajo())) {
                return true;
            }
        }
        return false;
    }
}
