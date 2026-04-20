package ru.yandex.practicum.filmorate.exception;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = ReleaseDateValidator.class)
@Target({FIELD})
@Retention(RUNTIME)
public @interface ReleaseDateAfter1895 {
    String message() default "Дата релиза должна быть не ранее 28 декабря 1895 года";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
