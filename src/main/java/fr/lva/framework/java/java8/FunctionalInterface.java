package fr.lva.framework.java.java8;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.function.*;

public class FunctionalInterface {

    /**
     * Use function if it takes something and returns something else
     *
     * @param o object applying function
     * @param function to apply
     * @return the result of the function
     */
    public static String applyFunction(Object o, Function<String, String> function) {
        return function.apply((String) o);
    }

    /**
     * Use Supplier if it takes nothing, but returns something.
     *
     * @param supplier : function to call
     */
    public static String applySupplier(Supplier<String> supplier) {
        return supplier.get();
    }

    /**
     * Use Consumer if it takes something, but returns nothing.
     *
     * @param d object to consume
     * @param consumer the consumer function
     */
    public static void applyConsumer(double d, DoubleConsumer consumer) {
        consumer.accept(d);
    }

    /**
     * Use consumer if it takes something and return boolean
     * @return Month predicates filtering "er" month
     */
    public static Predicate<Month> erMonth() {
        return m -> m.getDisplayName(TextStyle.FULL, Locale.US).endsWith("er");
    }

}
