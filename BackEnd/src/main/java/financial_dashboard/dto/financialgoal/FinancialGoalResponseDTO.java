package financial_dashboard.dto.financialgoal;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FinancialGoalResponseDTO (
        Long id,
        String name,
        BigDecimal value,
        BigDecimal valueToComplete,
        Double percentage,
        String description,
        LocalDate registrationDate,
        Long userId) {
}
