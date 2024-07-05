package financial_dashboard.dto.transaction;

import financial_dashboard.model.enums.TransactionCategory;
import financial_dashboard.model.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionResponseDTO
        (Long id,
         TransactionType type,
         TransactionCategory category,
         BigDecimal value,
         String description,
         LocalDate registrationDate,
         Long accountId,
         Long userId){
}
