package financial_dashboard.controller;

import financial_dashboard.dto.investmentgoal.InvestmentGoalResponseDTO;
import financial_dashboard.dto.investmentgoal.InvestmentGoalRequestDTO;
import financial_dashboard.service.MonthlyInvestmentGoalService;
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
@RequestMapping("/investmentgoals")
@SecurityRequirement(name = "bearer-key")
@CrossOrigin(origins = "*")
public class MonthlyInvestmentGoalController {

    //ATRIBUTOS
    @Autowired
    private MonthlyInvestmentGoalService service;

    //MÉTODOS PRINCIPAIS: GET ALL, GET, POST PUT E DELETE
    //GET ALL - ADMIN
    @Operation(summary = "List all registered Monthly Investment Goals. " +
            "It can only be accessed by users with \"ADMIN\" authorization.")
    @Secured("ROLE_ADMIN")
    @GetMapping("/all")
    public ResponseEntity<List<InvestmentGoalResponseDTO>> getInvestmentGoals() {
        return ResponseEntity.ok(service.getInvestmentGoals());
    }

    //GET - USER
    @Operation(summary = "Accesses the Monthly Investment Goal identified by the User JWT Token.")
    @GetMapping
    public ResponseEntity<InvestmentGoalResponseDTO> getInvestmentGoal
    (HttpServletRequest request) {
        return ResponseEntity.ok(service.getInvestmentGoal(request));
    }

    //POST - USER
    @Operation(summary = "Register a new Monthly Investment Goal for de User identified by JWT Token. " +
            "There can only be one Monthly Investment Goal for each User.",
            responses = @ApiResponse(
                    description = "Created",
                    responseCode = "201",
                    content = {@Content(
                            schema = @Schema(implementation = InvestmentGoalResponseDTO.class))}))
    @PostMapping
    public ResponseEntity postInvestmentGoal(
            @RequestBody @Valid InvestmentGoalRequestDTO dto,
            HttpServletRequest request) {
        return service.postInvestmentGoal(dto, request);
    }

    //UPDATE - USER
    @Operation(summary = "Updates a Monthly Investment Goal identified by the User JWT Token, " +
            "passing the values to be corrected.",
            responses = @ApiResponse(content = {@Content(
                    schema = @Schema(implementation = InvestmentGoalResponseDTO.class))}))
    @PutMapping
    public ResponseEntity updateInvestmentGoal(
            HttpServletRequest request, @RequestBody @Valid InvestmentGoalRequestDTO dto) {
        service.updateInvestmentGoal(request, dto);
        return ResponseEntity.ok(service.getInvestmentGoal(request));
    }

    //DELETE- USER
    @Operation(summary = "Deletes a Monthly Investment Goal identified by the User JWT Token.",
            responses = @ApiResponse(description = "No Content", responseCode = "204"))
    @DeleteMapping
    public ResponseEntity deleteInvestmentGoal(HttpServletRequest request) {
        service.deleteInvestmentGoal(request);
        return ResponseEntity.noContent().build();
    }


    //DEMAIS MÉTODOS PARA CONSULTAS
    //GET NOTIFICATION - USER
    @Operation(summary = "Check the notification of the Monthly Investment Goal.")
    @GetMapping("/notification")
    public ResponseEntity<String> getNotification(
            HttpServletRequest request) {
        return ResponseEntity.ok(service.getNotification(request));
    }


}
