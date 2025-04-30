package grand.capital.bank.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collections;

public class JwtAuthenticatedUser extends AbstractAuthenticationToken {
    private final Long userId;

    public JwtAuthenticatedUser(Long userId) {
        super(Collections.singletonList(new SimpleGrantedAuthority("USER")));
        this.userId = userId;
        setAuthenticated(true);
    }
    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return userId;
    }
}