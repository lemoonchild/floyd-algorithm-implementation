package JUnit;

import static org.junit.jupiter.api.Assertions.*;

import Controller.ControladorGrafo;
import Model.ExcepGrafo;
import org.junit.jupiter.api.Test;


class GraphManagerTest {

    @Test
    void testShorterRoute() {
        ControladorGrafo manager = new ControladorGrafo();
        String[] lines = new String[] {"Mixco Antigua 30", "Antigua Escuintla 25", "Escuintla Santa-Lucia 15", "Santa-Lucia Guatemala 90", "Guatemala Mixco 15", "Guatemala Antigua 40", "Escuintla Guatemala 70"};
        try {
            manager.fileToGraph(lines);
        } catch (ExcepGrafo e) {
            // TODO Auto-generated catch block
            System.out.println("El grafo contiene vertices no conectados.");
        }
        assertEquals(manager.shorterRoute("Mixco", "Antigua"), "Route: 30.0 km");
    }

    @Test
    void testgetGraphCenter() {
        ControladorGrafo manager = new ControladorGrafo();
        String[] lines = new String[] {"Mixco Antigua 30", "Antigua Escuintla 25", "Escuintla Santa-Lucia 15", "Santa-Lucia Guatemala 90", "Guatemala Mixco 15", "Guatemala Antigua 40", "Escuintla Guatemala 70"};
        try {
            manager.fileToGraph(lines);
        } catch (ExcepGrafo e) {
            // TODO Auto-generated catch block
            System.out.println("El grafo contiene vertices no conectados.");
        }
        assertEquals(manager.getGraphCenter(), "Antigua");
    }

    @Test
    void testBreakRoute() {
        ControladorGrafo manager = new ControladorGrafo();
        String[] lines = new String[] {"Mixco Antigua 30", "Antigua Escuintla 25", "Escuintla Santa-Lucia 15", "Santa-Lucia Guatemala 90", "Guatemala Mixco 15", "Guatemala Antigua 40", "Escuintla Guatemala 70"};
        try {
            manager.fileToGraph(lines);
        } catch (ExcepGrafo e) {
            // TODO Auto-generated catch block
            System.out.println("El grafo contiene vertices no conectados.");
        }
        assertEquals(manager.shorterRoute("Mixco", "Antigua"), "Route: 30.0 km");
        assertEquals(manager.breakRoute("Mixco", "Antigua"),"Route successfully deleted, the shortest routes have been recalculated.\n");
        assertEquals(manager.shorterRoute("Mixco", "Antigua"), "Route: 55.0 km\nIntermediate Cities: Guatemala");

    }

    @Test
    void testNewRoute() {
        ControladorGrafo manager = new ControladorGrafo();
        String[] lines = new String[] {"Mixco Antigua 30", "Antigua Escuintla 25", "Escuintla Santa-Lucia 15", "Santa-Lucia Guatemala 90", "Guatemala Mixco 15", "Guatemala Antigua 40", "Escuintla Guatemala 70"};
        try {
            manager.fileToGraph(lines);
            manager.newRoute("Guatemala", "Mexico", 70);
        } catch (ExcepGrafo e) {
            // TODO Auto-generated catch block
            System.out.println("Ha ocurrido un error al manipular el grafo, saliendo del programa...");
        }
        assertEquals(manager.shorterRoute("Antigua", "Mexico"), "Route: 110.0 km\nIntermediate Cities: Guatemala");

    }

}

