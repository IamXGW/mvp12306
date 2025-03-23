package com.iamxgw.common;

import com.iamxgw.exception.BusinessException;
import com.iamxgw.exception.ParamException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *  全局异常处理类
 * @author IamXGW
 * @since 2024-07-10 20:15
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = RuntimeException.class)
    @ResponseBody
    public JsonData exceptionHandler(RuntimeException e) {
        log.error("unknown exception", e);
        if (e instanceof ParamException || e instanceof BusinessException) {
            return JsonData.fail(e.getMessage());
        }
        return JsonData.fail("系统异常，请稍后尝试");
    }

    @ExceptionHandler(value = Error.class)
    @ResponseBody
    public JsonData errorHandler(RuntimeException e) {
        log.error("unknown error", e);
        return JsonData.fail("系统异常，请联系管理员");
    }
}