package com.grupo7.renthotels.exception.handler;

import com.grupo7.renthotels.exception.Exceptions;
import com.grupo7.renthotels.exception.NotFoundException;
import com.grupo7.renthotels.exception.UnprocessableEntityException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exceptions.class)
    public ResponseEntity<String> errorBadRequest(Exceptions exception) {
        return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> errorNotFound(NotFoundException exception){
        return new  ResponseEntity(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnprocessableEntityException.class)
    public ResponseEntity<String> errorUnprocessableEntity(UnprocessableEntityException exception) {
        return new ResponseEntity(exception.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }


}
