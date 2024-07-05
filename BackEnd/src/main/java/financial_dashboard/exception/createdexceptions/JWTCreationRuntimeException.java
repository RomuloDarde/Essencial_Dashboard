package financial_dashboard.exception.createdexceptions;

public class JWTCreationRuntimeException extends RuntimeException{
    public JWTCreationRuntimeException(String message) {
        super(message);
    }
}
