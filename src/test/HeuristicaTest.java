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
	private List<Empleado> empleado;
	private Comparator<Empleado> customComparator;

	@Before
	public void setUp() {
		empleado = new ArrayList<>();
		empleado.add(new Empleado("1", "Linus", "Torvalds", 5, new HashSet<>(), Rol.Lider, "photo1.jpg"));
		empleado.add(new Empleado("2", "Elon", "Musk", 4, new HashSet<>(), Rol.Arquitecto, "photo2.jpg"));
		empleado.add(new Empleado("3", "Raul", "Capablanca", 3, new HashSet<>(), Rol.Programador, "photo3.jpg"));
		empleado.add(new Empleado("4", "Magnus", "Carlsen", 2, new HashSet<>(), Rol.Tester, "photo4.jpg"));
		empleado.add(new Empleado("5", "Judith", "Polgar", 2, new HashSet<>(Arrays.asList("1", "2", "3")), Rol.Tester, "photo4.jpg"));

		customComparator = (e1, e2) -> {
			double coefficient1 = e1.getPuntaje() - e1.getConflictos().size();
			double coefficient2 = e2.getPuntaje() - e2.getConflictos().size();
			return Double.compare(coefficient2, coefficient1);
		};
		heuristica = new Heuristica(empleado, 1, 1, 1, 1, customComparator);
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
		combination.add(empleado.get(4));
		combination.add(empleado.get(1));
		combination.add(empleado.get(2));
		combination.add(empleado.get(3));

		boolean containsConflicted = heuristica.combinacionContieneConflicto(combination, empleado.get(0));
		Assert.assertTrue(containsConflicted);
	}
}