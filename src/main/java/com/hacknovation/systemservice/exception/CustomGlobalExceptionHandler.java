package com.hacknovation.systemservice.exception;

import com.hacknovation.systemservice.constant.MessageConstant;
import com.hacknovation.systemservice.exception.httpstatus.BadRequestException;
import com.hacknovation.systemservice.exception.httpstatus.ForbiddenException;
import com.hacknovation.systemservice.exception.httpstatus.NotFoundException;
import com.hacknovation.systemservice.exception.httpstatus.UnauthorizedException;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StructureRS> globalExceptionHandler(Exception ex) {
        StructureRS structureRS = new StructureRS(
                HttpStatus.INTERNAL_SERVER_ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()
        );
        ex.printStackTrace();
        return new ResponseEntity<>(structureRS, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<StructureRS> accessDenied(Exception ex) {
        StructureRS structureRS = new StructureRS(
                HttpStatus.FORBIDDEN,
                HttpStatus.FORBIDDEN.getReasonPhrase()
        );
        ex.printStackTrace();
        return new ResponseEntity<>(structureRS, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadSqlGrammarException.class)
    public ResponseEntity<StructureRS> badSqlGrammerExceptionHandler(Exception ex) {
        StructureRS structureRS = new StructureRS(
                HttpStatus.INTERNAL_SERVER_ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()
        );
        ex.printStackTrace();
        return new ResponseEntity<>(structureRS, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<StructureRS> userNameNotFoundExceptionHandler(UsernameNotFoundException ex) {
        StructureRS structureRS = new StructureRS(
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );
        ex.printStackTrace();
        return new ResponseEntity<>(structureRS, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<StructureRS> badRequestExceptionHandler(BadRequestException ex) {
        StructureRS structureRS = new StructureRS(
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );
        ex.printStackTrace();
        return new ResponseEntity<>(structureRS, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<StructureRS> forbiddenExceptionHandler(ForbiddenException ex) {
        StructureRS structureRS = new StructureRS(
                HttpStatus.FORBIDDEN,
                ex.getMessage()
        );
        ex.printStackTrace();
        return new ResponseEntity<>(structureRS, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<StructureRS> unauthorizedExceptionHandler(UnauthorizedException ex) {
        StructureRS structureRS = new StructureRS(
                HttpStatus.UNAUTHORIZED,
                ex.getMessage()
        );
        ex.printStackTrace();
        return new ResponseEntity<>(structureRS, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<StructureRS> notFoundExceptionHandler(NotFoundException ex) {
        StructureRS structureRS = new StructureRS(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
        ex.printStackTrace();
        return new ResponseEntity<>(structureRS, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<StructureRS> constraintViolationException(Exception ex) {
        StructureRS structureRS = new StructureRS(
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.getReasonPhrase()
        );
        ex.printStackTrace();
        return new ResponseEntity<>(structureRS, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        StructureRS structureRS = new StructureRS(HttpStatus.BAD_REQUEST, MessageConstant.VALIDATION_FAILED);
        ex.printStackTrace();
        final List<String> errors = new ArrayList<>();
        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        structureRS.setData(errors);
        return new ResponseEntity<>(structureRS, HttpStatus.BAD_REQUEST);
    }


}
