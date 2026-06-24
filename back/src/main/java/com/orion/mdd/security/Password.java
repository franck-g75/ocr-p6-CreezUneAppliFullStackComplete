package com.orion.mdd.security;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;
@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Password {
    String message() default "Le mot de passe doit contenir au moins 1 chiffre, 1 majuscule, 1 minuscule, 1 caractère spécial. et faire 8 caractères mini.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
