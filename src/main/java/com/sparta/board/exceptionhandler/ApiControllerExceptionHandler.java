package com.sparta.board.exceptionhandler;

import com.sparta.board.exceptionhandler.ex.ExceptionRes;
import com.sparta.board.exceptionhandler.ex.FieldException;
import com.sparta.board.web.dto.ResDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice(annotations = RestController.class)
@Slf4j
public class ApiControllerExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResDto methodArgumentNotValidException(MethodArgumentNotValidException e) {

        return new ResDto(-1, ExceptionRes.of(e.getBindingResult()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResDto constraintViolationException(ConstraintViolationException e) {

        return new ResDto(-1, ExceptionRes.of(e.getConstraintViolations()));
    }

    @ExceptionHandler(FieldException.class)
    public ResDto fieldException(FieldException e){
        return new ResDto(-1,ExceptionRes.of(e.getField(),e.getReason()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResDto illegalArgumentException(IllegalArgumentException e){
        return new ResDto(-1,ExceptionRes.of("not",e.getMessage()));
    }


}
