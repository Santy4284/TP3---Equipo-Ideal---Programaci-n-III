package dal;

import model.Empleado;
import java.util.List;

public interface ICargaDatos {
    List<Empleado> cargarEmpleadosDesdeJSON();
}
