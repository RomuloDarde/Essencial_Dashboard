package financial_dashboard.controller;

import financial_dashboard.dto.user.UserPostRequestDTO;
import financial_dashboard.dto.user.UserResponseDTO;
import financial_dashboard.model.enums.UserRole;
import financial_dashboard.service.UserService;
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

import java.time.LocalDate;
import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class UserControllerTest {

    //Atributos
    @Autowired
    private MockMvc mockMcv;
    @Autowired
    private JacksonTester<UserPostRequestDTO> userRequestDTOTester;
    @Autowired
    private JacksonTester<UserResponseDTO> userResponseDTOTester;
    @Autowired
    private JacksonTester<List<UserResponseDTO>> listUserResponseDTOTester;
    @MockBean
    private UserService service;



    //MÉTODOS DE TESTE
    //GET ALL - RETURN OK
    @Test
    @WithMockUser(roles = "ADMIN")
    public void getUsersShouldReturn200Ok() throws Exception {
        var userDTO = getUserResponseDTO();
        var users = List.of(userDTO);

        when(service.getUsers()).thenReturn(users);

        var response = mockMcv.perform(get("/users"))
                .andReturn().getResponse();

        var usersJson = listUserResponseDTOTester.write(users).getJson();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertEquals(response.getContentAsString(), usersJson);
    }

    //GET USER - RETURN OK
    @Test
    @WithMockUser
    public void getUserShouldReturn200Ok() throws Exception {
        var userDTO = getUserResponseDTO();
        when(service.getProfile(any())).thenReturn(userDTO);

        var response = mockMcv.perform(get("/users/profile"))
                .andReturn().getResponse();

        var jsonUserDTO = userResponseDTOTester.write(userDTO).getJson();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString().equals(jsonUserDTO));
    }


    //POST - RETURN BAD REQUEST
    @Test
    public void postUserShouldReturn400BadRequest() throws Exception {
        var response = mockMcv.perform(post("/users"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    //POST - RETURN OK
    @Test
    public void postUserShouldReturn201Created() throws Exception {

        when(service.postUser(any())).thenReturn(
                ResponseEntity.status(HttpStatus.CREATED).body(getUserResponseDTO()));

        var response = mockMcv.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRequestDTOTester.write(getUserRequestDTO()).getJson()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());

        var jsonResponse = userResponseDTOTester.write(getUserResponseDTO()).getJson();
        assertEquals(response.getContentAsString(),jsonResponse);
    }


    //MÉTODOS PRIVADOS
    private UserResponseDTO getUserResponseDTO()  {
        return new UserResponseDTO(
                        null,
                        "Romulo",
                        "004.773.170-22",
                        "romulo.darde@gmail.com",
                        "Teste@321",
                        LocalDate.now(),
                        UserRole.USER);
    }

    private UserPostRequestDTO getUserRequestDTO() {
        return new UserPostRequestDTO(
                        "Romulo",
                        "004.773.170-22",
                        "romulo.darde@gmail.com",
                        "Teste@321");
    }
}
