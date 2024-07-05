package financial_dashboard.controller;

import financial_dashboard.dto.transaction.AmountByCategoryResponseDTO;
import financial_dashboard.dto.transaction.TransactionRequestDTO;
import financial_dashboard.dto.transaction.TransactionResponseDTO;
import financial_dashboard.service.TransactionService;
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
@RequestMapping("/transactions")
@SecurityRequirement(name = "bearer-key")
@CrossOrigin(origins = "*")
public class TransactionController {

    //ATRIBUTOS
    @Autowired
    private TransactionService service;


    //MÉTODOS PRINCIPAIS - GET ALL, (ADMIN), GET ALL (USER), GET BY ID, POST, PUT BY ID E DELETE BY ID
    //GET ALL - ADMIN
    @Operation(summary = "List all registered Transactions. " +
            "It can only be accessed by users with \"ADMIN\" authorization.")
    @Secured("ROLE_ADMIN")
    @GetMapping("all")
    public ResponseEntity<List<TransactionResponseDTO>> getTransactions() {
        return ResponseEntity.ok(service.getTransactions());
    }

    //GET ALL - USER
    @Operation(summary = "List all registered Transactions by the Account identified by the User JWT Token.")
    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> getTransactionsByAccount(
            HttpServletRequest request) {
        return ResponseEntity.ok(service.getTransactionsByAccount(request));
    }

    //GET BY ID - USER
    @Operation(summary = "Accesses a Transaction identified by id. " +
            "The access will only be allowed if the transaction belongs to the Account " +
            "identified by the User JWT Token.")
    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> getTransactionById(
            @PathVariable Long id,
            HttpServletRequest request) {
        return ResponseEntity.ok(service.getTransactionById(id, request));
    }

    //POST - USER
    @Operation(summary = "Register a new Transaction Goal at Account identified by the User JWT Token.",
            responses = @ApiResponse(
                    description = "Created",
                    responseCode = "201",
                    content = {@Content(
                            schema = @Schema(implementation = TransactionResponseDTO.class))}))
    @PostMapping
    public ResponseEntity postTransaction(
            @RequestBody  @Valid TransactionRequestDTO dto,
            HttpServletRequest request) {
        return service.postTransaction(dto, request);
    }

    //PUT BY ID - USER
    @Operation(summary = "Updates a Transaction identified by id. " +
            "Will only be allowed if the Transaction belongs to the Account identified by the User JWT Token.",
            responses = @ApiResponse(content = {@Content(
                    schema = @Schema(implementation = TransactionResponseDTO.class))}))
    @PutMapping("/{id}")
    public ResponseEntity updateTransaction(
            @PathVariable Long id,
            @RequestBody TransactionRequestDTO dto,
            HttpServletRequest request) {
        service.updateTransaction(id, dto, request);
        return ResponseEntity.ok(service.getTransactionById(id, request));
    }

    //DELETE BY ID - USER
    @Operation(summary = "Deletes a Transaction identified by id. " +
            "Will only be allowed if the Transaction belongs to the Account identified by the User JWT Token.",
            responses = @ApiResponse(description = "No Content", responseCode = "204"))
    @DeleteMapping("/{id}")
    public ResponseEntity deleteTransasction(
            @PathVariable Long id,
            HttpServletRequest request) {
        service.deleteTransactionById(id, request);
        return ResponseEntity.noContent().build();
    }

    //////////////////////////////////////////////////////////////////////////////////////////

    //DEMAIS MÉTODOS PARA CONSULTAS
    //DEBITS BY CATEGORY - USER
    @Operation(summary = "List the debits by Category at the Account identified by the User JWT Token.")
    @GetMapping("debits/")
    public ResponseEntity<List<TransactionResponseDTO>> getDebitsByCategory(
            HttpServletRequest request, @RequestParam("category") String category) {
        return ResponseEntity.ok(service.getDebitsByStringCategory(request, category));
    }

    //DEBITS RANK BY CATEGORY - USER
    @Operation(summary = "Get a Debits Rank by Category at the Account identified by the User JWT Token.")
    @GetMapping("/debits/categoryrank")
    public ResponseEntity<List<AmountByCategoryResponseDTO>> getExpensesRankByCategory(
            HttpServletRequest request) {
        return ResponseEntity.ok(service.getExpensesRankByCategory(request));
    }

    //DEBITS RANK BY CATEGORY BY MONTH - USER
    @Operation(summary = "Get a Debits Rank by Category by Month, at the Account identified by the User JWT Token.")
    @GetMapping("/debits/categoryrankbymonth/")
    public ResponseEntity<List<AmountByCategoryResponseDTO>> getExpensesRankByCategoryByMonth(
            HttpServletRequest request, @RequestParam("month") Integer month) {
        return ResponseEntity.ok(service.getExpensesRankByCategoryByMonth(request, month));
    }

    //AT CURRENT MONTH - USER
    @Operation(summary = "List the transactions at current month at the Account identified by the User JWT Token.")
    @GetMapping("/atcurrentmonth")
    public ResponseEntity<List<TransactionResponseDTO>> getTransactionsAtCurretnMonth(
            HttpServletRequest request) {
        return ResponseEntity.ok(service.getTransactionsAtCurrentMonth(request));
    }

    //BY MONTH - USER
    @Operation(summary = "List the transactions by month at the Account identified by the User JWT Token.")
    @GetMapping("/bymonth/")
    public ResponseEntity<List<TransactionResponseDTO>> getTransactionsByMonth (
            HttpServletRequest request, @RequestParam("month") Integer month) {
        return ResponseEntity.ok(service.getTransactionsByMonth(request, month));
    }

    //BY PERIOD - USER
    @Operation(summary = "List the transactions by period (initial date and final date)" +
            " at the Account identified by the User JWT Token.")
    @GetMapping("/byperiod/")
    public ResponseEntity<List<TransactionResponseDTO>> getTransactionsByPeriod (
            HttpServletRequest request,
            @RequestParam("initialdate") String initialDate,
            @RequestParam("finaldate") String finaldate) {

        return ResponseEntity.ok(service.getTransactionsByPeriod(request, initialDate, finaldate));
    }

    //CATEGORIES LIST
    @Operation(summary = "List the transactions categories.")
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getCategories () {
        return ResponseEntity.ok(service.getCategories());
    }

}
