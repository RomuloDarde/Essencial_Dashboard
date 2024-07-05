package financial_dashboard;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FinancialDashboardApplication {

	public static void main(String[] args) {
		// Carregar as variáveis de ambiente do arquivo .env
		Dotenv dotenv = Dotenv.configure().load();

		// Definir as variáveis de ambiente no sistema
		dotenv.entries()
				.forEach(e -> System.setProperty(e.getKey(), e.getValue()));

		SpringApplication.run(FinancialDashboardApplication.class, args);
	}

}
