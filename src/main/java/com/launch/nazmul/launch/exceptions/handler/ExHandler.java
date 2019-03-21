package com.launch.nazmul.launch.exceptions.handler;

import com.launch.nazmul.launch.exceptions.forbidden.ForbiddenException;
import com.launch.nazmul.launch.exceptions.invalid.InvalidException;
import com.launch.nazmul.launch.exceptions.notfound.NotFoundException;
import org.omg.CORBA.portable.UnknownException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;

@ControllerAdvice
public class ExHandler {

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity handleForbiddenException(ForbiddenException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidException.class)
    public ResponseEntity handleInvalidException(InvalidException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity handleNotFoundException(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(UnknownException.class)
    public ResponseEntity handleUnknownException(UnknownException ex) {
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(ex.getMessage());
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity handleMultipartException(MultipartException ex) {
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(ex.getMessage());
    }
}
