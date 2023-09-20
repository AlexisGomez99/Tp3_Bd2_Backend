package ar.unrn.tp.web;

import java.time.LocalDateTime;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class MyResponseEntityExceptionHandler
        extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetail> handleAllMyException(Exception ex, WebRequest request) {


        return new ResponseEntity<ErrorDetail>(new ErrorDetail(LocalDateTime.now(), ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        return new ResponseEntity<Object>(new ErrorDetail(LocalDateTime.now(), ex.getFieldError().getDefaultMessage()), HttpStatus.BAD_REQUEST);
    }

}
