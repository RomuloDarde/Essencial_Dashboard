package financial_dashboard.service;

import com.sun.jdi.request.DuplicateRequestException;
import financial_dashboard.dto.financialgoal.FinancialGoalPostRequestDTO;
import financial_dashboard.dto.financialgoal.FinancialGoalResponseDTO;
import financial_dashboard.dto.financialgoal.FinancialGoalUpdateRequestDTO;
import financial_dashboard.exception.createdexceptions.ResourceNotFoundException;
import financial_dashboard.model.FinancialGoal;
import financial_dashboard.model.User;
import financial_dashboard.repository.FinancialGoalRepository;
import financial_dashboard.repository.UserRepository;
import financial_dashboard.security.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FinancialGoalService {

    //ATRIBUTOS
    @Autowired
    private FinancialGoalRepository financialGoalRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenService tokenService;


    //MÉTODOS PRINCIPAIS: GET ALL, GET, POST PUT E DELETE
    //GET ALL - ADMIN
    public List<FinancialGoalResponseDTO> getFinancialGoals() {
        financialGoalRepository.findAll().stream()
                .forEach(goal -> goal.updatePercentageAndValueToComplete());

        return financialGoalRepository.findAll().stream()
                .map(goal -> new FinancialGoalResponseDTO(
                        goal.getId(),
                        goal.getName(),
                        goal.getValue(),
                        goal.getValueToComplete(),
                        goal.getPercentage(),
                        goal.getDescription(),
                        goal.getRegistrationDate(),
                        goal.getUser().getId()))
                .collect(Collectors.toList());
    }

    //GET - USER
    public FinancialGoalResponseDTO getFinancialGoal(HttpServletRequest request) {
        String login = getLoginByToken(request);
        var user =  (User) userRepository.findByEmail(login);

        verifyIfFinancialGoalExists(user);
        var goal = user.getFinancialGoal();
        goal.updatePercentageAndValueToComplete();

        return convertFinancialGoalToDTO(goal);
    }

    //POST - USER
    public ResponseEntity postFinancialGoal(
            FinancialGoalPostRequestDTO dto, HttpServletRequest request) {

        if (dto.value().doubleValue() <= 0.0) {
            throw new IllegalArgumentException("Value must be greater than zero.");
        }

        String login = getLoginByToken(request);
        var user =  (User) userRepository.findByEmail(login);

        if (user.getFinancialGoal() != null) {
        throw new DuplicateRequestException(
                "Only one financial goal is allowed for each user.");
        }

        var financialGoal = new FinancialGoal
                    (dto.name(), dto.value(), dto.description());

        financialGoal.addUser(user);
        financialGoalRepository.save(financialGoal);
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                    .body(convertFinancialGoalToDTO(financialGoal));
    }

    //PUT - USER
    public void updateFinancialGoal(HttpServletRequest request, FinancialGoalUpdateRequestDTO dto) {
        if (dto.value().doubleValue() <= 0.0) {
            throw new IllegalArgumentException("Value must be greater than zero.");
        }

        String login = getLoginByToken(request);
        var user =  (User) userRepository.findByEmail(login);

        verifyIfFinancialGoalExists(user);
        var goal = user.getFinancialGoal();

        goal.updateData(dto);
        financialGoalRepository.save(goal);
    }

    //DELETE - USER
    public void deleteFinancialGoal(HttpServletRequest request) {
        String login = getLoginByToken(request);
        var user =  (User) userRepository.findByEmail(login);

        verifyIfFinancialGoalExists(user);
        var goal = user.getFinancialGoal();

        financialGoalRepository.deleteById(goal.getId());
    }

    //DEMAIS MÉTODOS PARA CONSULTAS
    //GET NOTIFICATION - USER
    public String getNotification(HttpServletRequest request) {
        String login = getLoginByToken(request);
        var user =  (User) userRepository.findByEmail(login);

        verifyIfFinancialGoalExists(user);
        var goal = user.getFinancialGoal();

        goal.updateNotification();
        return goal.getNotification();
    }


    //MÉTODOS PRIVADOS
    //Converter a meta financeira em DTO
    private FinancialGoalResponseDTO convertFinancialGoalToDTO(FinancialGoal goal) {
        return new FinancialGoalResponseDTO(
                goal.getId(),
                goal.getName(),
                goal.getValue(),
                goal.getValueToComplete(),
                goal.getPercentage(),
                goal.getDescription(),
                goal.getRegistrationDate(),
                goal.getUser().getId());
    }

    //Buscar o login pelo token
    private String getLoginByToken(HttpServletRequest request) {
        if (request == null) return null;
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        var token = authHeader.replace("Bearer ", "");

        if (token == null) return null;
        return tokenService.validateToken(token);
    }

    // Verificar se a meta financeira existe para o usuário
    private void verifyIfFinancialGoalExists(User user) {
        if (user.getFinancialGoal() == null) {
            throw new ResourceNotFoundException("There is no financial goal for this user.");
        }
    }
}
