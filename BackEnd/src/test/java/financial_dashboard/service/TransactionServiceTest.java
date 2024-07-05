package financial_dashboard.service;

import financial_dashboard.model.Transaction;
import financial_dashboard.model.User;
import financial_dashboard.repository.TransactionRepository;
import financial_dashboard.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    //ATRIBUTOS
    @InjectMocks
    private TransactionService service;
    @Mock
    private TransactionRepository repository;
    @Mock
    private UserRepository userRepository;


    //MÉTODOS DE TESTE
    //GET TRANSACTION BY ID
    @Test
    public void getTransactionByIdShouldBeSucced() {
        var user =  getUserRomulo();
        user.setId(1l);
        user.addAccount();

        var account = user.getAccount();
        account.setId(1l);
        account.setTransactions(new ArrayList<>());

        var transaction = getDespesaMercado150();
        transaction.addAccount(account);

        Long id = 1l;

        when(repository.findById(id)).thenReturn(Optional.of(transaction));
        when(userRepository.findByEmail(any())).thenReturn(user);

        var responseDTO = service.getTransactionById(id, any());

        assertEquals(responseDTO.accountId(), transaction.getAccount().getId());
        assertEquals(responseDTO.value(), transaction.getValue());
        assertEquals(responseDTO.userId(), 1l);
    }


    //GET TRANSACTIONS AT CURRENT MONTH
    @Test
    public void getTransactionsAtCurrentMonthShouldBeSucced() {
        //Arrange
        var user = getUserRomulo();
        user.addAccount();

        var account  = user.getAccount();
        account.setTransactions(new ArrayList<>());

        var mercado150 = getDespesaMercado150();
        mercado150.setRegistrationDate(LocalDate.now());
        mercado150.addAccount(account);

        var mercado200 = getDespesaMercado200();
        mercado200.setRegistrationDate(LocalDate.now());
        mercado200.addAccount(account);

        var restaurante100 = getDespesaRestaurante100();
        restaurante100.setRegistrationDate(LocalDate.now());
        restaurante100.addAccount(account);

        when(userRepository.findByEmail(any())).thenReturn(user);

        //Act
        var responseListDTO = service.getTransactionsAtCurrentMonth(any());

        //Assertion
        assertTrue(!responseListDTO.isEmpty());
        assertEquals(responseListDTO.get(0).value(), mercado150.getValue());
        assertEquals(responseListDTO.get(1).value(), mercado200.getValue());
        assertEquals(responseListDTO.get(2).value(), restaurante100.getValue());
        assertEquals(responseListDTO.size(), 3);
        assertThrows(IndexOutOfBoundsException.class,
                () -> responseListDTO.get(3));
    }


    //GET TRANSACTIONS BY MONTH
    @Test
    public void getTransactionsByMonthShouldBeSucced() {
        //Arrange
        var user = getUserRomulo();
        user.addAccount();

        var account  = user.getAccount();
        account.setTransactions(new ArrayList<>());

        var mercado150 = getDespesaMercado150();
        mercado150.setRegistrationDate(LocalDate.now());
        mercado150.addAccount(account);

        var mercado200 = getDespesaMercado200();
        mercado200.setRegistrationDate(LocalDate.now());
        mercado200.addAccount(account);

        var restaurante100 = getDespesaRestaurante100();
        restaurante100.setRegistrationDate(LocalDate.now());
        restaurante100.addAccount(account);

        when(userRepository.findByEmail(any())).thenReturn(user);

        var correctMonth = LocalDate.now().getMonth().getValue();

        int wrongMonth = 0;
        if (correctMonth == 12) wrongMonth = 11;
        else wrongMonth = correctMonth + 1;

        //Act
        var responseDTOEmpty= service.getTransactionsByMonth(any(), (wrongMonth));
        var responseDTOSucced = service.getTransactionsByMonth(any(), (correctMonth));

        //Assertion
        assertTrue(responseDTOEmpty.isEmpty());
        assertEquals(responseDTOSucced.get(0).value(), mercado150.getValue());
        assertEquals(responseDTOSucced.get(1).value(), mercado200.getValue());
        assertEquals(responseDTOSucced.get(2).value(), restaurante100.getValue());
        assertEquals(responseDTOSucced.size(), 3);
        assertThrows(DateTimeException.class,
                () -> service.getTransactionsByMonth(any(), 13));
    }







    //MÉTODOS PRIVADOS
    private User getUserRomulo() {
        var userRomulo =  new User("Romulo", "004.773.170-22",
                "romulo.darde@gmail.com", "Teste@321");
        return userRomulo;
    }

    private Transaction getDespesaMercado150() {
        return new Transaction("despesa", "mercado", BigDecimal.valueOf(150), "");
    }

    private Transaction getDespesaMercado200() {
        return new Transaction("despesa", "mercado", BigDecimal.valueOf(200), "");
    }

    private Transaction getDespesaRestaurante100() {
        return new Transaction("despesa", "restaurante", BigDecimal.valueOf(100), "");
    }
}
