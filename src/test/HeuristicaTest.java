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
		empleados.add(new Empleado("1", "Kylian", "Mbappe", 5, new HashSet<>(), Rol.Lider, "agregar"));
		empleados.add(new Empleado("2", "Kevin", "De Bruyne", 4, new HashSet<>(), Rol.Arquitecto, "agregar"));
		empleados.add(new Empleado("3", "Toni", "Kroos", 3, new HashSet<>(), Rol.Programador, "agregar"));
		empleados.add(new Empleado("4", "Harry", "Kane", 2, new HashSet<>(), Rol.Tester, "agregar"));
		empleados.add(new Empleado("5", "Giuliano", "Simeone", 2, new HashSet<>(Arrays.asList("1", "2", "3")), Rol.Tester, "agregar"));

		customComparator = (e1, e2) -> {
			double coefficient1 = e1.getPuntaje() - e1.getConflictos().size();
			double coefficient2 = e2.getPuntaje() - e2.getConflictos().size();
			return Double.compare(coefficient2, coefficient1);
		};
		heuristica = new Heuristica(empleados, 1, 1, 1, 1, customComparator);
	}
	
	// Verifica que el algoritmo encuentre una cambinacion no vacia
	@Test
	public void encontrarMejorCombinacionTest() {
		List<Empleado> mejorCombinacion = heuristica.encontrarMejorCombinacion();
		Assert.assertNotNull(mejorCombinacion);
		Assert.assertFalse(mejorCombinacion.isEmpty());
	}
	
	// Verifica que la mejor combinacion encontrada tenga el promedio esperado
	@Test
	public void mejorCombinacionDeMayorPromedioTest() {
	    List<Empleado> equipo = heuristica.encontrarMejorCombinacion();
	    double promedio = 0;
	    for (Empleado e : equipo)
	        promedio += e.getPuntaje();
	    promedio /= equipo.size();
	    Assert.assertEquals(3.5, promedio, 0.01);
	}
	
	// Verifica que el tiempo de ejecucion sea correcto
	@Test
	public void tiempoDeEjecucionDebeSerMayorOIgualACeroTest() {
	    heuristica.encontrarMejorCombinacion();
	    Assert.assertTrue(heuristica.getTiempoEjecucion() >= 0);
	}
	
	// Verifica conflicto de empelados en la combinacion
	@Test
	public void combinacionTieneConflictoConEmpleadosTest() {
		List<Empleado> combination = new ArrayList<>();
		combination.add(empleados.get(4));
		combination.add(empleados.get(1));
		combination.add(empleados.get(2));
		combination.add(empleados.get(3));

		boolean containsConflicted = heuristica.combinacionContieneConflicto(combination, empleados.get(0));
		Assert.assertTrue(containsConflicted);
	}
	
	// Verifica que el equipo no tenga empleados en conflicto
	@Test
	public void equipoGeneradoNoDebeTenerConflictosTest() {
	    List<Empleado> equipo = heuristica.encontrarMejorCombinacion();
	    Assert.assertFalse(heuristica.tieneEmpleadosEnConflicto(equipo));
	}
	
	// Verifica que el equipo tenga la cantidad correcta de roles solicitados
	@Test
	public void equipoGeneradoTieneRolesCorrectosTest() {
	    List<Empleado> equipo = heuristica.encontrarMejorCombinacion();
	    int lideres = 0;
	    int arquitectos = 0;
	    int programadores = 0;
	    int testers = 0;

	    for (Empleado e : equipo)
	        switch (e.getRol()) {
	            case Lider: lideres++; break;
	            case Arquitecto: arquitectos++; break;
	            case Programador: programadores++; break;
	            case Tester: testers++; break;
	        }

	    Assert.assertEquals(1, lideres);
	    Assert.assertEquals(1, arquitectos);
	    Assert.assertEquals(1, programadores);
	    Assert.assertEquals(1, testers);
	}
	
	// Verifica que se genero exactamente una combinacion
	@Test
	public void cantidadCombinacionesGeneradasTest() {
	    heuristica.encontrarMejorCombinacion();
	    Assert.assertEquals(1, heuristica.getCantidadCombinaciones(), 0);
	}
}