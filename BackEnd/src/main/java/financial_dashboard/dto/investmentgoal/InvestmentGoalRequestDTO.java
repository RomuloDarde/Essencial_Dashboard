package financial_dashboard.dto.investmentgoal;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record InvestmentGoalRequestDTO(
        @NotNull
        BigDecimal value) {
}
