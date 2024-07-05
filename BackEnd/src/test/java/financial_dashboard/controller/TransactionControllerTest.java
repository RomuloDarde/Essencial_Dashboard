package financial_dashboard.controller;

import financial_dashboard.dto.transaction.TransactionRequestDTO;
import financial_dashboard.dto.transaction.TransactionResponseDTO;
import financial_dashboard.model.Account;
import financial_dashboard.model.Transaction;
import financial_dashboard.model.User;
import financial_dashboard.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class TransactionControllerTest {

    //ATRIBUTOS
    @Autowired
    private MockMvc mockMcv;
    @MockBean
    private TransactionService service;
    @Autowired
    private JacksonTester<TransactionResponseDTO> responseDTOTester;
    @Autowired
    private JacksonTester<TransactionRequestDTO> requestDTOTester;

    //MÉTODOS DE TESTE
    //GET TRANSACTION BY ID
    @Test
    @WithMockUser
    public void getTransactionsByIdShouldReturn200Ok() throws Exception {
        //Arrange
        var transactionDTO = getResponseDTO();
        when(service.getTransactionById(any(), any())).thenReturn(transactionDTO);

        //Act
        var response = mockMcv.perform(get("/transactions/1")).andReturn().getResponse();
        var transactionJson = responseDTOTester.write(transactionDTO).getJson();

        //Assetion
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(response.getContentAsString(), transactionJson);
    }

    //POST TRANSACTION
    @Test
    @WithMockUser
    public void postTransactionsShouldReturn201Created() throws Exception {
        var requestDTO = new TransactionRequestDTO("despesa", "mercado",
                BigDecimal.valueOf(150), "");

        when(service.postTransaction(any(), any())).thenReturn(
                ResponseEntity.status(HttpStatus.CREATED).body(getResponseDTO()));

        var response = mockMcv.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestDTOTester.write(requestDTO).getJson()))
                .andReturn().getResponse();

        var responseJson = responseDTOTester.write(getResponseDTO()).getJson();

        assertEquals(response.getStatus(), HttpStatus.CREATED.value());
        assertEquals(response.getContentAsString(), responseJson);
    }



    //MÉTODOS PRIVADOS
    private Account getAccountRomulo() {
        var userRomulo =  new User("Romulo", "004.773.170-22",
                "romulo.darde@gmail.com", "Teste@321");
        userRomulo.addAccount();
        return userRomulo.getAccount();
    }

    private Transaction getTransaction() {
        return new Transaction("despesa", "mercado", BigDecimal.valueOf(150), "");
    }

    private TransactionResponseDTO getResponseDTO() {
        var account = getAccountRomulo();
        account.setTransactions(new ArrayList<>());

        var transaction = getTransaction();
        transaction.addAccount(account);

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
}
