package dal;

import model.Empleado;
import java.util.HashMap;
import java.util.List;

public interface IGrabaDatos {
    void guardarResultado(List<Empleado> equipo, double combinaciones, long tiempo);
    void guardarComparativa(HashMap<String, Object[]> mapa);
}
