package com.study.SpringSecurityMybatis.exception;

import lombok.Getter;
import org.springframework.validation.FieldError;
import java.util.List;


public class ValidException extends RuntimeException {

    @Getter
    private List<FieldError> FiledErrors;

    public ValidException(String message, List<FieldError> errors) {
        super(message);
        this.FiledErrors = errors;
    }
}
