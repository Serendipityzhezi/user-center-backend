package com.example.usercenterbackend.exception;

import com.example.usercenterbackend.common.BaseResponse;
import com.example.usercenterbackend.common.ErrorCode;
import com.example.usercenterbackend.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessExceptionHanler(BusinessException e){
        log.info("BusinessException： " + e.getMessage() ,e);
        return ResultUtils.error(e.getCode(),e.getMessage(),e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeExceptionHanler(RuntimeException e){
        log.info("RuntimeException： " + e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR,e.getMessage(),e.getMessage());
    }
}
