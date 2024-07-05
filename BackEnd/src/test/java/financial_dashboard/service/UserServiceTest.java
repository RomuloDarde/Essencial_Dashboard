package financial_dashboard.service;


import financial_dashboard.dto.user.UserPostRequestDTO;
import financial_dashboard.dto.user.UserResponseDTO;
import financial_dashboard.dto.user.UserUpdateRequestDTO;
import financial_dashboard.model.User;
import financial_dashboard.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    //ATRIBUTOS
    @InjectMocks
    private UserService service;
    @Mock
    private UserRepository repository;
    @Mock
    private JavaMailSender mailSender;


    //MÉTODOS DE TESTES
    //GET ALL
    @Test
    public void getUsersShouldReturn200Ok() {
        //Arrange
        var userRomulo = getUserRomulo();
        var userMalaquias = getUserMalaquias();
        List<User> mockUsers = List.of(userRomulo, userMalaquias);
        when(repository.findAll()).thenReturn(mockUsers);

        //Act
        var dtoUsers = service.getUsers();

        //Assert
        for (int i = 0; i < mockUsers.size(); i++) {
            assertTrue(dtoUsers.get(i).name().equals(mockUsers.get(i).getName()));
            assertTrue(dtoUsers.get(i).cpf().equals(mockUsers.get(i).getCpf()));
            assertTrue(dtoUsers.get(i).email().equals(mockUsers.get(i).getEmail()));
            assertTrue(dtoUsers.get(i).password().equals(mockUsers.get(i).getPassword()));
        }
    }


    //POST
    @Test
    public void postUserShouldReturn201Created() {
        var requestDTO = new UserPostRequestDTO("Romulo", "004.773.170-22",
                "romulo.darde@gmail.com", "Teste@321");

        var response = service.postUser(requestDTO);
        var responseDTO = (UserResponseDTO) response.getBody();

        assertTrue(response.getStatusCode().equals(HttpStatus.CREATED));
        assertEquals(responseDTO.name(), requestDTO.name());
        assertEquals(responseDTO.cpf(), requestDTO.cpf());
        assertEquals(responseDTO.email(), requestDTO.email());
    }


    //CREATE ACCOUNT WHEN POST USER
    @Test
    public void createAccountWhenPostUserShouldBeSucced() {
        var user = getUserRomulo();
        user.addAccount();
        var account = user.getAccount();

        assertEquals(account.getUser().getId(), user.getId());
        assertEquals(account.getCurrentBalance(), BigDecimal.ZERO);
    }

    //UPDATE EMAIL AT USER
    @Test
    public void updateEmailAtUserShouldBeSucced() {
        var user = getUserRomulo();
        var dtoUser = new UserUpdateRequestDTO(null, null, "romulo.schmidt@zallpy.com", null);
        user.updateData(dtoUser);

        assertEquals(user.getName(), "Romulo");
        assertEquals(user.getCpf(), "004.773.170-22");
        assertEquals(user.getEmail(),dtoUser.email());
        assertEquals(user.getPassword(), "Teste@321");
    }




    //MÉTODOS PRIVADOS
    private User getUserRomulo() {
        return new User("Romulo", "004.773.170-22",
                "romulo.darde@gmail.com", "Teste@321");
    }

    private User getUserMalaquias() {
        return new User("Malaquias", "000.000.000-01",
                "malaquias.guanabara@gmail.com", "Teste@321");
    }
}
