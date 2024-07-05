package financial_dashboard.dto.transaction;

import financial_dashboard.model.enums.TransactionCategory;

import java.math.BigDecimal;

public record AmountByCategoryResponseDTO(
        TransactionCategory category,
        BigDecimal amount){
}
