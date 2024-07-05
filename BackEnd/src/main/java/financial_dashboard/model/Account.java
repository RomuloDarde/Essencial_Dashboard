package financial_dashboard.model;

import financial_dashboard.model.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal currentBalance;

    @Transient
    private BigDecimal monthBalance;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    //@JoinColumn cria uma coluna (foreign key) na tabela do banco de dados.

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Transaction> transactions;
    //mapped By identifica o atributo (pelo nome) na entidade relacionada.
    //CascadeType.ALL permite que a entidade relacionada seja persistida
    // no banco de dados junto com essa entidade.

    @Transient
    private String notification;


    //Construtor sem o id
    public Account(User user) {
        this.currentBalance = BigDecimal.ZERO;
        this.user = user;
    }

    //Método que adiciona uma transação a lista
    public void addTransactionToList(Transaction transaction) {
        transactions.add(transaction);
    }

    //Método que atualiza o saldo total
    public void updateCurrentBalance() {
        this.currentBalance = BigDecimal.ZERO;
        transactions.stream()
                .filter(t -> t.getType().equals(TransactionType.RECEITA))
                .forEach(t -> this.setCurrentBalance(this.currentBalance.add(t.getValue())));

        transactions.stream()
                .filter(t -> t.getType().equals(TransactionType.DESPESA))
                .forEach(t -> this.setCurrentBalance(this.currentBalance.subtract(t.getValue())));
    }

    //Método que atualiza o saldo do mês atual
    public void updateMonthBalance() {
        var transactionsAthCurrentMonth = getTansactionsAtCurrentMonth();

        this.monthBalance = BigDecimal.ZERO;
        transactionsAthCurrentMonth.stream()
                .filter(t -> t.getType().equals(TransactionType.RECEITA))
                .forEach(t -> this.setMonthBalance(this.monthBalance.add(t.getValue())));

        transactionsAthCurrentMonth.stream()
                .filter(t -> t.getType().equals(TransactionType.DESPESA))
                .forEach(t -> this.setMonthBalance(this.monthBalance.subtract(t.getValue())));
    }

    //Método que atualiza a notificação
    public void updateNotification() {
        this.notification = "No momento, não há notificações.";

        if(this.currentBalance.doubleValue() < 0) {
            this.notification = "Seu saldo está negativo: R$ " + String.format("%.2f",this.currentBalance) + "." +
                    "\nTente buscar alguma forma alternativa de receita " +
                    "para normalizar o seu saldo." +
                    "\nTente diminuir as suas despesas." +
                    "\nVocê pode consultar o seu ranking de despesas por " +
                    "categoria para verificar onde estão os seus maiores gastos. \n  ";
        }
    }

    //Método que busca todas as Transações do mês atual
    public List<Transaction> getTansactionsAtCurrentMonth() {
        Month month = LocalDate.now().getMonth();

        return this.transactions.stream()
                .filter(transaction -> transaction.getRegistrationDate().getMonth().equals(month))
                .collect(Collectors.toList());
    }

}


