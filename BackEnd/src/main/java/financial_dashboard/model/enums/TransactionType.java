package financial_dashboard.model.enums;

public enum TransactionType {
    DESPESA ("despesa"),
    RECEITA ("receita");

    //Atributos
    private String stringType;

    //Construtor
    TransactionType(String stringType) {
        this.stringType = stringType;
    }


    //Método estático para associar a String ao Enum
    public static TransactionType fromString(String text) {
        for (TransactionType type:TransactionType.values()) {
            if (type.stringType.equalsIgnoreCase(text)) {
                return type;
            }
        }
        throw new IllegalArgumentException
                ("No type found for the given String: " + text);
    }

}
