package fr.lva.framework.automaton;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static fr.lva.framework.automaton.AutomatonTest.States.*;
import static fr.lva.framework.automaton.AutomatonTest.Events.Event1;
import static fr.lva.framework.automaton.AutomatonTest.Events.Event2;
import static fr.lva.framework.automaton.AutomatonTest.Events.Event3;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class AutomatonTest {
    private static final Logger LOG = LogManager.getLogger();

    public enum States implements AutomatonState {
            Init, State1, State2, Done
    }

    public enum Events implements AutomatonEvent {
            Event1, Event2, Event3
    }

    private final static int READY = 0x0000001;
    private final static int FAIL  = 0x0000002;
    private final static int ERROR = 0x0000004;

    private AutomatonState currentState;
    private int            conditions;
    private boolean        failed;
    private boolean        errorEncountered;

    private void process(AutomatonEvent e) {
        new StateMachine(this).process(e);
    }

    @Test
    public void automatonTest() {
        this.process(Event1);
        assertEquals(State1, this.currentState);
        assertFalse(this.failed);
        assertFalse(this.errorEncountered);
        this.process(Event2);
        assertEquals(State2, this.currentState);
        assertFalse(this.failed);
        assertTrue(this.errorEncountered);
        this.process(Event3);
        assertEquals(State1, this.currentState);
        assertTrue(this.failed);
        assertFalse(this.errorEncountered);
        this.process(Event2);
        assertEquals(State2, this.currentState);
        this.process(Event3);
        assertEquals(Done, this.currentState);
    }

    @Test(expected = ExceptionInInitializerError.class)
    public void buildAutomatonKoTest() {
        new StateKoMachine().process(Event1);
    }

    private final static class StateMachine extends DefaultContext {
        private final static Automaton automaton = stateAutomaton();

        private final AutomatonTest sut;

        public StateMachine(AutomatonTest sut) {
            super(sut.currentState, sut.conditions);
            this.sut = sut;
        }

        public void process(AutomatonEvent e) {
            automaton.process(e, this);
            sut.currentState = this.getState();
            sut.conditions = this.getConditions();
        }

        private static Automaton stateAutomaton() {
            return Automaton.builder().initialState(Init)
                .transition(Event1, StateMachine::init_state1, State1)
                .fallback(StateMachine::invalidEvent, Done).state(State1)
                .transition(Event2, READY, StateMachine::state1_state2, State2)
                .transition(Event2, FAIL, StateMachine::init_state1, State2)
                .transition(Event2, StateMachine::error, Done)
                .fallback(StateMachine::invalidEvent, Done).state(State2)
                .transition(Event3, ERROR, StateMachine::state2_state1, State1)
                .transition(Event3, StateMachine::checkFailCond, StateMachine::state2_done, Done)
                .transition(Event1, State1).transition(Event3, StateMachine::error, Done)
                .fallback(Done).end(Done);
        }

        private static void init_state1(AutomatonEvent e, Context ctx) {
            ctx.setCondition(READY);
        }

        private static void state1_state2(AutomatonEvent e, Context ctx) {
            ctx.setCondition(ERROR).resetCondition(READY);
            AutomatonTest sut = ((StateMachine) ctx).sut;
            sut.errorEncountered = true;
        }

        private static void state2_state1(AutomatonEvent e, Context ctx) {
            ctx.setCondition(FAIL).resetCondition(ERROR);
            AutomatonTest sut = ((StateMachine) ctx).sut;
            sut.failed = true;
            sut.errorEncountered = false;
        }

        private static boolean checkFailCond(Context ctx) {
            return ctx.matches(FAIL);
        }

        private static void state2_done(AutomatonEvent e, Context ctx) {
            ctx.setCondition(ERROR).resetCondition(READY);
        }

        private static void error(AutomatonEvent e, Context ctx) {
            logError(e, ctx, "Invalid event");
        }

        private static void invalidEvent(AutomatonEvent e, Context ctx) {
            logError(e, ctx, "Unexpected event");
        }

        private static void logError(AutomatonEvent e, Context ctx, String msg) {
            ((StateMachine) ctx).sut.errorEncountered = true;
            LOG.error("Rejected event \"{}\" in state \"{}\": {}", e, ctx.getState(), msg);
        }
    }

    private final static class StateKoMachine extends DefaultContext {
        private final static Automaton automaton = stateAutomaton();

        private static Automaton stateAutomaton() {
            return Automaton.builder().initialState(Init)
                .transition(Event1, StateMachine::init_state1, State1)
                .fallback(StateMachine::invalidEvent, Done).end(Done);
        }

        public void process(AutomatonEvent e) {
            automaton.process(e, this);
        }
    }
}
