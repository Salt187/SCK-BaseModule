package org.xry.interceptors.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.xry.interceptors.exception.Exceptions.serviceException;
import org.xry.interceptors.exception.Exceptions.tokenValidationException;
import org.xry.interceptors.pojo.Code;
import org.xry.interceptors.pojo.Result;
import org.xry.interceptors.pojo.WrongMessage;


@RestControllerAdvice
public class GlobeExceptionHandler {
    //处理全局异常
    @ExceptionHandler(Exception.class)
    public Result HandleException(Exception e) {
        e.printStackTrace();
        return new Result(null, WrongMessage.UNKNOWN, Code.Unknown_ERROR);
    }

    //统一处理业务异常
    @ExceptionHandler(serviceException.class)
    public Result HandleServerException(serviceException e) {
        // 打印错误信息
        System.out.println(e.getMsg());
        return new Result(null,e.getMsg(),e.getCode());
    }

    //令牌验证异常
    @ExceptionHandler(tokenValidationException.class)
    public Result HandleTokenValidationException(tokenValidationException e) {
        e.printStackTrace();
        return new Result(null,WrongMessage.TOKEN_EXPIRE,Code.Unknown_ERROR);
    }

    //处理参数校验异常


}
