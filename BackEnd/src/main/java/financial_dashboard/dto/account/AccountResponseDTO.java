package financial_dashboard.dto.account;

import java.math.BigDecimal;

public record AccountResponseDTO(
        Long id,
        BigDecimal currentBalance,
        BigDecimal monthBalance,
        Long userId) {
}
