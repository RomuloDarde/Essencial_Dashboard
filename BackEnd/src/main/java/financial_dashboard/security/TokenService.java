package financial_dashboard.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import financial_dashboard.exception.createdexceptions.JWTCreationRuntimeException;
import financial_dashboard.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    //Atributos
    //@Value("${api.security.token.secret}")
    private String secret = "my-secret-key";

    //Métodos
    //Método para gerar um token a partir de um usuário
    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("financial_dashboard.api")
                    .withSubject(user.getEmail())
                    .withClaim("id", user.getId())
                    .withExpiresAt(generateExpirationDate())
                    .sign(algorithm);

            return token;
        } catch (JWTCreationException exception) {
            throw new JWTCreationRuntimeException("Error while generating token. " + exception.getMessage());
        }
    }

    //Método para validar um token e retornar o seu Subject (login)
    public String validateToken(String token) {
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String login =  JWT.require(algorithm)
                    .withIssuer("financial_dashboard.api")
                    .build()
                    .verify(token)
                    .getSubject();

            return login;
        } catch (JWTVerificationException exception) {
            return "";
        }

    }

    //Métodos privados
    //Método privado que retorna o instante de agora mais 2h.
    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
