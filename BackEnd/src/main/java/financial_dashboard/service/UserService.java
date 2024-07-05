package financial_dashboard.service;

import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import financial_dashboard.dto.user.*;
import financial_dashboard.exception.createdexceptions.ResourceNotFoundException;
import financial_dashboard.model.User;
import financial_dashboard.repository.UserRepository;
import financial_dashboard.security.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    //ATRIBUTOS
    @Autowired
    private UserRepository repository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;


    //MÉTODOS PRINCIPAIS: LOGIN, GET ALL, GET (PERFIL), POST PUT E DELETE
    //LOGIN - LIVRE
    public ResponseEntity login (AuthenticationDTO dto) {
        var usernamePassword =
                new UsernamePasswordAuthenticationToken(dto.login(), dto.password());

        var authentication = authenticationManager.authenticate(usernamePassword);
        User user = (User) authentication.getPrincipal();

        var token = tokenService.generateToken(user);
        var loginDTO = new LoginResponseDTO(token);
        return ResponseEntity.ok(loginDTO);
    }

    //GET ALL - ADMIN
    public List<UserResponseDTO> getUsers() {
        return repository.findAll().stream()
                .map(user -> new UserResponseDTO(
                        user.getId(),
                        user.getName(),
                        user.getCpf(),
                        user.getEmail(),
                        user.getPassword(),
                        user.getRegistrationDate(),
                        user.getRole()))
                .collect(Collectors.toList());
    }

    //GET (PERFIL) - USER
    public UserResponseDTO getProfile(HttpServletRequest request) {
        var user = getUserByToken(request);
        return convertUserToDTO(user);
    }

    //POST - LIVRE
    @Transactional
    public ResponseEntity postUser(UserPostRequestDTO dto) {
        String encryptedPassword = new BCryptPasswordEncoder().encode(dto.password());
        var user = new User(dto.name(), dto.cpf(), dto.email(), encryptedPassword);
        user.addAccount();
        repository.save(user);

        try {
            new MailService().welcomeMail(user);
        } catch (MailjetException e) {
            throw new RuntimeException(e);
        } catch (MailjetSocketTimeoutException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(convertUserToDTO(user));
    }

    //PUT (PERFIL) - USER
    public void updateProfile(HttpServletRequest request, UserUpdateRequestDTO dto) {
        var user = getUserByToken(request);
        user.updateData(dto);
        repository.save(user);
    }

    //DELETE - ADMIN
    public void deleteUserById(Long id) {
        verifyIfUserExists(id);
        repository.deleteById(id);
    }


    //Métodos privados
    //Verificar se o usuário existe
    private void verifyIfUserExists(Long id) {
        if(repository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException
                    ("There is no user with that ID number: " + id + ".");
        }
    }

    //Verificar se a meta financeira existe para o usuário
    private void verifyIfFinancialGoalExists(User user){
        if(user.getFinancialGoal() == null) {
            throw new ResourceNotFoundException
                    ("There is no financial goal for this user.");
        }
    }

    //Verificar se a meta financeira existe para o usuário
    private void verifyIfInvestmentGoalExists(User user){
        if(user.getMonthlyInvestmentGoal() == null) {
            throw new ResourceNotFoundException
                    ("There is no investment goal for this user.");
        }
    }

    //Converter usuário em DTO
    private UserResponseDTO convertUserToDTO (User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getCpf(),
                user.getEmail(),
                user.getPassword(),
                user.getRegistrationDate(),
                user.getRole());
    }

    //Buscar o usuário pelo token
    private User getUserByToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        var token = authHeader.replace("Bearer ", "");

        if (token == null) return null;
        var login = tokenService.validateToken(token);
        return (User) repository.findByEmail(login);
    }


}
