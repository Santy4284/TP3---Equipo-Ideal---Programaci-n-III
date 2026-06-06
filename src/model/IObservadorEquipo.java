package model;

import java.util.HashMap;
import java.util.List;

public interface IObservadorEquipo {
    void onEquipoGenerado(List<Empleado> equipo, double combinaciones, long tiempo);
    void onComparativaGenerada(HashMap<String, Object[]> mapa);
}
