package test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import model.Empleado;
import model.Empleado.Rol;
import model.GestorEquipo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GestorEquipoTest {
    private GestorEquipo gestorEquipo;
    private int cantidadLideres;
    private int cantidadArquitectos;
    private int cantidadProgramadores;
    private int cantidadTesters;

    @Before
    public void setUp() {
        gestorEquipo = GestorEquipo.getInstancia();
        List<Empleado> empleados = new ArrayList<>();
        Set<String> conflictos = new HashSet<>();
        conflictos.add("0000");
     
        empleados.add(new Empleado("1111", "A", "A", 3, conflictos, Rol.Lider, null));
        empleados.add(new Empleado("2222", "B", "B", 3, conflictos, Rol.Arquitecto, null));
        empleados.add(new Empleado("3333", "C", "C", 3, conflictos, Rol.Tester, null));
        empleados.add(new Empleado("4444", "D", "D", 3, conflictos, Rol.Tester, null));
        empleados.add(new Empleado("5555", "E", "E", 3, conflictos, Rol.Programador, null));
        empleados.add(new Empleado("6666", "F", "F", 3, conflictos, Rol.Programador, null));
        empleados.add(new Empleado("7777", "G", "G", 3, conflictos, Rol.Tester, null));
        empleados.add(new Empleado("8888", "H", "H", 3, conflictos, Rol.Programador, null));
        
        gestorEquipo.setEmpleados(empleados);
        cantidadLideres = 1;
        cantidadArquitectos = 1;
        cantidadProgramadores = 1;
        cantidadTesters = 1;
    }
    
 // Verifica que la comparativa contenga los tres algoritmos
    @Test
    public void generarComparativaTest() {
        HashMap<String, Object[]> comparativa = gestorEquipo.generarComparativa(cantidadLideres, cantidadArquitectos, cantidadProgramadores, cantidadTesters);
        Assert.assertNotNull(comparativa);
        Assert.assertEquals(3, comparativa.size());
        Assert.assertTrue(comparativa.containsKey("Fuerza Bruta"));
        Assert.assertTrue(comparativa.containsKey("Retroceso Progresivo"));
        Assert.assertTrue(comparativa.containsKey("Heuristica"));
    }

    // Verifica que Fuerza Bruta genere un equipo con la cantidad correcta de roles
    @Test
    public void generarEquipoPorFuerzaBrutaTest() {
        List<Empleado> equipo = gestorEquipo.generarEquipoPorFuerzaBruta(cantidadLideres, cantidadArquitectos, cantidadProgramadores, cantidadTesters);
        Assert.assertNotNull(equipo);
        Assert.assertEquals(cantidadLideres, contarPorRol(equipo, Rol.Lider));
        Assert.assertEquals(cantidadArquitectos, contarPorRol(equipo, Rol.Arquitecto));
        Assert.assertEquals(cantidadProgramadores, contarPorRol(equipo, Rol.Programador));
        Assert.assertEquals(cantidadTesters, contarPorRol(equipo, Rol.Tester));
    }

    // Verifica que BackTracking genere un equipo con la cantidad correcta de roles
    @Test
    public void generarEquipoPorBackTrackingTest() {
        List<Empleado> equipo = gestorEquipo.generarEquipoPorRetroceso(cantidadLideres, cantidadArquitectos, cantidadProgramadores, cantidadTesters);
        Assert.assertNotNull(equipo);
        Assert.assertEquals(cantidadLideres, contarPorRol(equipo, Rol.Lider));
        Assert.assertEquals(cantidadArquitectos, contarPorRol(equipo, Rol.Arquitecto));
        Assert.assertEquals(cantidadProgramadores, contarPorRol(equipo, Rol.Programador));
        Assert.assertEquals(cantidadTesters, contarPorRol(equipo, Rol.Tester));
    }

    // Verifica que Heuristica genere un equipo con la cantidad correcta de roles
    @Test
    public void generarEquipoPorHeuristicaTest() {
        List<Empleado> equipo = gestorEquipo.generarEquipoPorHeuristica(cantidadLideres, cantidadArquitectos, cantidadProgramadores, cantidadTesters);
        Assert.assertNotNull(equipo);
        Assert.assertEquals(cantidadLideres, contarPorRol(equipo, Rol.Lider));
        Assert.assertEquals(cantidadArquitectos, contarPorRol(equipo, Rol.Arquitecto));
        Assert.assertEquals(cantidadProgramadores, contarPorRol(equipo, Rol.Programador));
        Assert.assertEquals(cantidadTesters, contarPorRol(equipo, Rol.Tester));
    }

    // Verifica que buscarPorLegajo retorne null cuando no hay empleados cargados
    @Test
    public void buscarPorLegajoListaVaciaTest() {
        gestorEquipo.setEmpleados(new ArrayList<>());
        Empleado encontrado = gestorEquipo.buscarPorLegajo("1111");
        Assert.assertNull(encontrado);
    }
    
    // Verifica la busqueda de empleados por legajo
    @Test
    public void buscarPorLegajoTest() {
        Empleado encontrado = gestorEquipo.buscarPorLegajo("1111");
        Assert.assertNotNull(encontrado);
        Assert.assertEquals("A", encontrado.getNombre());
        Assert.assertEquals(Rol.Lider, encontrado.getRol());

        Empleado noEncontrado = gestorEquipo.buscarPorLegajo("11112222");
        Assert.assertNull(noEncontrado);
    }
    
    // Verifica que se obtiene correctamente la lista de empleados
    @Test
    public void getEmpleadosTest() {
        List<Empleado> empleados = gestorEquipo.getEmpleados();
        Assert.assertNotNull(empleados);
        Assert.assertEquals(8, empleados.size());
        Assert.assertEquals("A", empleados.get(0).getNombre());
        Assert.assertEquals("B", empleados.get(1).getNombre());
    }

    // Verifica que un empleado se agregue correctamente al gestor
    @Test
    public void agregarEmpleadoTest() {
        Empleado nuevoEmpleado = new Empleado("9999", "I", "I", 3, new HashSet<>(), Rol.Programador, null);
        gestorEquipo.agregarEmpleado(nuevoEmpleado);
        Assert.assertTrue(gestorEquipo.getEmpleados().contains(nuevoEmpleado));
    }
    
 // Verifica que setEmpleados reemplace correctamente la lista actual
    @Test
    public void setEmpleadosTest() {
        List<Empleado> nuevaLista = new ArrayList<>();
        nuevaLista.add(new Empleado("9999",  "Nuevo", "Empleado", 5, new HashSet<>(), Rol.Lider, null));
        gestorEquipo.setEmpleados(nuevaLista);
        Assert.assertEquals(1, gestorEquipo.getEmpleados().size());
        Assert.assertEquals("9999", gestorEquipo.getEmpleados().get(0).getLegajo());
    }

    private int contarPorRol(List<Empleado> empleados, Rol rol) {
        int cantidad = 0;
        for (Empleado empleado : empleados)
            if (empleado.getRol() == rol)
                cantidad++;
        return cantidad;
    }
}