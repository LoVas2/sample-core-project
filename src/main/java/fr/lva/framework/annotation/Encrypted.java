package fr.lva.framework.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation used to indicate an object attribute that should be encrypted
 */
@Retention(RUNTIME)
@Target({FIELD, TYPE})
public @interface Encrypted {

    boolean print() default false;

}
