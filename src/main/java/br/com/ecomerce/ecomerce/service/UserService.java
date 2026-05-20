package br.com.ecomerce.ecomerce.service;

import br.com.ecomerce.ecomerce.config.security.UserDetailsServiceSecurity;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserService {

    public static UserDetailsServiceSecurity authenticated() {
        try {
            return (UserDetailsServiceSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        catch (Exception e) {
            return null;
        }
    }
}
