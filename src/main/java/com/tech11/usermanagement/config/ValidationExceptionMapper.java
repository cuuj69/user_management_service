package com.tech11.usermanagement.config;

import com.tech11.usermanagement.dto.response.ErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.logging.Logger;
import java.util.stream.Collectors;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    private static final Logger LOGGER = Logger.getLogger(ValidationExceptionMapper.class.getName());

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        LOGGER.warning("Validation exception occurred: " + exception.getMessage());
        
        String validationMessage = exception.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
        
        ErrorResponse errorResponse = new ErrorResponse(400, "Validation failed: " + validationMessage);
        
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(errorResponse)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
} 