package fr.lva.framework.automaton;

import fr.lva.framework.utils.Args;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * Automaton for processing an Event throw a Context
 */
public final class Automaton {

    private static final Logger LOG = LogManager.getLogger();

    /**
     * The states descriptions by state
     */
    private final Map<AutomatonState, StateDesc> states;

    /**
     * The initial state
     */
    private final AutomatonState initialState;

    /**
     * Automaton constructor used by Builder
     *
     * @param states the states descriptions
     * @param initialState the initial state
     * @param endStates the end states
     */
    private Automaton(Collection<StateDesc> states, AutomatonState initialState, Collection<AutomatonState> endStates) {
        // Adding known states
        Set<AutomatonState> validStates = new HashSet<>(endStates);
        for (StateDesc d : states) {
            validStates.add(d.state);
        }
        Map<AutomatonState, StateDesc> entryStates = new HashMap<>();
        states.stream().forEach(d -> {
            d.transitions.stream().forEach(t -> {
                if (!validStates.contains(t.nextState)) {
                    throw new IllegalStateException("" + d.state + ": Undefined target state \""
                        + t.nextState + " for event \"" + t.event + '"');
                }
            });
            entryStates.put(d.state, d);
        });
        this.states = Collections.unmodifiableMap(entryStates);
        this.initialState = initialState;
    }

    /**
     * Process an event and update the context
     *
     * @param e the handled event
     * @param ctx the current context
     * @return the updated context
     */
    public Context process(AutomatonEvent e, Context ctx) {
        Args.notNull(e, "AutomatonEvent cannot be null");
        Args.notNull(ctx, "Context cannot be null");
        if (ctx.getState() == null) {
            ctx.setState(this.initialState);
        }
        AutomatonState currentState = ctx.getState();
        StateDesc stateDesc = this.states.get(currentState);
        if (stateDesc == null) {
            throw new IllegalStateException(currentState.toString());
        }
        Transition transition = null;
        for (Transition t : stateDesc.transitions) {
            if (t.event.equals(e) && t.condition.matches(ctx)) {
                transition = t;
                break;
            }
        }
        if (transition == null) {
            transition = stateDesc.fallback;
        }
        AutomatonState nextState = transition.nextState;
        LOG.info("Transition: [{}] {} -> {}", currentState, e, nextState);
        ctx.setState(nextState);
        transition.processor.process(e, ctx);
        return ctx;
    }

    /**
     * Get the automaton builder
     *
     * @return a new automaton builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Automaton Builder
     */
    public static final class Builder {

        private final Map<AutomatonState, StateDesc> states = new HashMap<>();

        private AutomatonState initialState;

        /**
         * Private constructor
         */
        private Builder() {
            super();
        }

        /**
         * Initialize the initial state of the automaton
         *
         * @param state the initial state
         * @return a state builder
         */
        public StateBuilder initialState(AutomatonState state) {
            return this.state(state, true);
        }

        /**
         * Initialize a state of the automaton
         *
         * @param state the state
         * @return a state builder
         */
        public StateBuilder state(AutomatonState state) {
            return this.state(state, false);
        }

        /**
         * Initialize a state of the automaton
         *
         * @param state the state to initialize
         * @param initial : true if the state is the initial one
         * @return a state builder
         */
        private StateBuilder state(AutomatonState state, boolean initial) {
            Args.notNull(state, "state");
            if (states.containsKey(state)) {
                throw new IllegalArgumentException(state.toString() + " already defined.");
            }
            if (initial) {
                this.initialState = state;
            } else if (this.initialState == null) {
                throw new IllegalStateException("No initial state defined");
            }
            return new StateBuilder(state);
        }

        /**
         * Ends the automaton build
         *
         * @param endStates the end states uninitialized
         * @return the automaton
         */
        public Automaton end(AutomatonState... endStates) {
            return new Automaton(states.values(), this.initialState, Arrays.asList(endStates));
        }

        /**
         * Represent a mutable StateBuilder
         */
        public final class StateBuilder {

            /**
             * The state under build
             */
            private final AutomatonState state;

            /**
             * The collection of Transitions relating to the state
             */
            private final Collection<Transition> transitions = new LinkedList<>();

            /**
             * Private constructor
             *
             * @param state the state under build
             */
            private StateBuilder(AutomatonState state) {
                this.state = state;
            }

