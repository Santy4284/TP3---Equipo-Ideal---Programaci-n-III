package model;

import java.util.ArrayList;
import java.util.List;

public class BackTracking extends Algoritmo {

    public BackTracking(List<Empleado> empleados, int cantidadLideres, int cantidadArquitectos, int cantidadProgramadores, int cantidadTesters) {
        super(empleados, cantidadLideres, cantidadArquitectos, cantidadProgramadores, cantidadTesters);
    }

    @Override
    public void generarCombinacion(List<Empleado> combinacion, int indiceActual) {
        if (indiceActual == _empleados.size()) {
            if (esCombinacionValida(combinacion)) {
                double promedio = calcularPuntajePromedio(combinacion);
                if (promedio > _mejorPuntajePromedio) {
                    _mejorCombinacion = new ArrayList<>(combinacion);
                    _mejorPuntajePromedio = promedio;
                }
            }
            return;
        }

        Empleado actual = _empleados.get(indiceActual);
        if (!combinacionContieneConflicto(combinacion, actual) && !rolSuperaLimite(combinacion, actual.getRol())) {
            combinacion.add(actual);
            _cantidadCombinaciones++;
            generarCombinacion(combinacion, indiceActual + 1);
            combinacion.remove(combinacion.size() - 1);
        }
        _cantidadCombinaciones++;
        generarCombinacion(combinacion, indiceActual + 1);
    }

    private boolean rolSuperaLimite(List<Empleado> combinacion, Empleado.Rol rol) {
        int conteo = 0;
        for (Empleado e : combinacion)
            if (e.getRol() == rol) conteo++;

        switch (rol) {
            case Lider:
            	return conteo >= _cantidadLideres;
            case Arquitecto:
            	return conteo >= _cantidadArquitectos;
            case Programador:
            	return conteo >= _cantidadProgramadores;
            case Tester:
            	return conteo >= _cantidadTesters;
            default:
            	return false;
        }
    }
}
