package com.sparta.board.exceptionhandler;

import com.sparta.board.util.Script;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final HttpServletResponse response;

    @ExceptionHandler(NoHandlerFoundException.class)
    public void noHandlerFoundException(NoHandlerFoundException e) throws IOException {
        StringBuilder msg = new StringBuilder();
        msg.append(e.getRequestURL()+": 요청하신 페이지는 없는 페이지입니다.");
        ErrorUtil.errorResponse(response,Script.redirectToHome(msg.toString()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public void illegalArgumentException(IllegalArgumentException e) throws IOException {
        ErrorUtil.errorResponse(response,Script.back(e.getMessage()));
    }


}
