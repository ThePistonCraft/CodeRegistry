package de.eincode.coderegistry.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RegisterCommand {

    String name();
    String permission() default "";

    String description() default "";
    String[] aliases() default {};
}
