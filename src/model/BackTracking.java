package model;

import java.util.ArrayList;
import java.util.List;

public class BackTracking extends Algoritmo {

    public BackTracking(List<Empleado> empleados, int cantidadLideres, int cantidadArquitectos,
                               int cantidadProgramadores, int cantidadTesters) {
        super(empleados, cantidadLideres, cantidadArquitectos, cantidadProgramadores, cantidadTesters);
    }

    @Override
    protected void generarCombinacion(List<Empleado> combinacion, int indiceActual) {
        if (indiceActual == empleados.size()) {
            if (esCombinacionValida(combinacion)) {
                double promedio = calcularPuntajePromedio(combinacion);
                if (promedio > mejorPuntajePromedio) {
                    mejorCombinacion = new ArrayList<>(combinacion);
                    mejorPuntajePromedio = promedio;
                }
            }
            return;
        }

        Empleado actual = empleados.get(indiceActual);
        if (!combinacionContieneConflicto(combinacion, actual)
                && !rolSuperaLimite(combinacion, actual.getRol())) {
            combinacion.add(actual);
            cantidadCombinaciones++;
            generarCombinacion(combinacion, indiceActual + 1);
            combinacion.remove(combinacion.size() - 1);
        }
        cantidadCombinaciones++;
        generarCombinacion(combinacion, indiceActual + 1);
    }

    private boolean rolSuperaLimite(List<Empleado> combinacion, Empleado.Rol rol) {
        int conteo = 0;
        for (Empleado e : combinacion) {
            if (e.getRol() == rol) conteo++;
        }
        switch (rol) {
            case Lider:       return conteo >= cantidadLideres;
            case Arquitecto:  return conteo >= cantidadArquitectos;
            case Programador: return conteo >= cantidadProgramadores;
            case Tester:      return conteo >= cantidadTesters;
            default:          return false;
        }
    }
}
