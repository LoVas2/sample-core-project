package fr.lva.framework.automaton;

/**
 * Interface for methods processed in the Automaton
 */
@FunctionalInterface
public interface EventProcessor {

    /**
     * Method called in the Automaton processing
     *
     * @param e the event trigger
     * @param ctx the context updated
     */
    public void process(AutomatonEvent e, Context ctx);
}
