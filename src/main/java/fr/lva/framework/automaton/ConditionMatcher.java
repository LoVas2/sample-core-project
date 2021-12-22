package fr.lva.framework.automaton;

/**
 * Interface for a context condition tested in automaton process during a transition
 */
@FunctionalInterface
public interface ConditionMatcher {

    /**
     * Tests the context for processing an event
     *
     * @param ctx the context
     * @return true if conditions are valid
     */
    public boolean matches(Context ctx);
}
