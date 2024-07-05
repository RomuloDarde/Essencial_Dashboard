package financial_dashboard.model;

import financial_dashboard.dto.financialgoal.FinancialGoalUpdateRequestDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "financial_goals")
public class FinancialGoal {

    //Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private BigDecimal value;
    private BigDecimal valueToComplete;
    private String description;
    private double percentage;

    @CreationTimestamp
    private LocalDate registrationDate;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Transient
    private String notification;


    //Construtor sem o id, registrationDate e user
    public FinancialGoal(String name, BigDecimal value, String description) {
        this.name = name;
        this.value = value;
        this.description = description;
    }

    //Métodos
    //Método para adicionar um usuário a meta financeira
    public void addUser(User user) {
        this.user = user;
        user.setFinancialGoal(this);
    }

    //Método para atualizar os dados
    public void updateData(FinancialGoalUpdateRequestDTO dto) {
        if (dto.name() != null) this.name = dto.name();
        if (dto.value() != null) this.value = dto.value();
        if (dto.description() != null) this.description = dto.description();
    }

    //Método para atualizar o percentual
    public void updatePercentageAndValueToComplete() {
        double investmentGoalValue = 0.0;
        if (this.user.getMonthlyInvestmentGoal() != null) {
            investmentGoalValue = this.user.getMonthlyInvestmentGoal().getValue().doubleValue();
        }
        double balance = this.user.getAccount().getCurrentBalance().doubleValue();
        double difference = balance - investmentGoalValue;

        this.valueToComplete = this.value.subtract(BigDecimal.valueOf(difference));
        this.percentage = (difference * 100) / this.value.doubleValue();
    }

    //Método para atualizar a notificação
    public void updateNotification() {
        this.notification = "No momento, não há notificações.";
        this.user.getAccount().updateMonthBalance();

        int today = LocalDate.now().getDayOfMonth();
        int lastDayOfMonth = this.getLastDayOfMonth().getDayOfMonth();

        String userName = this.user.getName();
        String financialGoalName = this.getName();
        Double financialGoalValue = this.value.doubleValue();
        Double investmentGoalValue = 0.0;
        if (this.user.getMonthlyInvestmentGoal() != null) {
            investmentGoalValue = this.user.getMonthlyInvestmentGoal().getValue().doubleValue();
        }

        updatePercentageAndValueToComplete();
        Double balanceAtCurrentMonth = this.user.getAccount().getMonthBalance().doubleValue();
        Double percentage = this.percentage;
        Double valueToComplete = this.valueToComplete.doubleValue();
        Double progressionValue = balanceAtCurrentMonth - investmentGoalValue;


        //Se os gastos do mês passerem do limite da meta de financeira,
        //enviará uma notificação por e-mail e para a caixa de notificações do app.
        if (balanceAtCurrentMonth <= investmentGoalValue) {
            this.notification = String.format("Olá, %s! " +
                            "\rMeta Financeira: %s - Valor: R$ %,.2f. " +
                            "\n\nSeu saldo deste mês está, no momento, em R$ %,.2f. " +
                            "\nConsiderando a sua meta de investimento de R$ %,.2f, " +
                            "se você não tiver mais nenhuma entrada nesse mês, " +
                            "não conseguirá progredir com a sua meta financeira." +
                            "\n\nVoce atingiu %.0f%% da sua meta financeira, " +
                            "faltando um total de R$ %,.2f para completá-la. \n  ",
                    userName, financialGoalName, financialGoalValue, balanceAtCurrentMonth,
                    investmentGoalValue, percentage, valueToComplete);
        }

        //Quando for o último dia do mês,
        //enviará uma notificação por e-mail e para a caixa de notificações do app.
        if (today == lastDayOfMonth) {
            if (balanceAtCurrentMonth <= investmentGoalValue) {
                this.notification = """
                        Olá, %s!
                        Meta Financeira: %s - Valor: R$ %,.2f.
                        Seu saldo deste mês foi de R$ %,.2f.
                        
                        Considerando a sua meta de investimento de R$ %,.2f, você infelizmente não conseguiu progredir com a sua meta financeira.
                        Você atingiu %.0f%% da sua meta financeira, faltando um total de R$ %,.2f para completá-la.
                        
                        """.formatted(userName, financialGoalName,
                        financialGoalValue, balanceAtCurrentMonth, investmentGoalValue,
                        percentage, valueToComplete);

            } else {
                this.notification = String.format("Olá, %s! " +
                                "\nMeta Financeira: %s - Valor: R$ %,.2f." +
                                "\n\nParabéns! Nesse mês, você conseguiu progredir na sua meta financeira, " +
                                "com um total de R$ %,.2f. " +
                                "\nContinue assim!" +
                                "\n\nVoce atingiu %.0f%% da sua meta financeira, " +
                                "faltando um total de R$ %,.2f para completá-la. \n  ",
                        userName, financialGoalName, financialGoalValue, progressionValue,
                        percentage, valueToComplete);
            }
        }
    }

    //Método para buscar o último dia do mês
    public LocalDate getLastDayOfMonth() {
        LocalDate lastDayOfMonth = null;
        for (Month month : Month.values()) {
            if (LocalDate.now().getMonth().equals(month)) {
                lastDayOfMonth = LocalDate.of(LocalDate.now().getYear(), month, 1)
                        .with(TemporalAdjusters.lastDayOfMonth());
            }
        }
        return lastDayOfMonth;
    }


}
