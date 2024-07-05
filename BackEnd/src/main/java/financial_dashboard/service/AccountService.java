package financial_dashboard.service;

import financial_dashboard.dto.account.AccountResponseDTO;
import financial_dashboard.dto.transaction.AmountByCategoryResponseDTO;
import financial_dashboard.model.Account;
import financial_dashboard.model.Transaction;
import financial_dashboard.model.User;
import financial_dashboard.model.enums.TransactionCategory;
import financial_dashboard.model.enums.TransactionType;
import financial_dashboard.repository.AccountRepository;
import financial_dashboard.repository.UserRepository;
import financial_dashboard.security.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {

    //ATRIBUTOS
    @Autowired
    private AccountRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenService tokenService;


    //MÉTODOS PRINCIPAIS - GET ALL E GET
    //GET ALL - ADMIN
    public List<AccountResponseDTO> getAccounts() {
        repository.findAll().stream()
                .forEach(account -> account.updateCurrentBalance());

        repository.findAll().stream()
                .forEach(account -> account.updateMonthBalance());

        return repository.findAll().stream()
                .map(account -> new AccountResponseDTO(
                        account.getId(),
                        account.getCurrentBalance(),
                        account.getMonthBalance(),
                        account.getUser().getId()))
                .collect(Collectors.toList());
    }

    //GET - USER
    public AccountResponseDTO getAccount(HttpServletRequest request) {
        var account = getAccountByToken(request);
        account.updateCurrentBalance();
        account.updateMonthBalance();

        return new AccountResponseDTO(
                account.getId(),
                account.getCurrentBalance(),
                account.getMonthBalance(),
                account.getUser().getId());
    }

    //DEMAIS MÉTODOS PARA CONSULTAS
    //GET NOTIFICATION - USER
    public String getNotification (HttpServletRequest request) {
        var account = getAccountByToken(request);
        account.updateCurrentBalance();
        account.updateMonthBalance();

        account.updateNotification();
        return account.getNotification();
    }

    ////////////////////////////////////////////////////////////////////////////////////

    //MÉTODOS PRIVADOS
    //Buscar a conta pelo token
    private Account getAccountByToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        var token = authHeader.replace("Bearer ", "");

        if (token == null) return null;
        var login = tokenService.validateToken(token);
        User user =  (User) userRepository.findByEmail(login);
        return user.getAccount();
    }

    //Listar débitos por categoria
    public List<Transaction> debitsByCategory(
            List<Transaction> transactions, TransactionCategory category) {
        return transactions.stream()
                .filter(t -> t.getType().equals(TransactionType.DESPESA))
                .filter(t -> t.getCategory().equals(category))
                .collect(Collectors.toList());
    }

    //Lista um ranking de débitos por categorias
    public List<AmountByCategoryResponseDTO> rankDebitsByCategory(
            List<Transaction> transactions) {
        List<AmountByCategoryResponseDTO> dtoList = new ArrayList<>();
        List<TransactionCategory> categorys = Arrays.asList(TransactionCategory.values());

        categorys.stream().forEach(c -> {
            var debitsByCategory = debitsByCategory(transactions, c);
            var value = BigDecimal.ZERO;
            for (Transaction transaction : debitsByCategory) {
                value = value.add(transaction.getValue());
            }
            if (!value.equals(BigDecimal.ZERO))
                dtoList.add(new AmountByCategoryResponseDTO(c, value));
        });

        return dtoList.stream()
                .sorted(Comparator.comparing(AmountByCategoryResponseDTO::amount).reversed())
                .collect(Collectors.toList());
    }

}