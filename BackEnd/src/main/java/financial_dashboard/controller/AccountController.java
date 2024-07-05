package financial_dashboard.controller;

import financial_dashboard.dto.account.AccountResponseDTO;
import financial_dashboard.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/accounts")
@SecurityRequirement(name = "bearer-key")
@CrossOrigin(origins = "*")
public class AccountController {

    //ATRIBUTOS
    @Autowired
    private AccountService service;


    //MÉTODOS PRINCIPAIS - GET ALL E GET
    //GET ALL - ADMIN
    @Operation(summary = "List all registered Accounts. " +
            "It can only be accessed by users with \"ADMIN\" authorization.")
    @Secured("ROLE_ADMIN")
    @GetMapping("all")
    public ResponseEntity<List<AccountResponseDTO>> getAccounts() {
        return ResponseEntity.ok(service.getAccounts());
    }

    //GET - USER
    @Operation(summary = "Accesses the Account identified by the User JWT Token.")
    @GetMapping
    public ResponseEntity<AccountResponseDTO> getAccount(HttpServletRequest request) {
        return ResponseEntity.ok(service.getAccount(request));
    }

    //DEMAIS MÉTODOS PARA CONSULTAS
    //GET NOTIFICATION - USER
    @Operation(summary = "Check the notification of account.")
    @GetMapping("/notification")
    public ResponseEntity<String> getNotification(
            HttpServletRequest request) {
        return ResponseEntity.ok(service.getNotification(request));
    }

}
