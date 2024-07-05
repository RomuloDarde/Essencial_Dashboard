package financial_dashboard.model.enums;

public enum UserRole {
    ADMIN ("admin"),
    USER ("user");

    //Atributos
    private String stringRole;

    //Construtor

    UserRole(String stringRole) {
        this.stringRole = stringRole;
    }

    public static UserRole fromString(String text) {
        for (UserRole role : UserRole.values()) {
            if (role.stringRole.equalsIgnoreCase(text)) {
                return role;
            }
        }
        throw new IllegalArgumentException
                ("There is no user role found for the given String: " + text);
    }
}
