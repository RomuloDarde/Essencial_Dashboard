package financial_dashboard.security;

import financial_dashboard.exception.createdexceptions.ResourceNotFoundException;
import financial_dashboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService {
    //Atributos
    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (repository.findByEmail(username) != null) {
            return repository.findByEmail(username);
        } else throw new ResourceNotFoundException(
                "There is no user with that email: " + username + ".");
    }

}
