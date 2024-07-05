package financial_dashboard.model;

import financial_dashboard.dto.investmentgoal.InvestmentGoalRequestDTO;
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
@Table(name = "monthly_investment_goals")
public class MonthlyInvestmentGoal {

    //Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal value;
    private String status;

    @CreationTimestamp
    private LocalDate registrationDate;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Transient
    private String notification;


    //Construtor sem o id, registrationDate, user e notification
    public MonthlyInvestmentGoal(BigDecimal value) {
        this.value = value;
    }


    //Métodos
    //Método para adicionar um usuário a meta de investimento
    public void addUser(User user) {
        this.user = user;
        user.setMonthlyInvestmentGoal(this);
    }

    //Método para atualizar os dados
    public void updateData(InvestmentGoalRequestDTO dto) {
        if (dto.value() != null) this.value = dto.value();
    }

    //Método para atualizar o status
    public void updateStatus() {
        this.user.getAccount().updateMonthBalance();
        this.status = "Atingida";
        if (this.value.doubleValue() >
                this.user.getAccount().getMonthBalance().doubleValue()) this.status = "Não atingida";
    }

    //Método para atualizar uma notificação
    public void updateNotification() {
        this.notification = "No momento, não há notificações.";
        this.user.getAccount().updateMonthBalance();

        int today = LocalDate.now().getDayOfMonth();
        int lastDayOfMonth = this.getLastDayOfMonth().getDayOfMonth();

        String userName = this.user.getName();
        Double balanceAtCurrentMonth = this.user.getAccount().getMonthBalance().doubleValue();
        Double investmentGoalValue = this.value.doubleValue();

        //Se os gastos do mês passerem do limite da meta de investimento,
        //enviará uma notificação por e-mail e para a caixa de notificações do app.
        if (balanceAtCurrentMonth < investmentGoalValue) {
            this.notification = String.format("Olá %s!" +
                            "\nSua meta de investimento mensal é de R$ %.2f, " +
                            "mas o seu saldo do mês atual está em R$ %.2f." +
                            "\n\nSe você não tiver mais nenhuma entrada nesse mês, " +
                            "não conseguirá atingir a sua meta mensal de investimento. \n  ",
                    userName, investmentGoalValue, balanceAtCurrentMonth);
        }

        //Quando for o último dia do mês,
        //enviará uma notificação por e-mail e para a caixa de notificações do app.
        if (today == lastDayOfMonth) {
            if (balanceAtCurrentMonth < investmentGoalValue) {
                this.notification = String.format("<Olá %s!" +
                                "\nSua meta de investimento mensal é de R$ %,.2f, " +
                                "mas o seu saldo do mês foi de R$ %,.2f. " +
                                "\n\nVocê infelizmente não atingiu a meta de investimento nesse mês. \n  ",
                        userName, investmentGoalValue, balanceAtCurrentMonth);
            } else {
                this.notification = String.format("Olá, %s!" +
                                "\nParabéns! Nesse, mês você conseguiur atingir sua meta " +
                                "de investimento de R$ %,.2f. " +
                        "\nContinue assim! \n  ", userName, investmentGoalValue);
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
        } return lastDayOfMonth;
    }
}
