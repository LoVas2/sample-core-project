package fr.lva.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation to indicate when a method should clear the user cache
 */
@Retention(RUNTIME)
@Target(ElementType.METHOD)
public @interface ClearCache {

    String value();

}
