package com.study.SpringSecurityMybatis.exception;

import org.springframework.stereotype.Component;

public class DeleteUserException extends RuntimeException{

    public DeleteUserException(String message) {
        super(message);
    }
}
