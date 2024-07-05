package financial_dashboard.controller;

import financial_dashboard.dto.account.AccountResponseDTO;
import financial_dashboard.model.Account;
import financial_dashboard.model.User;
import financial_dashboard.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class AccountControllerTest {

    //ATRIBUTOS
    @Autowired
    private MockMvc mockMcv;
    @MockBean
    private AccountService service;
    @Autowired
    private JacksonTester<List<AccountResponseDTO>> listAccountResponseDTOTester;
    @Autowired
    private JacksonTester<AccountResponseDTO> accountResponseDTOTester;




    //MÉTODOS DE TESTE
    //GET ALL - RETURN OK
    @Test
    @WithMockUser(roles = "ADMIN")
    public void getAccountsShouldReturn200Ok() throws Exception {
        //Arrange
        var accountDTORomulo = getAccountRomulo();
        var accoountDTOMalaquias = getAccountMalaquias();

        var accounts = List.of(accountDTORomulo, accoountDTOMalaquias);

        when(service.getAccounts()).thenReturn(accounts);

        //Act
        var response = mockMcv.perform(get("/accounts/all"))
                .andReturn().getResponse();

        var accountsJson = listAccountResponseDTOTester.write(accounts).getJson();

        //Assertion
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(response.getContentAsString(), accountsJson);
    }

    @Test
    @WithMockUser
    public void getAccountShouldReturn200Ok() throws Exception {
        //Arrange
        var accountDTORomulo = getAccountRomulo();
        when (service.getAccount(any())).thenReturn(accountDTORomulo);

        //Act
        var response = mockMcv.perform(get("/accounts"))
                .andReturn().getResponse();

        var accountJson = accountResponseDTOTester.write(accountDTORomulo).getJson();

        //Assertion
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(response.getContentAsString(), accountJson);
    }


    //MÉTODOS PRIVADOS
    private AccountResponseDTO getAccountRomulo() {
        var userRomulo =  new User("Romulo", "004.773.170-22",
                "romulo.darde@gmail.com", "Teste@321");
        var account = new Account(userRomulo);
        return new AccountResponseDTO(account.getId(), account.getCurrentBalance(),
                account.getMonthBalance(), account.getUser().getId());
    }

    private AccountResponseDTO getAccountMalaquias() {
        var userMalaquias =  new User("Malaquias", "000.000.000-01",
                "malaquias.guanabara@gmail.com", "Teste@321");
        var account = new Account(userMalaquias);

        return new AccountResponseDTO(account.getId(), account.getCurrentBalance(),
                account.getMonthBalance(), account.getUser().getId());
    }

}
