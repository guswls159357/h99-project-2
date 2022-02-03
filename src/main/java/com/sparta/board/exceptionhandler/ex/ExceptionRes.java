package com.sparta.board.exceptionhandler.ex;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


@Getter
@Builder(access = AccessLevel.PROTECTED)
public class ExceptionRes<T> {

    private T errors;

    public static ExceptionRes of(String field, String reason) {
        return ExceptionRes.builder()
                .errors(CustomFieldError.of(field,reason))
                .build();
    }

    public static ExceptionRes of(BindingResult bindingResult){
        return ExceptionRes.builder()
                .errors(CustomFieldError.of(bindingResult))
                .build();
    }

    public static ExceptionRes of(Set<ConstraintViolation<?>> set){
        return ExceptionRes.builder()
                .errors(CustomFieldError.of(set))
                .build();
    }



    @Getter
    @Builder(access = AccessLevel.PROTECTED)
    public static class CustomFieldError {
        private String field;
        private String reason;

        public static List<CustomFieldError> of(String field, String reason) {

            List<CustomFieldError> fieldErrors = new ArrayList<>();
            fieldErrors.add(CustomFieldError.builder()
                    .field(field)
                    .reason(reason)
                    .build());

            return fieldErrors;
        }

        public static List<CustomFieldError> of(BindingResult bindingResult) {

            List<CustomFieldError> customFieldErrors = new ArrayList<>();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for (FieldError error : fieldErrors) {
                customFieldErrors.add(
                        CustomFieldError.builder()
                                .field(error.getField())
                                .reason(error.getDefaultMessage())
                                .build()
                );
            }

            return customFieldErrors;
        }

        public static List<CustomFieldError> of(Set<ConstraintViolation<?>> set) {

            List<CustomFieldError> customFieldErrors = new ArrayList<>();

            set.stream().forEach(error -> {

                Stream<Path.Node> stream = StreamSupport.stream(error.getPropertyPath().spliterator(), false);
                List<Path.Node> list = stream.collect(Collectors.toList());
                String field = list.get(list.size() - 1).getName();
                String message = error.getMessage();

                customFieldErrors.add(
                        CustomFieldError.builder()
                                .field(field)
                                .reason(message)
                                .build()
                );
            });

            return customFieldErrors;
        }
    }

}
