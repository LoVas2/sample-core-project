package fr.lva.framework.audit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface AuditField {

    String name() default "";

    String path() default "";

    Class<?> type() default Object.class;

    String embeddedPath() default "";

    boolean required() default false;

    boolean externalIdField() default false;

    boolean ignored() default false;

}
