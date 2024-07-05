package financial_dashboard.dto.transaction;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransactionRequestDTO(
        String type,
        String category,
        @NotNull
        BigDecimal value,
        String description
){
}
