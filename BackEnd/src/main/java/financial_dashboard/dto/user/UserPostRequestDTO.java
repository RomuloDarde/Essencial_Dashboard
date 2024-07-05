package financial_dashboard.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserPostRequestDTO(
        @NotBlank (message = "O campo do nome não deve estar vazio.")
        String name,

        @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message =
                "Cpf inválido, deve ser neste formato: ddd.ddd.ddd-dd.")
        //@CPF
        String cpf,

        @Email(message = "Formato de e-mail inválido.")
        String email,

        @NotBlank (message = "O campo da senha não deve estar vazio.")
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
                message = "A senha deve ter ao menos 8 caracteres, " +
                        "incluindo ao menos uma letra minúscula, " +
                        "uma maiúscula, um número e um caractere especial.")
        String password) {
}
