package test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import model.Empleado;
import model.Empleado.Rol;
import model.Heuristica;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

public class HeuristicaTest {
	private Heuristica heuristica;
	private List<Empleado> empleados;
	private Comparator<Empleado> customComparator;

	@Before
	public void setUp() {
		empleados = new ArrayList<>();
		empleados.add(new Empleado("1", "Kylian", "Mbappe", 5, new HashSet<>(), Rol.Lider, "photo1.jpg"));
		empleados.add(new Empleado("2", "Kevin", "De Bruyne", 4, new HashSet<>(), Rol.Arquitecto, "photo2.jpg"));
		empleados.add(new Empleado("3", "Toni", "Kroos", 3, new HashSet<>(), Rol.Programador, "photo3.jpg"));
		empleados.add(new Empleado("4", "Harry", "Kane", 2, new HashSet<>(), Rol.Tester, "photo4.jpg"));
		empleados.add(new Empleado("5", "Giuliano", "Simeone", 2, new HashSet<>(Arrays.asList("1", "2", "3")), Rol.Tester, "photo4.jpg"));

		customComparator = (e1, e2) -> {
			double coefficient1 = e1.getPuntaje() - e1.getConflictos().size();
			double coefficient2 = e2.getPuntaje() - e2.getConflictos().size();
			return Double.compare(coefficient2, coefficient1);
		};
		heuristica = new Heuristica(empleados, 1, 1, 1, 1, customComparator);
	}

	@Test
	public void testFindBestCombination() {
		List<Empleado> mejorCombinacion = heuristica.encontrarMejorCombinacion();
		Assert.assertNotNull(mejorCombinacion);
		Assert.assertFalse(mejorCombinacion.isEmpty());
	}

	@Test
	public void testCombinationContainsConflictedEmployee() {
		List<Empleado> combination = new ArrayList<>();
		combination.add(empleados.get(4));
		combination.add(empleados.get(1));
		combination.add(empleados.get(2));
		combination.add(empleados.get(3));

		boolean containsConflicted = heuristica.combinacionContieneConflicto(combination, empleados.get(0));
		Assert.assertTrue(containsConflicted);
	}
}