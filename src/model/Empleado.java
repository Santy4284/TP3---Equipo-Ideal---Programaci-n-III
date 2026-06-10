package model;

import java.util.Objects;
import java.util.Set;

public class Empleado {
    private String _legajo;
    private String _nombre;
    private String _apellido;
    private int _puntaje;
    private Set<String> _conflictos;
    private Rol _rol;
    private String _foto;

    public Empleado(String legajo, String nombre, String apellido, int puntaje, Set<String> conflictos, Rol rol, String foto) {
        _legajo = legajo;
        _nombre = nombre;
        _apellido = apellido;
        _puntaje = puntaje;
        _conflictos = conflictos;
        _rol = rol;
        _foto = foto;
    }

    public void agregarConflicto(String legajo) {
        _conflictos.add(legajo);
    }

    public String getFoto() {
        return _foto;
    }

    public Set<String> getConflictos() {
        return _conflictos;
    }

    public String getLegajo() {
        return _legajo;
    }

    public String getNombre() {
        return _nombre;
    }

    public String getApellido() {
        return _apellido;
    }

    public int getPuntaje() {
        return _puntaje;
    }

    public Rol getRol() {
        return _rol;
    }

    public enum Rol {
        Lider, Arquitecto, Programador, Tester
    }

    @Override
    public String toString() {
        return "Empleado{legajo='" + _legajo + "', nombre='" + _nombre + "', apellido='" + _apellido
                + "', puntaje=" + _puntaje + ", rol=" + _rol + '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(_legajo);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Empleado otro = (Empleado) obj;
        return Objects.equals(_legajo, otro._legajo);
    }
}
