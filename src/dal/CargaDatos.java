package dal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import model.Empleado;
import model.Empleado.Rol;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class CargaDatos implements ICargaDatos {

    // Rutas relativas al classpath (dentro de src/dal/)
    private final String archivoEmpleados  = "/dal/ListaEmpleados.json";
    private final String archivoConflictos = "/dal/ListaConflictos.json";

    @Override
    public List<Empleado> cargarEmpleadosDesdeJSON() {
        List<Empleado> empleados = leerListaEmpleados(archivoEmpleados);
        if (empleados == null) return new ArrayList<>();
        cargarConflictos(empleados, archivoConflictos);
        return empleados;
    }

    private List<Empleado> leerListaEmpleados(String ruta) {
        try {
            String contenido = leerRecurso(ruta);
            JSONArray array = new JSONArray(contenido);
            List<Empleado> empleados = new ArrayList<>();

            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                String legajo   = obj.getString("dni");
                String nombre   = obj.getString("Nombre");
                String apellido = obj.getString("Apellido");
                int puntaje     = obj.getInt("Puntaje");
                Rol rol         = Rol.valueOf(obj.getString("Rol"));
                String foto     = obj.optString("foto", "");
                empleados.add(new Empleado(legajo, nombre, apellido, puntaje, new HashSet<>(), rol, foto));
            }
            return empleados;

        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void cargarConflictos(List<Empleado> empleados, String ruta) {
        try {
            String contenido = leerRecurso(ruta);
            JSONArray array = new JSONArray(contenido);

            Map<String, Empleado> mapaEmpleados = new HashMap<>();
            for (Empleado e : empleados) {
                mapaEmpleados.put(e.getLegajo(), e);
            }

            for (int i = 0; i < array.length(); i++) {
                JSONObject entrada = array.getJSONObject(i);
                String legajo = entrada.keys().next();
                Empleado empleado = mapaEmpleados.get(legajo);
                if (empleado == null) continue;

                JSONArray conflictosArray = entrada.getJSONArray(legajo);
                for (int j = 0; j < conflictosArray.length(); j++) {
                    empleado.getConflictos().add(conflictosArray.getString(j));
                }
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private String leerRecurso(String ruta) throws IOException {
        InputStream is = getClass().getResourceAsStream(ruta);
        if (is == null) throw new IOException("No se encontró el recurso: " + ruta);
        return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    }
}
