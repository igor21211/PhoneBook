package com.example.phonebook.util.Exeptions;


import com.example.phonebook.model.Response;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public Response response(IllegalArgumentException ex, WebRequest request){
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        return Response.fail().withMessage(ex.getMessage());
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public Response resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.message, request.getDescription(false));
        return Response.fail().withMessage(ex.message);
    }

    @ExceptionHandler(ResourceWasDeletedException.class)
    public Response resourceDeletedException(ResourceWasDeletedException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.message, request.getDescription(false));
        return Response.fail().withMessage(ex.message);
    }

    @ExceptionHandler(ResourseNotRemoveTheLastWrite.class)
    public Response resourceNotRemoveTheLastException(ResourseNotRemoveTheLastWrite ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.message, request.getDescription(false));
        return Response.fail().withMessage(ex.message);
    }


    @ExceptionHandler(Exception.class)
    public Response globleExcpetionHandler(Exception ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        return Response.fail().withMessage(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response validatorException(MethodArgumentNotValidException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getDetailMessageArguments()[1].toString().substring(1,ex.getDetailMessageArguments()[1].toString().length()-1), request.getDescription(false));
        return Response.fail().withMessage(ex.getDetailMessageArguments()[1].toString().substring(1,ex.getDetailMessageArguments()[1].toString().length()-1));
    }

    @ExceptionHandler(JwtAuthExeption.class)
    public Response validateToken(JwtAuthExeption ex){
        return Response.fail().withMessage(ex.getMessage());
    }


    @Data
    @AllArgsConstructor
    private static class MyGlobalExceptionHandler {
        private String message;
    }
}
