package com.sparta.board.exceptionhandler.ex;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FieldException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    private String field;
    private String reason;
}
