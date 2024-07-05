package financial_dashboard.dto.financialgoal;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record FinancialGoalPostRequestDTO(
        @NotBlank
        String name,
        BigDecimal value,
        String description) {
}
