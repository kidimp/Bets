package org.chous.bets.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class DuplicateCodesValidator<V extends Annotation, T> implements ConstraintValidator<V, T> {

    protected boolean isValid(List<String> codes, String errorMessage, ConstraintValidatorContext context) {

        context.disableDefaultConstraintViolation();

        Set<String> uniqueItems = new HashSet<>();
        List<String> duplicates = codes.stream()
                .filter(item -> !uniqueItems.add(item))
                .toList();

        if (!duplicates.isEmpty()) {
            String msg = errorMessage.formatted(duplicates.toString());
            context.buildConstraintViolationWithTemplate(msg).addConstraintViolation();
            return false;
        }

        return true;
    }
}
