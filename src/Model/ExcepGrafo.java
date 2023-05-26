package Model;

public class ExcepGrafo extends Exception {

    /**
     * Metodo constructor.
     *
     * @param message
     */
    public ExcepGrafo(String message) {
        super(message);
    }

    /**
     * Metodo constructor Excepcion para indicar una expresion invalida.
     */
    public ExcepGrafo() {
        super("The graph contains independent vertices.");
    }
}
