package fr.lva.framework.automaton;

/**
 * Default Automaton Context with a current AutomatonState and int conditions
 */
public class DefaultContext implements Context {
    /**
     * The current state
     */
    protected AutomatonState currentState;

    /**
     * The conditions
     */
    protected int conditions;

    /**
     * Constructs a DefaultContext with current state null.
     */
    public DefaultContext() {
        this(null);
    }

    /**
     * Constructs a DefaultContext with a current state.
     *
     * @param currentState the current state
     */
    public DefaultContext(AutomatonState currentState) {
        this(currentState, 0);
    }

    /**
     * Constructs a DefaultContext with a current state and conditions.
     *
     * @param currentState the current state
     * @param conditions the conditions
     */
    public DefaultContext(AutomatonState currentState, int conditions) {
        this.currentState = currentState;
        this.conditions = conditions;
    }

    @Override
    public AutomatonState getState() {
        return this.currentState;
    }

    @Override
    public void setState(AutomatonState s) {
        this.currentState = s;
    }

    @Override
    public int getConditions() {
        return this.conditions;
    }

    @Override
    public void setConditions(int conditions) {
        this.conditions = conditions;
    }
}
