package financial_dashboard.model;

import financial_dashboard.dto.user.UserUpdateRequestDTO;
import financial_dashboard.model.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;


@NoArgsConstructor
@Data
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String cpf;

    @Column(unique = true)
    private String email;

    private String password;

    @CreationTimestamp
    private LocalDate registrationDate;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Account account;
    //mapped By identifica o atributo (pelo nome) na entidade relacionada.
    //CascadeType.ALL permite que a entidade relacionada seja persistida
    // no banco de dados junto com essa entidade.

    @OneToOne(mappedBy = "user")
    private FinancialGoal financialGoal;

    @OneToOne(mappedBy = "user")
    private MonthlyInvestmentGoal monthlyInvestmentGoal;

    @Enumerated(EnumType.STRING)
    private UserRole role;


    //Construtor sem id, registrationDate e account
    public User(String name, String cpf, String email, String password) {
        this.name = name;
        this.cpf = cpf;
        this.email = email;
        this.password = password;
        this.role = UserRole.USER;
    }


    //Métodos
    //Método para adicionar uma conta ao usuário
    public void addAccount() {
        this.account = new Account(this);
 }

    //Método para atualizar dados cadastrais
    public void updateData(UserUpdateRequestDTO dto) {
        if (dto.name() != null) this.name = dto.name();
        if (dto.email() != null) this.email = dto.email();
        if (dto.cpf() != null) this.cpf = dto.cpf();
        if (dto.password() != null) {
            String encryptedPassword = new BCryptPasswordEncoder().encode(dto.password());
            this.password = encryptedPassword;
        }
    }


    //Métodos do Spring Security
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //retorna uma lista com as "roles" do usuário, gerenciando as suas autorizações.

        if (this.role == UserRole.ADMIN) {
            return List.of(
                    new SimpleGrantedAuthority("ROLE_ADMIN"),
                    new SimpleGrantedAuthority("ROLE_USER"));
            } else {
            return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    //Esses métodos abaixo, a princípio todos retornarão true
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
