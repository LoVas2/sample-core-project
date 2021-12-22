package fr.lva.framework.automaton;

/**
 * Represents the context handled by the automaton
 */
public interface Context {

    /**
     * Get the current context state
     *
     * @return the current context state
     */
    public AutomatonState getState();

    /**
     * Set the current context state
     *
     * @param state the state
     */
    public void setState(AutomatonState state);

    /**
     * Get the condition
     *
     * @return the condition
     */
    public int getConditions();

    /**
     * Set the condition
     *
     * @param conditions the condition
     * @return the context
     */
    public void setConditions(int conditions);

    /**
     * Set the condition
     *
     * @param cond the condition
     * @return the context
     */
    public default Context setCondition(int cond) {
        this.setConditions(this.getConditions() | cond);
        return this;
    }

    /**
     * Reset the conditions
     *
     * @param cond the conditions
     * @return the context
     */
    public default Context resetCondition(int cond) {
        this.setConditions(this.getConditions() ^ cond);
        return this;
    }

    /**
     * Test if the conditions matches
     *
     * @param cond the condition to test
     * @return true if the conditions matches
     */
    public default boolean matches(int cond) {
        return (this.getConditions() & cond) == cond;
    }

    /**
     * Reset the Context
     *
     * @param state the new state
     * @param conditions the new conditions
     * @return the new Context
     */
    public default Context reset(AutomatonState state, int conditions) {
        this.setState(state);
        this.setConditions(conditions);
        return this;
    }
}
