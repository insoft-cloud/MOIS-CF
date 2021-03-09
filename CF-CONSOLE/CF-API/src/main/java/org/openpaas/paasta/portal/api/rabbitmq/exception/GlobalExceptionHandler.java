package org.openpaas.paasta.portal.api.rabbitmq.exception;


import org.openpaas.paasta.portal.api.rabbitmq.model.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;


@RestControllerAdvice
public class GlobalExceptionHandler extends RuntimeException {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public GlobalExceptionHandler() {
    }

    @ExceptionHandler({ServiceBrokerApiVersionException.class})
    @ResponseBody
    public ResponseEntity<ErrorMessage> handleException(ServiceBrokerApiVersionException ex, HttpServletResponse response) {
        return this.getErrorResponse(ex.getMessage(), HttpStatus.PRECONDITION_FAILED);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseBody
    public ResponseEntity<ErrorMessage> handleException(HttpMessageNotReadableException ex, HttpServletResponse response) {
        return this.getErrorResponse(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseBody
    public ResponseEntity<ErrorMessage> handleException(MethodArgumentNotValidException ex, HttpServletResponse response) {
        BindingResult result = ex.getBindingResult();
        String message = "Missing required fields:";

        FieldError error;
        for(Iterator var5 = result.getFieldErrors().iterator(); var5.hasNext(); message = message + " " + error.getField()) {
            error = (FieldError)var5.next();
        }

        return this.getErrorResponse(message, HttpStatus.UNPROCESSABLE_ENTITY);
    }


    @ExceptionHandler({NullPointerException.class})
    @ResponseBody
    public ResponseEntity<ErrorMessage> nullException(NullPointerException ex, HttpServletResponse response) {
        logger.info("NullPointerException", ex);
        return getErrorResponse(ex.toString(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({IndexOutOfBoundsException.class})
    @ResponseBody
    public String indexOutOfBoundsException(IndexOutOfBoundsException ex, HttpServletResponse response) {
        logger.info("indexOutOfBoundsException >>> " + ex.getMessage());
        return ex.getMessage();
    }


    @ExceptionHandler({ServiceBrokerException.class})
    @ResponseBody
    public String serviceBrokerException(ServiceBrokerException ex, HttpServletResponse response) {
        logger.info("ServiceBrokerException >>> " + ex.getDetailMessage());
        return ex.getDetailMessage();
    }

    public ResponseEntity<ErrorMessage> getErrorResponse(String message, HttpStatus status) {
        return new ResponseEntity(new ErrorMessage(message,  status.name(),status.value()), status);
    }
}
