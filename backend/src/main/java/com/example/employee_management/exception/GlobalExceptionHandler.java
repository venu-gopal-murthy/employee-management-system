package com.example.employee_management.exception;


import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private Map<String,Object> errorBody(HttpStatus status, String message){
        Map<String,Object> body=new LinkedHashMap<>();
        body.put("status",status.value());
        body.put("error",status.getReasonPhrase());
        body.put("message",message);


        return body;


    }
    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<Map<String,Object>> handleNotFound(EmployeeNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(errorBody(HttpStatus.NOT_FOUND,ex.getMessage()));


    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> handleValidation(MethodArgumentNotValidException ex){
        String message=ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error-> error.getField()+": "+error.getDefaultMessage())
                .distinct()
                .collect(Collectors.joining("; "));

        return ResponseEntity
                .badRequest()
                .body(errorBody(HttpStatus.BAD_REQUEST,message));

    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String,Object>> handleDataIntegrity(DataIntegrityViolationException ex){
        return ResponseEntity
                .badRequest()
                .body(errorBody(HttpStatus.BAD_REQUEST,"Invalid JSON or Data type in request body"));
    }


    /*@ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String,Object>> handleBadjson(HttpMessageNotReadableException ex){
        return ResponseEntity
                .badRequest()
                .body(errorBody(HttpStatus.BAD_REQUEST,"The data violates a database connection."
                        +"check for Duplicate Email address" +
                        "or missing required values"));
    }*/

   @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidJson(HttpMessageNotReadableException ex) {
    Map<String, Object> body = new HashMap<>();
    body.put("status", 400);
    body.put("error", "Bad Request");
    body.put("message", "Invalid JSON request body");
    return ResponseEntity.badRequest().body(body);
}       


}
