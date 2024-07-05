package financial_dashboard.model.enums;

public enum TransactionCategory {
    SALARIO ("salario"),
    MORADIA("moradia"),
    CONTA_DE_LUZ("conta de luz"),
    CONTA_DE_INTERNET("conta de internet"),
    MERCADO ("mercado"),
    FARMACIA ("farmacia"),
    RESTAURANTE ("restaurante"),
    CONTA_FIXA ("conta fixa"),
    LAZER ("lazer"),
    GASTOS_COM_PET ("gastos com pet"),
    COMPRAS_INTERNET ("compras internet"),
    UBER ("uber"),
    COMBUSTIVEL ("combistivel"),
    MANUTENCAO_DO_CARRO ("manutencao do carro"),
    SEGURO_DO_CARRO ("seguro do carro"),
    RESTAURANTE_DELIVERY ("restaurante delivery"),
    PLANO_DE_SAUDE ("plano de saude"),
    CONSULTAS_MEDICAS ("consultas medicas"),
    MENSALIDADES_ESCOLARES ("mensalidades escolares"),
    ASSINATURAS_STREAMING ("assinaturas streaming"),
    VESTUARIO ("vestuario"),
    MENSALIDADE_ACADEMIA ("mensalidade academia"),
    IMPOSTOS ("impostos"),
    OUTRAS_RECEITAS ("outras receitas"),
    OUTRAS_DESPESAS ("outras despesas");


    //Atributos
    private String stringCategory;


    //Construtor
    TransactionCategory(String stringCategory) {
        this.stringCategory = stringCategory;
    }


    //Método estático para associar a String ao Enum
    public static TransactionCategory fromString(String text) {
        for (TransactionCategory category: TransactionCategory.values()) {
            if (category.stringCategory.equalsIgnoreCase(text)) {
                return category;
            }
        }
        throw new IllegalArgumentException
                ("No category found for the given String: " + text);
    }


}
