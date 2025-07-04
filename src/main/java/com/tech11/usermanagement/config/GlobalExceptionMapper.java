package com.tech11.usermanagement.config;

import com.tech11.usermanagement.dto.response.ErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.util.logging.Logger;
import java.util.stream.Collectors;

@Provider
public class GlobalExceptionMapper implements jakarta.ws.rs.ext.ExceptionMapper<Exception> {

    private static final Logger LOGGER = Logger.getLogger(GlobalExceptionMapper.class.getName());

    @Override
    public Response toResponse(Exception exception) {
        LOGGER.warning("Exception occurred: " + exception.getMessage());

        if (exception instanceof ConstraintViolationException) {
            ConstraintViolationException cve = (ConstraintViolationException) exception;
            String validationMessage = cve.getConstraintViolations().stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
            
            ErrorResponse errorResponse = new ErrorResponse(400, "Validation failed: " + validationMessage);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(errorResponse)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        if (exception instanceof BadRequestException) {
            ErrorResponse errorResponse = new ErrorResponse(400, exception.getMessage());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(errorResponse)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        if (exception instanceof NotFoundException) {
            ErrorResponse errorResponse = new ErrorResponse(404, exception.getMessage());
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(errorResponse)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        // Handle any other exceptions
        ErrorResponse errorResponse = new ErrorResponse(500, "Internal server error");
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(errorResponse)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
} 