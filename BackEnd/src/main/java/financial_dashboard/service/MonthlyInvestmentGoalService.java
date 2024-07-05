package financial_dashboard.service;

import com.sun.jdi.request.DuplicateRequestException;
import financial_dashboard.dto.investmentgoal.InvestmentGoalResponseDTO;
import financial_dashboard.dto.investmentgoal.InvestmentGoalRequestDTO;
import financial_dashboard.exception.createdexceptions.ResourceNotFoundException;
import financial_dashboard.model.MonthlyInvestmentGoal;
import financial_dashboard.model.User;
import financial_dashboard.repository.FinancialGoalRepository;
import financial_dashboard.repository.MonthlyInvestmentGoalRepository;
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
public class MonthlyInvestmentGoalService {

    //ATRIBUTOS
    @Autowired
    private MonthlyInvestmentGoalRepository investmentGoalRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FinancialGoalRepository financialGoalRepository;
    @Autowired
    private TokenService tokenService;


    //MÉTODOS PRINCIPAIS: GET ALL, GET, POST PUT E DELETE
    //GET ALL - ADMIN
    public List<InvestmentGoalResponseDTO> getInvestmentGoals() {
        investmentGoalRepository.findAll().stream()
                .forEach(goal -> goal.updateStatus());

        return investmentGoalRepository.findAll().stream()
                .map(goal -> new InvestmentGoalResponseDTO(
                        goal.getId(),
                        goal.getValue(),
                        goal.getStatus(),
                        goal.getRegistrationDate(),
                        goal.getUser().getId()))
                .collect(Collectors.toList());
    }

    //GET - USER
    public InvestmentGoalResponseDTO getInvestmentGoal(HttpServletRequest request) {
        String login = getLoginByToken(request);
        var user = (User) userRepository.findByEmail(login);

        verifyIfInvestmentGoalExists(user);
        var goal = user.getMonthlyInvestmentGoal();
        goal.updateStatus();

        return convertInvestmentGoalToDTO(goal);
    }

    //POST - USER
    public ResponseEntity postInvestmentGoal(
            InvestmentGoalRequestDTO dto,
            HttpServletRequest request) {

        if (dto.value().doubleValue() <= 0.0) {
            throw new IllegalArgumentException("Value must be greater than zero.");
        }

        String login = getLoginByToken(request);
        var user = (User) userRepository.findByEmail(login);

        if (user.getMonthlyInvestmentGoal() != null) {
            throw new DuplicateRequestException(
                    "Only one monthly investment goal is allowed for each user.");
        }
        var investmentGoal = new MonthlyInvestmentGoal(dto.value());

        investmentGoal.addUser(user);
        investmentGoalRepository.save(investmentGoal);
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(convertInvestmentGoalToDTO(investmentGoal));
    }

    //PUT - USER
    public void updateInvestmentGoal(
            HttpServletRequest request, InvestmentGoalRequestDTO dto) {

        if (dto.value().doubleValue() <= 0.0) {
            throw new IllegalArgumentException("Value must be greater than zero.");
        }

        String login = getLoginByToken(request);
        var user = (User) userRepository.findByEmail(login);

        verifyIfInvestmentGoalExists(user);
        var goal = user.getMonthlyInvestmentGoal();

        goal.updateData(dto);
        investmentGoalRepository.save(goal);
    }

    //DELETE - USER
    public void deleteInvestmentGoal(HttpServletRequest request) {
        String login = getLoginByToken(request);
        var user = (User) userRepository.findByEmail(login);

        verifyIfInvestmentGoalExists(user);
        var goal = user.getMonthlyInvestmentGoal();

        investmentGoalRepository.deleteById(goal.getId());
    }


    //DEMAIS MÉTODOS PARA CONSULTAS
    //GET NOTIFICATION - USER
    public String getNotification(HttpServletRequest request) {
        String login = getLoginByToken(request);
        var user = (User) userRepository.findByEmail(login);

        verifyIfInvestmentGoalExists(user);
        var goal = user.getMonthlyInvestmentGoal();

        goal.updateNotification();
        return goal.getNotification();
    }


    //MÉTODOS PRIVADOS
    //Conveter a meta mensal em DTO
    private InvestmentGoalResponseDTO convertInvestmentGoalToDTO(
            MonthlyInvestmentGoal goal) {
        return new InvestmentGoalResponseDTO(
                goal.getId(),
                goal.getValue(),
                goal.getStatus(),
                goal.getRegistrationDate(),
                goal.getUser().getId());
    }

    //Buscar o Login pelo Token
    private String getLoginByToken(HttpServletRequest request) {
        if (request == null) return null;
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        var token = authHeader.replace("Bearer ", "");

        if (token == null) return null;
        return tokenService.validateToken(token);
    }

    //Verificar se a meta de investimento exixte para o usuário
    private void verifyIfInvestmentGoalExists(User user) {
        if (user.getMonthlyInvestmentGoal() == null) {
            throw new ResourceNotFoundException("There is no investment goal for this user.");
        }
    }
}
