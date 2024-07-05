package financial_dashboard.repository;

import financial_dashboard.model.MonthlyInvestmentGoal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonthlyInvestmentGoalRepository extends
        JpaRepository<MonthlyInvestmentGoal, Long> {
}
