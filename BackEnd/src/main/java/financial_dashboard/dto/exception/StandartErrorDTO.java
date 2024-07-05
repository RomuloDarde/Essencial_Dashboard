package financial_dashboard.dto.exception;

import org.springframework.http.HttpStatus;

import java.time.Instant;

public record StandartErrorDTO(
        Instant timestamp,
        Integer status,
        String error,
        String message,
        String path) {


}
