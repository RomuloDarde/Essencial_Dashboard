package financial_dashboard.service;

import financial_dashboard.model.Account;
import financial_dashboard.model.Transaction;
import financial_dashboard.model.User;
import financial_dashboard.model.enums.TransactionCategory;
import financial_dashboard.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    //ATRIBUTOS
    @InjectMocks
    private AccountService service;
    @Mock
    private AccountRepository repository;


    //MÉTODOS DE TESTE
    //CHECK CURRENT BALANCE
    @Test
    public void checkCurrentBalanceShouldBeSucced() {
        //Arrange
        var account = getAccountRomulo();
        account.setTransactions(new ArrayList<>());

        var transactionReceita = getReceita2000();
        transactionReceita.setRegistrationDate(LocalDate.now());

        var transactionDespesa = getDespesaMercado150();
        transactionDespesa.setRegistrationDate(LocalDate.now());

        account.addTransactionToList(transactionReceita);
        account.addTransactionToList(transactionDespesa);

        //Act
        account.updateCurrentBalance();
        account.updateMonthBalance();

        //Assertion
        assertEquals(account.getCurrentBalance(), BigDecimal.valueOf(1850));
        assertEquals(account.getMonthBalance(), BigDecimal.valueOf(1850));
    }


    //CHECK LIST OF DEBITS
    @Test
    public void checkDebitsListShouldBeSucced() {
        //Arrange
        var account = getAccountRomulo();
        account.setTransactions(new ArrayList<>());

        var debitMercado = getDespesaMercado150();
        var debitRestaurante = getDespesaRestaurante100();

        account.addTransactionToList(debitMercado);
        account.addTransactionToList(debitRestaurante);

        //Act
        var debitsMercado = service.debitsByCategory(
                account.getTransactions(), TransactionCategory.MERCADO);
        var debitsRestaurante = service.debitsByCategory(
                account.getTransactions(), TransactionCategory.RESTAURANTE);
        var debitsMoradia = service.debitsByCategory(
                account.getTransactions(), TransactionCategory.MORADIA);

        //Assertion
        assertTrue(!debitsMercado.isEmpty());
        assertEquals(debitsMercado.get(0),debitMercado);
        assertThrows(IndexOutOfBoundsException.class,
                () -> debitsMercado.get(1));
        assertEquals(debitsRestaurante.get(0).getValue(), debitRestaurante.getValue());
        assertTrue(debitsMoradia.isEmpty());
    }

    //CHECK DEBITS RANK BY CATEGORY
    @Test
    public void checkDebitsRankByCategoryShouldBeSucced() {
        //Arrange
        var account = getAccountRomulo();
        account.setTransactions(new ArrayList<>());

        var debitMercado150 = getDespesaMercado150();
        var debitRestaurante = getDespesaRestaurante100();
        var debitMercado200 = getDespesaMercado200();

        account.addTransactionToList(debitMercado150);
        account.addTransactionToList(debitRestaurante);
        account.addTransactionToList(debitMercado200);

        //Act
        var debitsRankBtCategory = service.rankDebitsByCategory(account.getTransactions());

        //Assertion
        assertEquals(debitsRankBtCategory.get(0).amount(), BigDecimal.valueOf(350));
        assertEquals(debitsRankBtCategory.get(0).category(), TransactionCategory.MERCADO);
        assertEquals(debitsRankBtCategory.get(1).amount(), BigDecimal.valueOf(100));
        assertEquals(debitsRankBtCategory.get(1).category(), TransactionCategory.RESTAURANTE);
        assertThrows(IndexOutOfBoundsException.class,
                () -> debitsRankBtCategory.get(2));
    }



    //MÉTODOS PRIVADOS
    private Account getAccountRomulo() {
        var userRomulo =  new User("Romulo", "004.773.170-22",
                "romulo.darde@gmail.com", "Teste@321");

        userRomulo.addAccount();
        return userRomulo.getAccount();
    }

    private Account getAccountMalaquias() {
        var userMalaquias =  new User("Malaquias", "000.000.000-01",
                "malaquias.guanabara@gmail.com", "Teste@321");

        userMalaquias.addAccount();
        return userMalaquias.getAccount();
    }

    private Transaction getReceita2000() {
        return new Transaction("receita", "salario", BigDecimal.valueOf(2000), "");
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
