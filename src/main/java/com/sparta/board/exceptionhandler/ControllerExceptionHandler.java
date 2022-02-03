package com.sparta.board.exceptionhandler;

import com.sparta.board.exceptionhandler.ex.FieldException;
import com.sparta.board.exceptionhandler.ex.FieldsException;
import com.sparta.board.util.Script;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.sparta.board.exceptionhandler.ErrorUtil.errorResponse;

@ControllerAdvice(annotations = Controller.class)
@Slf4j
@RequiredArgsConstructor
public class ControllerExceptionHandler {

    private final HttpServletResponse response;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public void methodArgumentNotValidException(MethodArgumentNotValidException e) throws IOException {

        StringBuilder msg = new StringBuilder();
        e.getBindingResult().getFieldErrors().stream().forEach(fieldError -> {
            msg.append(fieldError.getField() + ": " + fieldError.getDefaultMessage() + "\n");
        });

        errorResponse(response, Script.back(msg.toString()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public void constraintViolationException(ConstraintViolationException e) throws IOException {
        StringBuilder msg = new StringBuilder();

        e.getConstraintViolations().stream().forEach(error -> {

            Stream<Path.Node> stream = StreamSupport.stream(error.getPropertyPath().spliterator(), false);
            List<Path.Node> list = stream.collect(Collectors.toList());
            String field = list.get(list.size() - 1).getName();
            String message = error.getMessage();

            msg.append(field + ": " + message + "\n");

        });

        errorResponse(response, Script.back(msg.toString()));
    }

    @ExceptionHandler(FieldException.class)
    public void fieldException(FieldException e) throws IOException {
        StringBuilder msg = new StringBuilder();

        msg.append(e.getField() + ": " + e.getReason());

        errorResponse(response, Script.back(msg.toString()));
    }

    @ExceptionHandler(FieldsException.class)
    public void fieldsException(FieldsException e) throws IOException {
        StringBuilder msg = new StringBuilder();

        e.getBindingResult().getFieldErrors().stream().forEach(fieldError -> {
            msg.append(fieldError.getField() + ": " + fieldError.getDefaultMessage() + "\\n");

        });

        errorResponse(response, Script.back(msg.toString()));

    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public void noHandlerFoundException(NoHandlerFoundException e) throws IOException {
        StringBuilder msg = new StringBuilder();
        msg.append(e.getRequestURL() + ": 요청하신 페이지는 없는 페이지입니다.");
        errorResponse(response,Script.back(msg.toString()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public void illegalArgumentException(IllegalArgumentException e) throws IOException {

        errorResponse(response,Script.back(e.getMessage()));
    }


}
