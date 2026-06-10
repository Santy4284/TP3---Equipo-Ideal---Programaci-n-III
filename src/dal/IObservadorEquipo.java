package dal;

import java.util.HashMap;
import java.util.List;

import model.Empleado;

public interface IObservadorEquipo {
    void onEquipoGenerado(List<Empleado> equipo, double combinaciones, long tiempo);
    void onComparativaGenerada(HashMap<String, Object[]> mapa);
}
