package br.com.ecomerce.ecomerce.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private JWTUtil jwtUtil;
    private UserDetailsService userDetailsService;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, UserDetailsService userDetailsService) {
        super(authenticationManager);
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String header = request.getHeader("Authorization");

        // Verifique se o header existe e tem o tamanho mínimo ("Bearer " + token)
        if (header != null && header.startsWith("Bearer ")) {
            try {

                String token = header.substring(7);
                UsernamePasswordAuthenticationToken auth = getAuthentication(token);

                if (auth != null) {
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (Exception e) {
                // Se o token for malformado ou expirado, o Spring Security
                // limpará o contexto e barrará o acesso automaticamente se a rota for protegida.
                // Opcional: imprimir o erro para debug
                System.out.println("Erro na autenticação JWT: " + e.getMessage());
            }
        }

        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String token) {
        if (jwtUtil.tokenValido(token)) {
            String userName = jwtUtil.getUserName(token);
            UserDetails user = userDetailsService.loadUserByUsername(userName);
            System.out.println("TOKEN USER ------------------>" + userName);
            System.out.println("AUTHORITIES ------------------>" + user.getAuthorities());
            return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        }
        return null;
    }
}


