package com.example.demooauth2.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
;
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    //other exception handlers
    private ResponseEntity<Object> buildResponseEntity(CommandResult apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
        CommandResult apiError = new CommandResult(HttpStatus.NOT_FOUND);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Object> handle(Exception ex,HttpServletRequest request, HttpServletResponse response) {
//        if (ex instanceof NullPointerException) {
//            ApiErrorViewModel apiError = new ApiErrorViewModel(HttpStatus.BAD_REQUEST);
//            apiError.setMessage(ex.getMessage());
//            return buildResponseEntity(apiError);
//        }
//        ApiErrorViewModel apiError = new ApiErrorViewModel(HttpStatus.INTERNAL_SERVER_ERROR);
//        apiError.setMessage(ex.getMessage());
//        return buildResponseEntity(apiError);
//    }
}
