package de.marcelgerber.springboard.api;

import de.marcelgerber.springboard.api.dto.ErrorResponse;
import de.marcelgerber.springboard.exceptions.GameNotFoundException;
import de.marcelgerber.springboard.exceptions.IllegalMoveException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Class for handling custom errors
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Method for handling GameNotFoundException
     *
     * @param e GameNotFoundException
     * @return ResponseEntity with ErrorResponse
     */
    @ExceptionHandler(GameNotFoundException.class)
    public ResponseEntity<ErrorResponse> gameNotFound(GameNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Method for handling IllegalMoveException
     *
     * @param e IllegalMoveException
     * @return ResponseEntity with ErrorResponse
     */
    @ExceptionHandler(IllegalMoveException.class)
    public ResponseEntity<ErrorResponse> illegalMove(IllegalMoveException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
