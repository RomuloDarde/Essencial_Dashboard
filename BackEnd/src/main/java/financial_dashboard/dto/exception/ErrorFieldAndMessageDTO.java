package financial_dashboard.dto.exception;

import org.springframework.http.HttpStatus;

import java.time.Instant;

public record ErrorFieldAndMessageDTO(
        String field,
        String message) {
}
