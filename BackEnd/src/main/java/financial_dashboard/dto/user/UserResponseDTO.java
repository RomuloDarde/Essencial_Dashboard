package financial_dashboard.dto.user;

import financial_dashboard.model.enums.UserRole;

import java.time.LocalDate;

public record UserResponseDTO(
        Long id,
        String name,
        String cpf,
        String email,
        String password,
        LocalDate registrationDate,
        UserRole role
        ) {

}
