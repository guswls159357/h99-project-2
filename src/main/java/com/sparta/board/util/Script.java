package com.sparta.board.util;

public class Script {

    public static String back(String msg){
        StringBuffer sb = new StringBuffer();
        sb.append("<script>");
        sb.append("alert(\""+msg+"\");");
        sb.append("history.back();");
        sb.append("</script>");
        return sb.toString();
    }

    public static String redirectToLogin(String msg){
        StringBuffer sb = new StringBuffer();
        sb.append("<script>");
        sb.append("alert(\""+msg+"\");");
        sb.append("window.location.href = '/user/login';");
        sb.append("</script>");
        return sb.toString();
    }

    public static String redirectToHome(String msg){
        StringBuffer sb = new StringBuffer();
        sb.append("<script>");
        sb.append("alert(\""+msg+"\");\n");
        sb.append("window.location.href = '/';");
        sb.append("</script>");
        return sb.toString();
    }
}