            /**
             * Add a transition to the state
             *
             * @param e the event trigger
             * @param next the next state
             * @return a reference to this object
             */
            public StateBuilder transition(AutomatonEvent e, AutomatonState next) {
                return this.transition(e, null, null, next);
            }

            /**
             * Add a transition to the state
             *
             * @param e the event trigger
             * @param p the code executed during the process
             * @param next the next state
             * @return a reference to this object
             */
            public StateBuilder transition(AutomatonEvent e, EventProcessor p,
                AutomatonState next) {
                return this.transition(e, null, p, next);
            }

            /**
             * Add a transition to the state
             *
             * @param e the event trigger
             * @param cond the condition to process the event
             * @param next the next state
             * @return a reference to this object
             */
            public StateBuilder transition(AutomatonEvent e, int cond, AutomatonState next) {
                return this.transition(e, cond, null, next);
            }

            /**
             * Add a transition to the state
             *
             * @param e the event trigger
             * @param cond the condition to process the event
             * @param next the next state
             * @return a reference to this object
             */
            public StateBuilder transition(AutomatonEvent e, ConditionMatcher cond,
                AutomatonState next) {
                return this.transition(e, cond, null, next);
            }

            /**
             * Add a transition to the state
             *
             * @param e the event trigger
             * @param cond the condition to process the event
             * @param p the code executed during the process
             * @param next the next state
             * @return a reference to this object
             */
            public StateBuilder transition(AutomatonEvent e, final int cond, EventProcessor p,
                AutomatonState next) {
                return transition(e, ctx -> ctx.matches(cond), p, next);
            }

            /**
             * Add a transition to the state
             *
             * @param e the event trigger
             * @param cond the condition to process the event
             * @param p the code executed during the process
             * @param next the next state
             * @return a reference to this object
             */
            public StateBuilder transition(AutomatonEvent e, ConditionMatcher cond,
                EventProcessor p, AutomatonState next) {
                Args.notNull(e, "e");
                LOG.debug("Ajout d une transition pour l etat {} : {} -> {} ",
                    this.state.toString(), e.toString(), next.toString());
                this.transitions.add(new Transition(e, cond, p, next));
                return this;
            }

            /**
             * Add a default transition to the state used in case of error. Ends the stateBuilder
             *
             * @param next the next state
             * @return an automaton builder
             */
            public Builder fallback(AutomatonState next) {
                return fallback(null, next);
            }

            /**
             * Add a default transition to the state used in case of error. Ends the stateBuilder
             *
             * @param p the code executed during the process
             * @param next the next state
             * @return an automaton builder
             */
            public Builder fallback(EventProcessor p, AutomatonState next) {
                Transition fallback = new Transition(null, null, p, next);
                Builder.this.states.put(this.state,
                    new StateDesc(this.state, this.transitions, fallback));
                return Builder.this;
            }
        }
    }

    /**
     * Represents the description of a state with his transitions and his fallback
     */
    private static final class StateDesc {
        private final AutomatonState         state;
        private final Collection<Transition> transitions;
        private final Transition             fallback;

        /**
         * State Description constructor
         *
         * @param state the described state
         * @param transitions the state transitions
         * @param fallback the default transition in case of error
         */
        public StateDesc(AutomatonState state, Collection<Transition> transitions,
            Transition fallback) {
            Args.notNull(state, "state");
            Args.notNull(fallback, "fallback");
            this.state = state;
            this.transitions = new LinkedList<>(transitions);
            this.fallback = fallback;
        }
    }

    /**
     * Transition from one state to another
     */
    private static final class Transition {
        /**
         * The trigger event
         */
        private final AutomatonEvent event;

        /**
         * The condition for processing the transition
         */
        private final ConditionMatcher condition;

        /**
         * The code executed during the process
         */
        private final EventProcessor processor;

        /**
         * The next state
         */
        private final AutomatonState nextState;

        /**
         * Constructor of transition
         *
         * @param e the trigger event
         * @param cond the condition matcher
         * @param p the processed function
         * @param next the next state
         */
        public Transition(AutomatonEvent e, ConditionMatcher cond, EventProcessor p,
            AutomatonState next) {
            Args.notNull(next, "next");
            this.event = e;
            // If no cond. matcher is defined, all conditions match.
            this.condition = (cond != null) ? cond : ((ctx) -> true);
            // If no processor is defined, execute a NOP function.
            this.processor = (p != null) ? p : ((x, ctx) -> LOG.trace("No function defined"));
            this.nextState = next;
        }
    }

}
