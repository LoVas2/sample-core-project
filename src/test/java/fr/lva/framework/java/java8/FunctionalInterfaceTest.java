package fr.lva.framework.java.java8;

import fr.lva.framework.pojo.User;
import org.junit.Assert;
import org.junit.Test;

import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class FunctionalInterfaceTest {

    @Test
    public void functionTest() {
        String s = ApplyFunctionalInterface.applyFunction("test", String::toUpperCase);
        Assert.assertEquals("Function not applied", "TEST", s);
    }

    @Test
    public void customFunctionTest(){
        Function<String, String> function = s -> s + " added by my function";
        String s = ApplyFunctionalInterface.applyFunction("my string", function);
        Assert.assertEquals("Function not applied", "my string added by my function", s);
    }

    @Test
    public void supplierTest() {
        String s = ApplyFunctionalInterface.applySupplier(String::new);
        Assert.assertNotNull(s);
    }

    @Test
    public void consumerTest() {
        ApplyFunctionalInterface.applyConsumer(1, System.out::println);
    }

    @Test
    public void predicateTest() {
        Predicate<Month> longMonth = m -> m.maxLength() == 31;
        long nbrOfLongMonthInEr = Arrays.stream(Month.values()).filter(ApplyFunctionalInterface.erMonth().and(longMonth)).count();
        Assert.assertEquals("Filters aren't ok", 2L, nbrOfLongMonthInEr);
    }

    @Test
    public void filterByAttribute() {
        User u1 = new User(1L, "foo","password");
        User u2 = new User(2L, "foo","password");
        User u3 = new User(3L, "bar","password");
        List<User> users = Arrays.asList(u1, u2, u3);
        Assert.assertEquals("Filtering is not ok", 2L, users.stream().filter(ApplyFunctionalInterface.filterByAttribute(User::getName)).count());
    }

    // Use Callable if it returns a result and might throw (most akin to Thunk in general CS terms).
    // Use Runnable if it does neither and cannot throw.

}
