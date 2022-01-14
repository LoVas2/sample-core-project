package fr.lva.framework.java.java8;

/**
 * A FunctionalInterface can have only 1 abstract method. But can implement many default methods.
 * Adding too many default methods to the interface is not a very good architectural decision.
 * This should be considered a compromise, only to be used when required for upgrading existing interfaces without breaking backward compatibility.
 *
 * Functional interfaces can be extended by other functional interfaces if their abstract methods have the same signature
 */
@java.lang.FunctionalInterface
public interface FunctionalInterface {

    String method(String str);

    default String methodDefault(String str) {
        return "";
    }

    /**
     * Class to show how to use functional interface and how scope is affected by lambda expression.
     */
    class FunctionalInterfaceUsage {

        private String value = "Value from class";

        // Use FunctionalInterface with lambda expression
        // We can’t hide variables from the enclosing scope inside the lambda’s body.
        FunctionalInterface fi = s -> {
            String value = "Value from lambda";
            return this.value + " let's do it";
        };

        // Over an inner class
        // When we use an inner class, it creates a new scope. We can hide local variables from the enclosing scope by instantiating new local variables with the same names.
        FunctionalInterface fiIC = new FunctionalInterface() {
            String value = "Value from inner class";
            @Override
            public String method(String str) {
               return this.value + " don't do it";
            }
        };

        String result = "Results: resultIC = " + fi.method("") +
                        ", resultLambda = " + fiIC.method("");
        // result = "Results: resultIC = Value from inner class, resultLambda = Value from class"
    }
}
