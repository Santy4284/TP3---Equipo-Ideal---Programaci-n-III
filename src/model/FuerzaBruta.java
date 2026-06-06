package model;

import java.util.List;

public class FuerzaBruta extends Algoritmo {

    public FuerzaBruta(List<Empleado> empleados, int cantidadLideres, int cantidadArquitectos,
                       int cantidadProgramadores, int cantidadTesters) {
        super(empleados, cantidadLideres, cantidadArquitectos, cantidadProgramadores, cantidadTesters);
    }

    @Override
    protected void generarCombinacion(List<Empleado> combinacion, int indiceActual) {
        if (indiceActual == empleados.size()) {
            cantidadCombinaciones++;
            evaluarCombinacion(combinacion);
            return;
        }

        Empleado actual = empleados.get(indiceActual);
        combinacion.add(actual);
        generarCombinacion(combinacion, indiceActual + 1);
        combinacion.remove(combinacion.size() - 1);
        generarCombinacion(combinacion, indiceActual + 1);
    }
}
