package com.example.configuration;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理
 *
 * @author zengnianmei
 */
@ControllerAdvice
public class DemoExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public String exceptionHandler(Exception e) {
        return "˃̣̣̥᷄⌓˂̣̣̥᷅  >>>"+e.getMessage();
    }
}
