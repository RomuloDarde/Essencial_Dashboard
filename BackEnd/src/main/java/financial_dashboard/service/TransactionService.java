package financial_dashboard.service;

import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import financial_dashboard.dto.transaction.AmountByCategoryResponseDTO;
import financial_dashboard.dto.transaction.TransactionRequestDTO;
import financial_dashboard.dto.transaction.TransactionResponseDTO;
import financial_dashboard.exception.createdexceptions.ResourceNotFoundException;
import financial_dashboard.model.Account;
import financial_dashboard.model.Transaction;
import financial_dashboard.model.User;
import financial_dashboard.model.enums.TransactionCategory;
import financial_dashboard.model.enums.TransactionType;
import financial_dashboard.repository.AccountRepository;
import financial_dashboard.repository.TransactionRepository;
import financial_dashboard.repository.UserRepository;
import financial_dashboard.security.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    //ATRIBUTOS
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenService tokenService;
    private MailService mailService = new MailService();


    //MÉTODOS PRINCIPAIS - GET ALL, (ADMIN), GET ALL (USER), GET BY ID, POST, PUT E DELETE
    //GET ALL - ADMIN
    public List<TransactionResponseDTO> getTransactions() {
        return transactionRepository.findAll().stream()
                .map(transaction -> new TransactionResponseDTO(
                        transaction.getId(),
                        transaction.getType(),
                        transaction.getCategory(),
                        transaction.getValue(),
                        transaction.getDescription(),
                        transaction.getRegistrationDate(),
                        transaction.getAccount().getId(),
                        transaction.getAccount().getUser().getId()))
                .collect(Collectors.toList());
    }


    //GET ALL - USER
    public List<TransactionResponseDTO> getTransactionsByAccount(HttpServletRequest request) {
        String login = getLoginByRequest(request);
        var account = getAccountByLogin(login);

        return convertListTransactionToListDTO(account.getTransactions());
    }


    //GET BY ID - USER
    public TransactionResponseDTO getTransactionById(
            Long id, HttpServletRequest request) {
        verifyIfTransactionExists(id);
        var transaction = transactionRepository.findById(id).get();

        String login = getLoginByRequest(request);
        var account = getAccountByLogin(login);

        if (transaction.getAccount().getId().equals(account.getId())) {
            return convertTransactionToDTO(transaction);

        }else throw new IllegalArgumentException(
                "This transaction does not belong to the account of the user logged.");
    }


    //POST - USER
    public ResponseEntity postTransaction(
            TransactionRequestDTO dto, HttpServletRequest request) {

        if (dto.value().doubleValue() <= 0.0) {
            throw new IllegalArgumentException("Value must be greater than zero.");
        }

        String login = getLoginByRequest(request);
        var account = getAccountByLogin(login);

        var transaction = new Transaction(
                dto.type(), dto.category(), dto.value(), dto.description());

        transaction.addAccount(account);
        transactionRepository.save(transaction);

        account.updateCurrentBalance();
        account.updateMonthBalance();
        account.updateNotification();
        accountRepository.save(account);

        var balance = account.getCurrentBalance();
        var monthBalance = account.getMonthBalance();
        var investmentGoal = account.getUser()
                .getMonthlyInvestmentGoal();
        var financialGoal = account.getUser().getFinancialGoal();

        //Email de notificação do saldo negativo da conta
        if (transaction.getType().equals(TransactionType.DESPESA)) {
            if (balance.doubleValue() < 0) {
                try {
                    mailService.negativeBalanceMail(account.getUser(), account);
                } catch (MailjetException e) {
                    throw new RuntimeException(e);
                } catch (MailjetSocketTimeoutException e) {
                    throw new RuntimeException(e);
                }
            }

            //Email de notificação se não estiver cumprindo a meta de investimento
            if (investmentGoal != null && financialGoal == null &&
                    LocalDate.now().getDayOfMonth() > 15) {
                if (monthBalance.doubleValue() < investmentGoal.getValue().doubleValue()) {
                    account.getUser().getMonthlyInvestmentGoal().updateNotification();

                    try {
                        mailService.investmentGoalNotificationMail(account.getUser());
                    } catch (MailjetException e) {
                        throw new RuntimeException(e);
                    } catch (MailjetSocketTimeoutException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            //Email de notificação se não estiver progredindo na meta financeira
            if (financialGoal != null && LocalDate.now().getDayOfMonth() > 15) {
                var investmentGoalValue = 0.0;
                account.getUser().getFinancialGoal().updateNotification();
                if (investmentGoal != null) {
                    investmentGoalValue = investmentGoal.getValue().doubleValue();
                }
                if (monthBalance.doubleValue() <= investmentGoalValue) {
                     try {
                        mailService.financialGoalNotificationMail(account.getUser());
                    } catch (MailjetException e) {
                        throw new RuntimeException(e);
                    } catch (MailjetSocketTimeoutException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(convertTransactionToDTO(transaction));
    }


    //PUT - USER
    public void updateTransaction(
            Long id, TransactionRequestDTO dto, HttpServletRequest request) {
        
        if (dto.value().doubleValue() <= 0.0) {
            throw new IllegalArgumentException("Value must be greater than zero.");
        }

        verifyIfTransactionExists(id);
        var transaction = transactionRepository.findById(id).get();

        String login = getLoginByRequest(request);
        var account = getAccountByLogin(login);

        if (transaction.getAccount().getId().equals(account.getId())) {
            transaction.updateData(dto);
            transactionRepository.save(transaction);

            account.updateCurrentBalance();
            account.updateMonthBalance();
            accountRepository.save(account);

        } else throw new IllegalArgumentException(
                "This transaction does not belong to the account of the user logged.");
    }


    //DELETE - USER
    public void deleteTransactionById(
            Long id, HttpServletRequest request) {

        verifyIfTransactionExists(id);
        var transaction = transactionRepository.findById(id).get();

        String login = getLoginByRequest(request);
        var account = getAccountByLogin(login);

        if (transaction.getAccount().getId().equals(account.getId())) {
            transactionRepository.deleteById(id);

            account.updateCurrentBalance();
            account.updateMonthBalance();
            accountRepository.save(account);

        } else throw new IllegalArgumentException(
                "This transaction does not belong to the account of the user logged.");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    //DEMAIS MÉTODOS PARA CONSULTAS
    //DEBITS BY CATEGORY - USER
    public List<TransactionResponseDTO> getDebitsByStringCategory(
            HttpServletRequest request, String stringCategory) {
        try {
            TransactionCategory.fromString(stringCategory);
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException(
                    "No category found for the given String: " + stringCategory);
        }
        String login = getLoginByRequest(request);
        var account = getAccountByLogin(login);

        var transactions = account.getTransactions();
        var category = TransactionCategory.fromString(stringCategory);
        var debitsByCategory = debitsByCategory(transactions, category);
        return convertListTransactionToListDTO(debitsByCategory);
    }

    //DEBITS RANK BY CATEGORY - USER
    public List<AmountByCategoryResponseDTO> getExpensesRankByCategory(
            HttpServletRequest request) {
        String login = getLoginByRequest(request);
        var account = getAccountByLogin(login);

        List<Transaction> transactions = account.getTransactions();
        return rankDebitsByCategory(transactions);
    }

    //DEBITS RANK BY CATEGORY BY MONTH - USER
    public List<AmountByCategoryResponseDTO> getExpensesRankByCategoryByMonth(
            HttpServletRequest request, Integer integerMonth) {

        String login = getLoginByRequest(request);
        var account = getAccountByLogin(login);

        Month month = Month.of(integerMonth);

        List<Transaction> transactionsAtCurrentMonth = account.getTransactions().stream()
                .filter(transaction -> transaction.getRegistrationDate().getMonth().equals(month))
                .collect(Collectors.toList());

        return rankDebitsByCategory(transactionsAtCurrentMonth);
    }

    //AT CURRENT MONTH - USER
    public List<TransactionResponseDTO> getTransactionsAtCurrentMonth(HttpServletRequest request) {
        String login = getLoginByRequest(request);
        var account = getAccountByLogin(login);

        List<Transaction> transactionsAtCurrentMonth = account.getTansactionsAtCurrentMonth();
        return convertListTransactionToListDTO(transactionsAtCurrentMonth);
    }

    //BY MONTH - USER
    public List<TransactionResponseDTO> getTransactionsByMonth(
            HttpServletRequest request, Integer integerMonth) {

        String login = getLoginByRequest(request);
        var account = getAccountByLogin(login);

        Month month = Month.of(integerMonth);

        List<Transaction> transactionsByMonth = account.getTransactions().stream()
                .filter(transaction -> transaction.getRegistrationDate().getMonth().equals(month))
                .collect(Collectors.toList());

        return convertListTransactionToListDTO(transactionsByMonth);
    }

    //BY PERIOD - USER
    public List<TransactionResponseDTO> getTransactionsByPeriod(
            HttpServletRequest request, String stringInitialDate, String stringFinalDate) {

        String login = getLoginByRequest(request);
        var account = getAccountByLogin(login);

        String[] initialDateParts = stringInitialDate.split("/");
        String initialDateDay = initialDateParts[0];
        String initialDateMonth = initialDateParts[1];
        String initialDateYear = initialDateParts[2];
        var formattedStringInitialDate = initialDateYear + "-" + initialDateMonth + "-" + initialDateDay;

        String[] finalDateParts = stringFinalDate.split("/");
        String finalDateDay = finalDateParts[0];
        String finalDateMonth = finalDateParts[1];
        String finalDateYear = finalDateParts[2];
        var formattedStringFinalDate = finalDateYear + "-" + finalDateMonth + "-" + finalDateDay;

        var initialDate = LocalDate.parse(formattedStringInitialDate);
        var finalDate = LocalDate.parse(formattedStringFinalDate);

        List<Transaction> transactionsByPeriod = account.getTransactions().stream()
                .filter(transaction ->
                        (transaction.getRegistrationDate().isBefore(finalDate) ||
                                transaction.getRegistrationDate().equals(finalDate)) &&
                                (transaction.getRegistrationDate().isAfter(initialDate) ||
                                        transaction.getRegistrationDate().equals(initialDate)))
                .collect(Collectors.toList());

        return convertListTransactionToListDTO(transactionsByPeriod);
    }


    //GET CATEGORIES - USER
    public List<String> getCategories() {
        List<String> stringCategories = Arrays.stream(TransactionCategory.values())
                .map(e -> e.name().replace("_", " "))
                .collect(Collectors.toList());
        return stringCategories;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    //MÉTODOS PRIVADOS
    //Buscar o Login pelo Request
   private String getLoginByRequest(HttpServletRequest request) {
        if (request == null) return null;
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        String token = authHeader.replace("Bearer ", "");
        if (token == null) return null;
        return tokenService.validateToken(token);
    }

    //Buscar a conta pelo Login
    private Account getAccountByLogin(String login) {
        User user = (User) userRepository.findByEmail(login);
        return user.getAccount();
    }

    //Verificar se a transação existe
    private void verifyIfTransactionExists(Long id) {
        if (transactionRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException
                    ("There is no transaction with that ID number: " + id + ".");
        }
    }

    //converter uma transação em DTO
    private TransactionResponseDTO convertTransactionToDTO(Transaction transaction) {
        return new TransactionResponseDTO(
                transaction.getId(),
                transaction.getType(),
                transaction.getCategory(),
                transaction.getValue(),
                transaction.getDescription(),
                transaction.getRegistrationDate(),
                transaction.getAccount().getId(),
                transaction.getAccount().getUser().getId());
    }

    //Converter uma lista de Transações em Uma lista de DTOs
    private List<TransactionResponseDTO> convertListTransactionToListDTO(List<Transaction> transactions) {
        return transactions.stream()
                .map(transaction -> new TransactionResponseDTO(
                        transaction.getId(),
                        transaction.getType(),
                        transaction.getCategory(),
                        transaction.getValue(),
                        transaction.getDescription(),
                        transaction.getRegistrationDate(),
                        transaction.getAccount().getId(),
                        transaction.getAccount().getUser().getId()))
                .collect(Collectors.toList());
    }

    //Listar débitos por categoria
    private List<Transaction> debitsByCategory(
            List<Transaction> transactions, TransactionCategory category) {
        return transactions.stream()
                .filter(t -> t.getType().equals(TransactionType.DESPESA))
                .filter(t -> t.getCategory().equals(category))
                .collect(Collectors.toList());
    }

    //Lista um ranking de débitos por categorias
    private List<AmountByCategoryResponseDTO> rankDebitsByCategory(
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
