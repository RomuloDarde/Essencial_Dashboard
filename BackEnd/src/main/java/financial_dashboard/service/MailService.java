package financial_dashboard.service;

import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.ClientOptions;
import com.mailjet.client.resource.Emailv31;
import financial_dashboard.model.Account;
import financial_dashboard.model.User;
import org.json.JSONArray;
import org.json.JSONObject;

public class MailService {

    //ATRIBUTOS
    MailjetClient client = new MailjetClient("ac3f48293ae812edacde11bb8176dc47",
            "0a71f6622664bb94ab39d8284762ba09",
            new ClientOptions("v3.1"));
    MailjetRequest request;
    MailjetResponse response;

    //MÉTODOS
    //EMAIL DE BOAS VINDAS
    public void welcomeMail(User user) throws MailjetException, MailjetSocketTimeoutException {
        request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                        .put(new JSONObject()
                                .put(Emailv31.Message.FROM, new JSONObject()
                                        .put("Email", "financialservice335@gmail.com")
                                        .put("Name", "Essencial Finanças Simplificadas"))

                                .put(Emailv31.Message.TO, new JSONArray()
                                        .put(new JSONObject()
                                                .put("Email", user.getEmail())
                                                .put("Name", user.getName())))

                                .put(Emailv31.Message.TEMPLATEID, 6007699)
                                .put(Emailv31.Message.TEMPLATELANGUAGE, true)
                                .put(Emailv31.Message.SUBJECT, "Bem vindo ao Essencial - Finanças Simplificadas")
                                .put(Emailv31.Message.VARIABLES, new JSONObject()
                                        .put("Username", user.getName()))));

        response = client.post(request);
        System.out.println(response.getStatus());
        System.out.println(response.getData());
    }


    //EMAIL DE SALDO NEGATIVO
    public void negativeBalanceMail(User user, Account account)
            throws MailjetException, MailjetSocketTimeoutException {

        request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                        .put(new JSONObject()
                                .put(Emailv31.Message.FROM, new JSONObject()
                                        .put("Email", "financialservice335@gmail.com")
                                        .put("Name", "Essencial Finanças Simplificadas"))

                                .put(Emailv31.Message.TO, new JSONArray()
                                        .put(new JSONObject()
                                                .put("Email", user.getEmail())
                                                .put("Name", user.getName())))

                                .put(Emailv31.Message.TEMPLATEID, 6015231)
                                .put(Emailv31.Message.TEMPLATELANGUAGE, true)
                                .put(Emailv31.Message.SUBJECT, "Essencial - Seu saldo está negativo!")
                                .put(Emailv31.Message.VARIABLES, new JSONObject()
                                        .put("Username", user.getName())
                                        .put("Balance", String.format("%.2f",
                                                account.getCurrentBalance().doubleValue())))));

        response = client.post(request);
        System.out.println(response.getStatus());
        System.out.println(response.getData());
    }


    //EMAIL DA NOTIFICAÇÃO DA META FINANCEIRA
    public void financialGoalNotificationMail(User user)
            throws MailjetException, MailjetSocketTimeoutException {

        request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                        .put(new JSONObject()
                                .put(Emailv31.Message.FROM, new JSONObject()
                                        .put("Email", "financialservice335@gmail.com")
                                        .put("Name", "Essencial Finanças Simplificadas"))

                                .put(Emailv31.Message.TO, new JSONArray()
                                        .put(new JSONObject()
                                                .put("Email", user.getEmail())
                                                .put("Name", user.getName())))

                                .put(Emailv31.Message.TEMPLATEID, 6015622)
                                .put(Emailv31.Message.TEMPLATELANGUAGE, true)
                                .put(Emailv31.Message.SUBJECT, "Essencial - Aviso sobre a Meta Financeira!")
                                .put(Emailv31.Message.VARIABLES, new JSONObject()
                                        .put("Username", user.getName())
                                        .put("FinancialGoalName", user.getFinancialGoal().getName())
                                        .put("FinancialGoalValue", String.format("%.2f",
                                                user.getFinancialGoal().getValue().doubleValue()))
                                        .put("FinancialGoalPercentage", String.format("%.0f",
                                                user.getFinancialGoal().getPercentage()))
                                        .put("ValueToComplete", String.format("%.2f",
                                                user.getFinancialGoal().getValueToComplete().doubleValue()))
                                        .put("monthBalance", String.format("%.2f",
                                                user.getAccount().getMonthBalance().doubleValue()))
                                        .put("InvestmentGoalValue", String.format("%.2f",
                                                user.getMonthlyInvestmentGoal().getValue().doubleValue())))));

        response = client.post(request);
        System.out.println(response.getStatus());
        System.out.println(response.getData());
    }


    //EMAIL DA NOTIFICAÇÃO DA META DE INVESTIMENTO
    public void investmentGoalNotificationMail(User user)
            throws MailjetException, MailjetSocketTimeoutException {

        request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                        .put(new JSONObject()
                                .put(Emailv31.Message.FROM, new JSONObject()
                                        .put("Email", "financialservice335@gmail.com")
                                        .put("Name", "Essencial Finanças Simplificadas"))

                                .put(Emailv31.Message.TO, new JSONArray()
                                        .put(new JSONObject()
                                                .put("Email", user.getEmail())
                                                .put("Name", user.getName())))

                                .put(Emailv31.Message.TEMPLATEID, 6015653)
                                .put(Emailv31.Message.TEMPLATELANGUAGE, true)
                                .put(Emailv31.Message.SUBJECT, "Essencial - Aviso sobre a Meta Mensal de Investimento!")
                                .put(Emailv31.Message.VARIABLES, new JSONObject()
                                        .put("Username", user.getName())
                                        .put("InvestmentGoalValue", String.format("%.2f",
                                                user.getMonthlyInvestmentGoal().getValue().doubleValue()))
                                        .put("monthBalance", String.format("%.2f",
                                                user.getAccount().getMonthBalance().doubleValue())))));

        response = client.post(request);
        System.out.println(response.getStatus());
        System.out.println(response.getData());
    }
}
