package financial_dashboard.dto.financialgoal;

import java.math.BigDecimal;

public record FinancialGoalUpdateRequestDTO(
        String name,
        BigDecimal value,
        String description){
}
