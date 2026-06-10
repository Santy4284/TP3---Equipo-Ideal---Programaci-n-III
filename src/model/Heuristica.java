package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Heuristica extends Algoritmo {
	
    private int _lideresAgregados;
    private int _arquitectosAgregados;
    private int _programadoresAgregados;
    private int _testersAgregados;
    private Comparator<Empleado> _comparador;
    private List<Empleado> _equipoFinal;

    public Heuristica(List<Empleado> empleados, int cantidadLideres, int cantidadArquitectos, int cantidadProgramadores, int cantidadTesters, Comparator<Empleado> comparador) {
        super(empleados, cantidadLideres, cantidadArquitectos, cantidadProgramadores, cantidadTesters);
        this._lideresAgregados = 0;
        this._arquitectosAgregados = 0;
        this._programadoresAgregados = 0;
        this._testersAgregados = 0;
        this._comparador = comparador;
        this._equipoFinal = new ArrayList<>();
    }

    @Override
    public List<Empleado> encontrarMejorCombinacion() {
        long inicio = System.currentTimeMillis();
        List<Empleado> candidatos = new ArrayList<>(_empleados);
        candidatos.sort(_comparador);
        generarCombinacion(candidatos, 0);
        long fin = System.currentTimeMillis();
        _tiempoEjecucion = fin - inicio;
        _mejorPuntajePromedio = calcularPuntajePromedio(_equipoFinal);
        return _equipoFinal;
    }

    @Override
    protected void generarCombinacion(List<Empleado> candidatos, int indiceActual) {
        for (Empleado empleado : candidatos)
            if (rolDisponible(empleado) && !hayConflictoConEquipo(_equipoFinal, empleado)) {
                incrementarConteoRol(empleado.getRol());
                _equipoFinal.add(empleado);
            }
        _cantidadCombinaciones++;
    }

    private void incrementarConteoRol(Empleado.Rol rol) {
        switch (rol) {
            case Lider:
            	_lideresAgregados++;
            	break;
            case Arquitecto:
            	_arquitectosAgregados++;
            	break;
            case Programador:
            	_programadoresAgregados++;
            	break;
            case Tester:
            	_testersAgregados++;
            	break;
        }
    }

    private boolean rolDisponible(Empleado empleado) {
        switch (empleado.getRol()) {
            case Lider:
            	return _lideresAgregados < _cantidadLideres;
            case Arquitecto:
            	return _arquitectosAgregados < _cantidadArquitectos;
            case Programador:
            	return _programadoresAgregados < _cantidadProgramadores;
            case Tester:
            	return _testersAgregados < _cantidadTesters;
            default:
            	return false;
        }
    }

    public boolean hayConflictoConEquipo(List<Empleado> equipo, Empleado candidato) {
        for (Empleado e : equipo)
            if (e.getConflictos().contains(candidato.getLegajo()))
                return true;
        return false;
    }
}
