package dal;

import model.Empleado;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class GrabaDatos implements IGrabaDatos {

    @Override
    public void guardarResultado(List<Empleado> equipo, double combinaciones, long tiempo) {
        String archivo = "registro.txt";
        String encabezado = generarEncabezado(equipo);
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(archivo, true))) {
            escritor.write(encabezado + "\n");
            escritor.write("Equipo generado: " + contarPorRol(equipo) + "\n");
            escritor.write("Combinaciones evaluadas: " + combinaciones + "\n");
            escritor.write("Tiempo de proceso: " + tiempo + "ms\n");
            for (Empleado e : equipo) {
                escritor.write(e.toString() + "\n");
            }
            escritor.write("\n");
            escritor.write("-------------------------------------------------------------------\n");
            escritor.flush();
        } catch (IOException e) {
            System.out.println("Error al guardar el equipo en el archivo.");
            e.printStackTrace();
        }
    }

    @Override
    public void guardarComparativa(HashMap<String, Object[]> mapa) {
        String timestamp = generarTimestamp();
        String archivo = "comparativa_algoritmos_" + timestamp + ".txt";
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(archivo))) {
            for (Map.Entry<String, Object[]> entrada : mapa.entrySet()) {
                String algoritmo = entrada.getKey();
                Object[] valores = entrada.getValue();
                @SuppressWarnings("unchecked")
                List<Empleado> equipo    = (List<Empleado>) valores[0];
                double combinaciones     = (double) valores[1];
                long tiempo              = (long) valores[2];
                double puntajePromedio   = (double) valores[3];

                escritor.write("Algoritmo: " + algoritmo + "\n");
                escritor.write("Combinaciones generadas: " + combinaciones + "\n");
                escritor.write("Tiempo de proceso: " + tiempo + "ms\n");
                escritor.write("Puntaje promedio: " + puntajePromedio + "\n");
                escritor.write("Composicion del equipo: " + contarPorRol(equipo) + "\n");
                for (Empleado e : equipo) {
                    escritor.write(e.toString() + "\n");
                }
                escritor.write("\n");
                escritor.write("-------------------------------------------------------------\n");
            }
            escritor.flush();
        } catch (IOException e) {
            System.out.println("Error al guardar la comparativa de algoritmos.");
            e.printStackTrace();
        }
    }

    private String generarEncabezado(List<Empleado> equipo) {
        String timestamp = generarTimestamp();
        for (Empleado e : equipo) {
            if (e.getRol() == Empleado.Rol.Lider) {
                return "equipo_lider_" + e.getApellido() + "_" + timestamp;
            }
        }
        return "equipo_" + timestamp;
    }

    private String generarTimestamp() {
        return new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
    }

    private String contarPorRol(List<Empleado> equipo) {
        Map<Empleado.Rol, Integer> conteo = new HashMap<>();
        for (Empleado e : equipo) {
            conteo.put(e.getRol(), conteo.getOrDefault(e.getRol(), 0) + 1);
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Empleado.Rol, Integer> entrada : conteo.entrySet()) {
            sb.append(entrada.getValue()).append(" ").append(entrada.getKey().toString().toLowerCase()).append("s, ");
        }
        if (sb.length() > 0) sb.setLength(sb.length() - 2);
        return sb.toString();
    }
}
