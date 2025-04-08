package com.sintaks.mushandi.exceptions;


import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {



    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<Object> handleNotFoundException(NotFoundException ex){
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND.value());
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }


    @ExceptionHandler(AlreadyExistsException.class)
    protected ResponseEntity<Object> handleAlreadyExistsException(AlreadyExistsException ex){
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value());
        apiError.setMessage(ex.getMessage());
       return buildResponseEntity(apiError);
    }

    @ExceptionHandler(NotSavedException.class)
    public final ResponseEntity<Object> handleNotSaved(NotSavedException ex){
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value());
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }
    
    @ExceptionHandler(NotDeletedException.class)
    public final ResponseEntity<Object> handleNotDeleted(NotDeletedException ex){
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value());
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public final ResponseEntity<Object> handleUserNameAlredyExists(UsernameAlreadyExistsException ex){
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value());
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(UnexpectedException.class)
    public final ResponseEntity<Object> handleUnexpectedException(UnexpectedException ex){
        ApiError apiError = new ApiError(HttpStatus.EXPECTATION_FAILED.value());
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }


    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<Object> handleRuntimeException(
            RuntimeException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value());
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(ValidationException.class)
    protected ResponseEntity<Object> handleValidationException(
            ValidationException ex) {

        ApiValidationError apiVError = new ApiValidationError();
        apiVError.setMessage(ex.getMessage());
        apiVError.setObject("object");
        apiVError.setField("field");
        apiVError.setRejectedValue("RejValue");
        return buildValidationResponseEntity(apiVError);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value());
        apiError.setMessage("Record already exists.");
        apiError.setDebugMessage(ex.getLocalizedMessage());
        return buildResponseEntity(apiError);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value());
        apiError.setMessage("Invalid input");
        apiError.setDebugMessage(ex.getLocalizedMessage());
        return buildResponseEntity(apiError);
    }
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value());

        String message = "";
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            message = fieldError.getDefaultMessage();

        }

        apiError.setMessage(message);
        apiError.setDebugMessage(ex.getMessage());

        return buildResponseEntity(apiError);
    }



    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, HttpStatus.valueOf(apiError.getStatus()));
    }

    private ResponseEntity<Object> buildValidationResponseEntity(ApiValidationError apiVError) {
        return new ResponseEntity<>(apiVError.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
