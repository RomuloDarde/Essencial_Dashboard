package financial_dashboard.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record UserUpdateRequestDTO(
        String name,

        @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "Invalid CPF format.")
        //@CPF
        String cpf,

        @Email(message = "Invalid email format.")
        String email,

        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
                message = "The password must contain at least 8 characters, " +
                        "including at least one lowercase letter, " +
                        "one uppercase letter, one number and one special character.")
        String password
) {
}
