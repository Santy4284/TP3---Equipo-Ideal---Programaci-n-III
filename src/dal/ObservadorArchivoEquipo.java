package dal;

import model.Empleado;

import java.util.HashMap;
import java.util.List;

public class ObservadorArchivoEquipo implements IObservadorEquipo {

    private final GrabaDatos grabaDatos = new GrabaDatos();

    @Override
    public void onEquipoGenerado(List<Empleado> equipo, double combinaciones, long tiempo) {
        grabaDatos.guardarResultado(equipo, combinaciones, tiempo);
    }

    @Override
    public void onComparativaGenerada(HashMap<String, Object[]> mapa) {
        grabaDatos.guardarComparativa(mapa);
    }
}
