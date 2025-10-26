package org.xry.interceptors.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.xry.interceptors.exception.Exceptions.serviceException;
import org.xry.interceptors.exception.Exceptions.tokenValidationException;
import org.xry.interceptors.pojo.Code;
import org.xry.interceptors.pojo.Result;
import org.xry.interceptors.pojo.WrongMessage;


@RestControllerAdvice
public class GlobeExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobeExceptionHandler.class);



    //处理全局异常
    @ExceptionHandler(Exception.class)
    public Result HandleException(Exception e) {
        //记录日志
        log.error("未处理的异常，建议排查并添加自定义",e);

        return new Result(null, WrongMessage.UNKNOWN, Code.Unknown_ERROR);
    }

    //统一处理业务异常
    @ExceptionHandler(serviceException.class)
    public Result HandleServerException(serviceException e) {
        // 打印错误信息
        log.error(e.getMessage(),e);
        return new Result(null,e.getMsg(),e.getCode());
    }

    //令牌验证异常
    @ExceptionHandler(tokenValidationException.class)
    public Result HandleTokenValidationException(tokenValidationException e) {
        log.error(e.getMessage(),e);

        return new Result(null,WrongMessage.TOKEN_EXPIRE,Code.Unknown_ERROR);
    }

    //处理参数校验异常


}
