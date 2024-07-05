package financial_dashboard.exception;

import com.sun.jdi.request.DuplicateRequestException;
import financial_dashboard.dto.exception.ErrorFieldAndMessageDTO;
import financial_dashboard.dto.exception.StandartErrorDTO;
import financial_dashboard.dto.exception.VallidationsErrorDTO;
import financial_dashboard.exception.createdexceptions.JWTCreationRuntimeException;
import financial_dashboard.exception.createdexceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.DateTimeException;
import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestExceptionHandler {

    //Status NOT_FOUND
    //Recurso não encontrado
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity handlingErrorResourceNotFound(
            ResourceNotFoundException exception, HttpServletRequest request) {

        var timestamp = Instant.now();
        var status = HttpStatus.NOT_FOUND.value();
        var error = "Resource not found.";
        var message = exception.getMessage();
        var path = request.getRequestURI();

        var errorDTO = new StandartErrorDTO(timestamp, status, error, message, path);
        return ResponseEntity.status(status).body(errorDTO);
    }

    //Validações da classe LocalDate
    @ExceptionHandler(DateTimeException.class)
    public ResponseEntity handlingErrorDateTime(
            DateTimeException exception, HttpServletRequest request) {

        var timestamp = Instant.now();
        var status = HttpStatus.NOT_FOUND.value();
        var error = "Invalid date value.";
        var message = exception.getMessage();
        var path = request.getRequestURI();

        var errorDTO = new StandartErrorDTO(timestamp, status, error, message, path);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
    }


    //Status BAD_REQUEST
    //Validações do BeansVallidation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handlingErrorMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpServletRequest request) {

        var timestamp = Instant.now();
        var status = HttpStatus.BAD_REQUEST.value();
        var error = "Invalid argument.";
        var path = request.getRequestURI();

        var fieldErrors = exception.getFieldErrors();
        var fieldErrorsDTO = fieldErrors.stream()
                .map(e -> new ErrorFieldAndMessageDTO(
                        e.getField(),
                        e.getDefaultMessage()))
                .collect(Collectors.toList());

        var vallidationErrorDTO = new VallidationsErrorDTO(
                timestamp, status, error, path, fieldErrorsDTO);
        return ResponseEntity.badRequest().body(vallidationErrorDTO);
    }

    //Validação do valor único na coluna do SQL
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity handlingErrorSQLIntegrityConstraintViolation(
            SQLIntegrityConstraintViolationException exception,
            HttpServletRequest request) {

        var timestamp = Instant.now();
        var status = HttpStatus.BAD_REQUEST.value();
        var error = "SQL validation error in data persistence.";
        var message = exception.getMessage();
        var path = request.getRequestURI();

        var errorDTO = new StandartErrorDTO(timestamp, status, error, message, path);
        return ResponseEntity.badRequest().body(errorDTO);
    }

    //Validação dos Enums
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity handlingErrorIllegalArgument(
            IllegalArgumentException exception, HttpServletRequest request) {

        var timestamp = Instant.now();
        var status = HttpStatus.BAD_REQUEST.value();
        var error = "Invalid argument.";
        var message = exception.getMessage();
        var path = request.getRequestURI();

        var errorDTO = new StandartErrorDTO(timestamp, status, error, message, path);
        return ResponseEntity.badRequest().body(errorDTO);
    }

    //Validação de uma única meta financeira para cada usuário
    @ExceptionHandler(DuplicateRequestException.class)
    public ResponseEntity handlingErrorDuplicateRequest(
            DuplicateRequestException exception, HttpServletRequest request) {

        var timestamp = Instant.now();
        var status = HttpStatus.BAD_REQUEST.value();
        var error = "Resource creation not allowed.";
        var message = exception.getMessage();
        var path = request.getRequestURI();

        var errorDTO = new StandartErrorDTO(timestamp, status, error, message, path);
        return ResponseEntity.badRequest().body(errorDTO);
    }

    //Exception na criação do Token JWT
    @ExceptionHandler(JWTCreationRuntimeException.class)
    public ResponseEntity handlingErrorJWTCreationRuintime(
            JWTCreationRuntimeException exception, HttpServletRequest request) {

        var timestamp = Instant.now();
        var status = HttpStatus.BAD_REQUEST.value();
        var error = "Error while generating token.";
        var message = exception.getMessage();
        var path = request.getRequestURI();

        var errorDTO = new StandartErrorDTO(timestamp, status, error, message, path);
        return ResponseEntity.badRequest().body(errorDTO);
    }

}
