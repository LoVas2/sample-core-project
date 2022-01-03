package fr.lva.framework.java.java8;

import org.junit.Assert;
import org.junit.Test;

import java.time.Month;
import java.util.Arrays;
import java.util.function.Predicate;

public class FunctionalInterfaceTest {

    @Test
    public void functionTest() {
        String s = FunctionalInterface.applyFunction("test", String::toUpperCase);
        Assert.assertEquals("Function not applied", "TEST", s);
    }

    @Test
    public void supplierTest() {
        String s = FunctionalInterface.applySupplier(String::new);
        Assert.assertNotNull(s);
    }

    @Test
    public void consumerTest() {
        FunctionalInterface.applyConsumer(1, System.out::println);
    }

    @Test
    public void predicateTest() {
        Predicate<Month> longMonth = m -> m.maxLength() == 31;
        long nbrOfLongMonthInEr = Arrays.stream(Month.values()).filter(FunctionalInterface.erMonth().and(longMonth)).count();
        Assert.assertEquals("Filters aren't ok", 2L, nbrOfLongMonthInEr);
    }

    // Use Callable if it returns a result and might throw (most akin to Thunk in general CS terms).
    // Use Runnable if it does neither and cannot throw.

}
