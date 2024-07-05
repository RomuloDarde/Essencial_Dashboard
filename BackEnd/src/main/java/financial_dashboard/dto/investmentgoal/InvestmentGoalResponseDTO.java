package financial_dashboard.dto.investmentgoal;

import java.math.BigDecimal;
import java.time.LocalDate;

public record InvestmentGoalResponseDTO(
        Long id,
        BigDecimal value,
        String status,
        LocalDate registrationDate,
        Long userId
) {
}
