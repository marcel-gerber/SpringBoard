package de.marcelgerber.springboard.api;

import de.marcelgerber.springboard.api.dto.ErrorResponse;
import de.marcelgerber.springboard.exceptions.GameNotFoundException;
import de.marcelgerber.springboard.exceptions.IllegalMoveException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Class for handling custom errors
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(@Nullable HttpMessageNotReadableException ex,
                                                                  @Nullable HttpHeaders headers,
                                                                  @Nullable HttpStatusCode status,
                                                                  @Nullable WebRequest request) {
        String error = "No valid JSON format";
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), error);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  @Nullable HttpHeaders headers,
                                                                  @Nullable HttpStatusCode status,
                                                                  @Nullable WebRequest request) {
        String error = ex.getBindingResult().getAllErrors().getFirst().getDefaultMessage();
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), error);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

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
