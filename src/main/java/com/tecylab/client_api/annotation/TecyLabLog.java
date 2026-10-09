package com.tecylab.client_api.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TecyLabLog {
    String description() default "";

    OperationType type() default OperationType.READ;

    enum OperationType {
        CREATE,
        READ,
        UPDATE,
        DELETE,
        OTHER
    }
}
