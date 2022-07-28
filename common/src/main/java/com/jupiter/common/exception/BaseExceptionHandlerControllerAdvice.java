package com.jupiter.common.exception;

import com.jupiter.common.base.ApiResponse;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class BaseExceptionHandlerControllerAdvice extends ResponseEntityExceptionHandler {

    protected final ResponseEntity<Object> buildResponseFromStandardizedException(StandardizedException ex) {
        return new ResponseEntity<>(ApiResponse.fromStandardizedException(ex), ex.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        log.error("Handling a generic exception of type: {}. Error: {}", ex.getClass().getSimpleName(), ExceptionUtils.getRootCause(ex).getMessage());
        log.error(ExceptionUtils.getStackTrace(ex));
        return this.buildResponseFromStandardizedException(ApiException.fromGenericExceptionWithNoDetails(ex));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public final ResponseEntity<Object> handleException(AccessDeniedException ex, WebRequest request) {
        return this.buildResponseFromStandardizedException(ApiException.fromGenericExceptionWithNoDetails(ex, HttpStatus.UNAUTHORIZED));
    }

    @ExceptionHandler(AuthenticationException.class)
    public final ResponseEntity<Object> handleException(AuthenticationException ex, WebRequest request) {
        return this.buildResponseFromStandardizedException(ApiException.fromGenericExceptionWithNoDetails(ex, HttpStatus.UNAUTHORIZED));
    }

    @ExceptionHandler(ApiException.class)
    public final ResponseEntity<Object> handleException(ApiException ex, WebRequest request) {
        if (StringUtils.isNotBlank(ex.getLogMessage())) log.error(ex.getLogMessage());

        return this.buildResponseFromStandardizedException(ex);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return this.buildResponseFromStandardizedException(ApiException.fromGenericExceptionWithNoDetails(ex));
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return this.buildResponseFromStandardizedException(ApiException.fromGenericExceptionWithNoDetails(ex));
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return this.buildResponseFromStandardizedException(ApiException.fromGenericExceptionWithNoDetails(ex));
    }

    @ExceptionHandler(FeignException.BadRequest.class)
    public ResponseEntity<Object> handleException(FeignException ex) {
        return this.buildResponseFromStandardizedException(ApiException.fromFeignBadException(ex));
    }
}
