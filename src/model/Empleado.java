package model;

import java.util.Objects;
import java.util.Set;

public class Empleado {
    private String legajo;
    private String nombre;
    private String apellido;
    private int puntaje;
    private Set<String> conflictos;
    private Rol rol;
    private String foto;

    public Empleado(String legajo, String nombre, String apellido, int puntaje, Set<String> conflictos, Rol rol, String foto) {
        this.legajo = legajo;
        this.nombre = nombre;
        this.apellido = apellido;
        this.puntaje = puntaje;
        this.conflictos = conflictos;
        this.rol = rol;
        this.foto = foto;
    }

    public void agregarConflicto(String legajo) {
        conflictos.add(legajo);
    }

    public String getFoto() {
        return foto;
    }

    public Set<String> getConflictos() {
        return conflictos;
    }

    public String getLegajo() {
        return legajo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public Rol getRol() {
        return rol;
    }

    public enum Rol {
        Lider, Arquitecto, Programador, Tester
    }

    @Override
    public String toString() {
        return "Empleado{legajo='" + legajo + "', nombre='" + nombre + "', apellido='" + apellido
                + "', puntaje=" + puntaje + ", rol=" + rol + '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(legajo);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Empleado otro = (Empleado) obj;
        return Objects.equals(legajo, otro.legajo);
    }
}
