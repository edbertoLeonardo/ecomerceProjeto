package br.com.ecomerce.ecomerce.securityTest;

import br.com.ecomerce.ecomerce.config.security.UserDetailsServiceSecurity;
import br.com.ecomerce.ecomerce.model.enums.Perfil;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserDetailsServiceSecurityTest {

    @Test
    void shouldMapPerfisToAuthorities() {
        Set<Perfil> perfis = Set.of(Perfil.ADMIN, Perfil.CLIENTE);

        UserDetailsServiceSecurity user =
                new UserDetailsServiceSecurity(1, "test@test.com", "123", perfis);

        assertEquals(2, user.getAuthorities().size());
        assertTrue(user.getAuthorities()
                .contains(new SimpleGrantedAuthority(Perfil.ADMIN.getDescricao())));
    }

    @Test
    void shouldReturnTrueWhenUserHasRole() {
        Set<Perfil> perfis = Set.of(Perfil.ADMIN);

        UserDetailsServiceSecurity user =
                new UserDetailsServiceSecurity(1, "test@test.com", "123", perfis);

        assertTrue(user.hasRole(Perfil.ADMIN));
    }

    @Test
    void shouldReturnFalseWhenUserDoesNotHaveRole() {
        Set<Perfil> perfis = Set.of(Perfil.CLIENTE);

        UserDetailsServiceSecurity user =
                new UserDetailsServiceSecurity(1, "test@test.com", "123", perfis);

        assertFalse(user.hasRole(Perfil.ADMIN));
    }
}
