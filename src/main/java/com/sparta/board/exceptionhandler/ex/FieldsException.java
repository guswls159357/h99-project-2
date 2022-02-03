package com.sparta.board.exceptionhandler.ex;

import lombok.Getter;
import org.springframework.validation.BindingResult;

@Getter
public class FieldsException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    private BindingResult bindingResult;

    public FieldsException(BindingResult bindingResult){
        super(" ");
        this.bindingResult = bindingResult;
    }

}
