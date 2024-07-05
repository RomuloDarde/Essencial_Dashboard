package financial_dashboard.controller;

import financial_dashboard.dto.financialgoal.FinancialGoalResponseDTO;
import financial_dashboard.dto.investmentgoal.InvestmentGoalResponseDTO;
import financial_dashboard.dto.user.*;
import financial_dashboard.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {

    //ATRIBUTOS
    @Autowired
    private UserService service;

    //MÉTODOS PRINCIPAIS: LOGIN, GET ALL, GET (PERFIL), POST PUT E DELETE
    //LOGIN - LIVRE
    @Operation(summary = "Allows to login with username and password, generating a JWT Token.",
            responses = @ApiResponse(content = {@Content(
                    schema = @Schema(implementation = LoginResponseDTO.class))}))
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthenticationDTO dto) {
        return service.login(dto);
    }

    //GET ALL - ADMIN
    @Operation(summary = "List all registered Users. " +
            "It can only be accessed by users with \"ADMIN\" authorization.")
    @Secured("ROLE_ADMIN")
    @GetMapping
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<List<UserResponseDTO>> getUsers() {
        return ResponseEntity.ok(service.getUsers());
    }

    //GET (PERFIL) - USER
    @Operation(summary = "Accesses the user identified by JWT Token.")
    @GetMapping("/profile")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<UserResponseDTO> getProfile(HttpServletRequest request) {
        return ResponseEntity.ok(service.getProfile(request));
    }

    //POST - LIVRE
    @Operation(summary = "Register a new user.",
    responses = @ApiResponse(
            description = "Created",
            responseCode = "201",
            content = {@Content(
                    schema = @Schema(implementation = UserResponseDTO.class))}))
    @PostMapping
    public ResponseEntity postUser(
            @RequestBody @Valid UserPostRequestDTO dto) {
        return service.postUser(dto);
    }

    //PUT (PERFIL) - USER
    @Operation(summary = "Updates a user passing the values to be corrected.",
            responses = @ApiResponse(content = {@Content(
                    schema = @Schema(implementation = UserResponseDTO.class))}))
    @PutMapping(value = "/profile")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity updateProfile(HttpServletRequest request,
                                        @RequestBody @Valid UserUpdateRequestDTO dto) {
        service.updateProfile(request, dto);

        if (dto.email() != null) return ResponseEntity.ok().build();
        return ResponseEntity.ok(service.getProfile(request));
    }

    //DELETE - ADMIN
    @Operation(summary = "Delete a user by this id. " +
            "It can only be accessed by users with \"ADMIN\" authorization.",
            responses = @ApiResponse(description = "No Content", responseCode = "204"))
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity deleteUserById(@PathVariable Long id) {
        service.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

}
