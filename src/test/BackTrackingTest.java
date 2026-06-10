package test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import model.BackTracking;
import model.Empleado;
import model.Empleado.Rol;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class BackTrackingTest {

	private BackTracking backTracking;
	private List<Empleado> empleados;

	@Before
	public void setUp() {
		empleados = new ArrayList<>();
		empleados.add(new Empleado("1", "Kylian", "Mbappe", 5, new HashSet<>(), Rol.Lider, "photo1.jpg"));
		empleados.add(new Empleado("2", "Kevin", "De Bruyne", 4, new HashSet<>(), Rol.Arquitecto, "photo2.jpg"));
		empleados.add(new Empleado("3", "Toni", "Kroos", 3, new HashSet<>(), Rol.Programador, "photo3.jpg"));
		empleados.add(new Empleado("4", "Harry", "Kane", 2, new HashSet<>(), Rol.Tester, "photo4.jpg"));
		empleados.add(new Empleado("5", "Giuliano", "Simeone", 2, new HashSet<>(Arrays.asList("0", "1", "2")), Rol.Tester,"photo4.jpg"));
		backTracking = new BackTracking(empleados, 1, 1, 1, 1);
	}

	@Test
	public void econtrarMejorCombinacionTest() {
		List<Empleado> bestCombination = backTracking.encontrarMejorCombinacion();
		assertNotNull(bestCombination);
		assertFalse(bestCombination.isEmpty());
		assertTrue(backTracking.esCombinacionValida(bestCombination));
	}

	@Test
	public void generarCombinacionTest() {
		List<Empleado> combinaciones = new ArrayList<>();
		backTracking.generarCombinacion(combinaciones, 0);
		Assert.assertEquals(54, backTracking.getCantidadCombinaciones(), 0);
	}

	@Test
	public void combinacionTieneConflictoConEmpleadosTest() {
		List<Empleado> combinaciones = new ArrayList<>();
		combinaciones.add(empleados.get(4));
		combinaciones.add(empleados.get(1));
		combinaciones.add(empleados.get(2));
		combinaciones.add(empleados.get(3));

		boolean contieneConflictos = backTracking.combinacionContieneConflicto(combinaciones, empleados.get(0));
		Assert.assertTrue(contieneConflictos);
	}

	@Test
	public void evaluarCombinacionValidaTest() {
		List<Empleado> combiValida = new ArrayList<>();
		combiValida.add(empleados.get(0));
		combiValida.add(empleados.get(1));
		combiValida.add(empleados.get(2));
		combiValida.add(empleados.get(3));

		Assert.assertTrue(backTracking.esCombinacionValida(combiValida));
	}

	@Test
	public void evaluarCombinacionInvalidaTest() {
		List<Empleado> combiInvalida = new ArrayList<>();
		combiInvalida.add(empleados.get(0));
		combiInvalida.add(empleados.get(1));
		combiInvalida.add(empleados.get(2));
		combiInvalida.add(empleados.get(3));
		combiInvalida.add(empleados.get(4));

		Assert.assertFalse(backTracking.esCombinacionValida(combiInvalida));
	}
}