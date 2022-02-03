package com.sparta.board.exceptionhandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ErrorUtil {

    public static void errorResponse(HttpServletResponse response, String script) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.println(script);
        writer.flush();
    }
}
