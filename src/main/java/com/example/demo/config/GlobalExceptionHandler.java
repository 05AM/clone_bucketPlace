package com.example.demo.config;

import org.hibernate.TypeMismatchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

//extends ResponseEntityExceptionHandler
@RestControllerAdvice
public class GlobalExceptionHandler {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    // Catch Custom Exception(BaseException)
    @ExceptionHandler({ BaseException.class })
    protected BaseResponse handleCustomException(BaseException exception) {
        return new BaseResponse<>(exception.getStatus());
    }

    // Catch Bad Request Exception
    @ExceptionHandler({
            MissingServletRequestParameterException.class,
            MissingRequestHeaderException.class,
            MethodArgumentNotValidException.class,
            IllegalStateException.class,
            IllegalArgumentException.class,
            TypeMismatchException.class,
            HttpMessageNotReadableException.class,
            MissingServletRequestParameterException.class,
            MultipartException.class,
            NoHandlerFoundException.class,
    })
    protected BaseResponse handleBadRequestException(Exception exception) {
        logger.debug("Bad request exception occurred: {}",
                exception.getMessage(),
                exception);

        return new BaseResponse<>(false,
                exception.getMessage()==null?null:exception.getMessage(),
                HttpStatus.BAD_REQUEST);
    }

    // Catch all Exception
    @ExceptionHandler({ Exception.class })
    protected BaseResponse handleServerException(Exception exception) {
        logger.error("Unexpected exception occurred: {}",
                exception.getMessage(),
                exception);

        return new BaseResponse<>(false,
                exception.getMessage()==null?null:exception.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}