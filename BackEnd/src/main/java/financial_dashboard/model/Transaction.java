package financial_dashboard.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import financial_dashboard.dto.transaction.TransactionRequestDTO;
import financial_dashboard.model.enums.TransactionCategory;
import financial_dashboard.model.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@Data
@Entity
@Table(name = "transactions")
public class Transaction {

    //Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    private TransactionCategory category;

    private BigDecimal value;

    private String description;

    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate registrationDate;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;
    //@JoinColumn cria uma coluna (foreign key) na tabela do banco de dados.



    //Construtor sem id e registrationDate
    public Transaction(String stringType,
                       String stringCategory,
                       BigDecimal value,
                       String description) {
        this.type = TransactionType.fromString(stringType);
        this.category = TransactionCategory.fromString(stringCategory);
        this.value = value;
        this.description = description;
    }

    //Método para adicionar uma conta à transação
    public void addAccount(Account accont) {
        this.account = accont;
        this.account.addTransactionToList(this);
    }

    //Método para atualizar os dados
    public void updateData(TransactionRequestDTO dto) {
        if (dto.type() != null) this.type = TransactionType.fromString(dto.type());
        if (dto.category() != null) this.category = TransactionCategory.fromString(dto.category());
        if (dto.value() != null) this.value = dto.value();
        if (dto.description() != null) this.description = dto.description();
    }
}