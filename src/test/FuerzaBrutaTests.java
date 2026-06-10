package test;

import model.Empleado;
import model.Empleado.Rol;
import model.FuerzaBruta;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class FuerzaBrutaTests{

	private FuerzaBruta fuerzaBruta;
	private List<Empleado> empleados;

	@Before
	public void setUp() {
		empleados = new ArrayList<>();
		empleados.add(new Empleado("1", "Kylian", "Mbappe", 5, new HashSet<>(), Rol.Lider, "photo1.jpg"));
		empleados.add(new Empleado("2", "Kevin", "De Bruyne", 4, new HashSet<>(), Rol.Arquitecto, "photo2.jpg"));
		empleados.add(new Empleado("3", "Toni", "Kroos", 3, new HashSet<>(), Rol.Programador, "photo3.jpg"));
		empleados.add(new Empleado("4", "Harry", "Kane", 2, new HashSet<>(), Rol.Tester, "photo4.jpg"));
		empleados.add(new Empleado("5", "Giuliano", "Simeone", 2, new HashSet<>(Arrays.asList("1", "2", "3")), Rol.Tester,"photo4.jpg"));
		fuerzaBruta = new FuerzaBruta(empleados, 1, 1, 1, 1);
	}

	@Test
	public void encontrarMejorConvinacionTest() {
		List<Empleado> mejorConbinacion = fuerzaBruta.encontrarMejorCombinacion();
		assertNotNull(mejorConbinacion);
		assertFalse(mejorConbinacion.isEmpty());
		assertTrue(fuerzaBruta.esCombinacionValida(mejorConbinacion));
	}

	@Test
	public void generarConbinacionTest() {
		List<Empleado> combinaciones = new ArrayList<>();
		fuerzaBruta.generarCombinacion(combinaciones, 0);
		Assert.assertEquals(32, fuerzaBruta.getCantidadCombinaciones(), 0);
	}

	@Test
	public void evaluarConbinacionValidaTest() {
		List<Empleado> conbiValida = new ArrayList<>();
		conbiValida.add(empleados.get(0));
		conbiValida.add(empleados.get(1));
		conbiValida.add(empleados.get(2));
		conbiValida.add(empleados.get(3));

		fuerzaBruta.evaluarCombinacion(conbiValida);
		boolean mismoTamaño = fuerzaBruta.getMejorCombinacion().size() == conbiValida.size();
		Assert.assertTrue(mismoTamaño);
	}

	@Test
	public void evaluarConbinacionInvalidaTest() {
		List<Empleado> conbiInvalida = new ArrayList<>();
		conbiInvalida.add(empleados.get(1));
		conbiInvalida.add(empleados.get(2));
		conbiInvalida.add(empleados.get(3));
		conbiInvalida.add(empleados.get(4));

		fuerzaBruta.evaluarCombinacion(conbiInvalida);
		boolean mejorConbinacionVacia = fuerzaBruta.getMejorCombinacion().isEmpty();
		Assert.assertTrue(mejorConbinacionVacia);
	}

	@Test
	public void tieneEmpleadosEnConflitosTest() {
		List<Empleado> conConflictos = new ArrayList<>();
		conConflictos.add(empleados.get(0));
		conConflictos.add(empleados.get(1));
		conConflictos.add(empleados.get(2));
		conConflictos.add(empleados.get(4));

		List<Empleado> sinConflictos = new ArrayList<>();
		sinConflictos.add(empleados.get(0));
		sinConflictos.add(empleados.get(1));
		sinConflictos.add(empleados.get(2));
		sinConflictos.add(empleados.get(3));

		boolean conflictos1 = fuerzaBruta.tieneEmpleadosEnConflicto(conConflictos);
		boolean conflictos2 = fuerzaBruta.tieneEmpleadosEnConflicto(sinConflictos);
		assertTrue(conflictos1);
		assertFalse(conflictos2);
	}
}
