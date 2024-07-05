package financial_dashboard.dto.exception;


import java.time.Instant;
import java.util.List;

public record VallidationsErrorDTO(
        Instant timestamp,
        Integer status,
        String error,
        String path,
        List<ErrorFieldAndMessageDTO> fieldErrors) {
}
