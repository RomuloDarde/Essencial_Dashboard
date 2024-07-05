package financial_dashboard.controller;

import financial_dashboard.dto.financialgoal.FinancialGoalPostRequestDTO;
import financial_dashboard.dto.financialgoal.FinancialGoalResponseDTO;
import financial_dashboard.dto.financialgoal.FinancialGoalUpdateRequestDTO;
import financial_dashboard.service.FinancialGoalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/financialgoals")
@SecurityRequirement(name = "bearer-key")
@CrossOrigin(origins = "*")
public class FinancialGoalController {

    //ATRIBUTOS
    @Autowired
    private FinancialGoalService service;


    //MÉTODOS PRINCIPAIS: GET ALL, GET, POST PUT E DELETE
    //GET ALL - ADMIN
    @Operation(summary = "List all registered Financial Goals. " +
            "It can only be accessed by users with \"ADMIN\" authorization.")
    @Secured("ROLE_ADMIN")
    @GetMapping("all")
    public ResponseEntity<List<FinancialGoalResponseDTO>> getFinancialGoals() {
        return ResponseEntity.ok(service.getFinancialGoals());
    }

    //GET - USER
    @Operation(summary = "Accesses the Financial Goal identified by the User JWT Token.")
    @GetMapping
    public ResponseEntity<FinancialGoalResponseDTO> getFinancialGoal
            (HttpServletRequest request) {
        return ResponseEntity.ok(service.getFinancialGoal(request));
    }

    //POST - USER
    @Operation(summary = "Register a new Financial Goal for de User identified by JWT Token. " +
            "There can only be one Financial Goal for each User.",
            responses = @ApiResponse(
                    description = "Created",
                    responseCode = "201",
                    content = {@Content(
                            schema = @Schema(implementation = FinancialGoalResponseDTO.class))}))
    @PostMapping
    public ResponseEntity postFinancialGoal(
            @RequestBody @Valid FinancialGoalPostRequestDTO dto,
            HttpServletRequest request) {
        return service.postFinancialGoal(dto, request);
    }

    //PUT - USER
    @Operation(summary = "Updates a Financial Goal identified by the User JWT Token, " +
            "passing the values to be corrected.",
            responses = @ApiResponse(content = {@Content(
                    schema = @Schema(implementation = FinancialGoalResponseDTO.class))}))
    @PutMapping
    public ResponseEntity updateFinancialGoal(
            HttpServletRequest request, @RequestBody FinancialGoalUpdateRequestDTO dto) {
        service.updateFinancialGoal(request, dto);
        return ResponseEntity.ok(service.getFinancialGoal(request));
    }

    //DELETE - USER
    @Operation(summary = "Deletes a Financial Goal identified by the User JWT Token.",
            responses = @ApiResponse(description = "No Content", responseCode = "204"))
    @DeleteMapping
    public ResponseEntity deleteFinancialGoal(HttpServletRequest request) {
        service.deleteFinancialGoal(request);
        return ResponseEntity.noContent().build();
    }

    //GET NOTIFICATION - USER
    @Operation(summary = "Check the notification of the Financial Goal.")
    @GetMapping("/notification")
    public ResponseEntity<String> getNotification(
            HttpServletRequest request) {
        return ResponseEntity.ok(service.getNotification(request));
    }
}
