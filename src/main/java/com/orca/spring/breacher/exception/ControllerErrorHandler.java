package com.orca.spring.breacher.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@ControllerAdvice
public class ControllerErrorHandler extends ResponseEntityExceptionHandler
{
    @ExceptionHandler({VirtualMachineNotFoundException.class})
    public ResponseEntity<ControllerErrorResponse> handleVirtualMachineNotFoundException()
    {
        var errorResponse = new ControllerErrorResponse.ControllerErrorResponseBuilder()
                .withFurtherDetails("Cannot find Virtual Machine definition")
                .withErrorMessage("Invalid VM ID. Please provide a valid one, or contact System Admin.")
                .withErrorCode(HttpStatus.NOT_FOUND.name())
                .withStatus(HttpStatus.NOT_FOUND)
                .atTime(LocalDateTime.now(ZoneOffset.UTC))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InternalRestEndpointException.class)
    protected ResponseEntity<Object> handleInternalRestEndpointException(InternalRestEndpointException e, HttpStatus status)
    {
        var errorResponse = new ControllerErrorResponse.ControllerErrorResponseBuilder()
                .withFurtherDetails("Internal REST endpoint fault, please contact System Admin.")
                .withErrorCode(e.getLocalizedMessage())
                .withErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .atTime(LocalDateTime.now(ZoneOffset.UTC))
                .withStatus(status)
                .build();

        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleRestEndpointGeneralException(Exception e, HttpStatus status)
    {
        var errorResponse = new ControllerErrorResponse.ControllerErrorResponseBuilder()
                .withFurtherDetails("Something want wrong, please contact System Admin.")
                .withErrorMessage(e.getLocalizedMessage())
                .withErrorCode(HttpStatus.SERVICE_UNAVAILABLE.name())
                .atTime(LocalDateTime.now(ZoneOffset.UTC))
                .withStatus(status)
                .build();

        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }
}
