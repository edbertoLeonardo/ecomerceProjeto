package br.com.ecomerce.ecomerce.config.security;

import br.com.ecomerce.ecomerce.dto.AuthDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final ObjectMapper objectMapper;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager,
                                   JWTUtil jwtUtil,
                                   ObjectMapper objectMapper) {

        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.objectMapper = objectMapper;

        // endpoint do login
        setFilterProcessesUrl("/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException {

        try {

            AuthDTO credentials = objectMapper.readValue(request.getInputStream(), AuthDTO.class);

            UsernamePasswordAuthenticationToken authToken =
                    UsernamePasswordAuthenticationToken.unauthenticated(
                            credentials.getEmail(),
                            credentials.getSenha()
                    );

            return authenticationManager.authenticate(authToken);

        } catch (IOException e) {
            throw new AuthenticationServiceException(
                    "Erro ao ler credenciais de autenticação",
                    e
            );
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authentication)
            throws IOException, ServletException {

        UserDetails user = (UserDetails) authentication.getPrincipal();

        String token = jwtUtil.generateToken(user.getUsername());

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        // Header JWT
        response.addHeader("Authorization", "Bearer " + token);

        // Necessário para frontend acessar o header
        response.addHeader("Access-Control-Expose-Headers", "Authorization");

        Map<String, Object> body = new HashMap<>();
        body.put("token", token);
        body.put("type", "Bearer");
        body.put("username", user.getUsername());

        objectMapper.writeValue(response.getWriter(), body);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed)
            throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> error = new HashMap<>();
        error.put("status", 401);
        error.put("error", "Unauthorized");
        error.put("message", "Email ou senha inválidos");
        error.put("path", request.getServletPath());
        error.put("timestamp", System.currentTimeMillis());

        objectMapper.writeValue(response.getWriter(), error);
    }
}
//public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
//
//    private AuthenticationManager authenticationManager;
//
//    private JWTUtil jwtUtil;
//
//    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
//        setAuthenticationFailureHandler(new JWTAuthenticationFailureHandler());
//        this.authenticationManager = authenticationManager;
//        this.jwtUtil = jwtUtil;
//    }
//
//
//    public Authentication attemptAuthentication(HttpServletRequest req,
//                                                HttpServletResponse res) throws AuthenticationException {
//
//        try {
//            AuthDTO creds = new ObjectMapper()
//                    .readValue(req.getInputStream(), AuthDTO.class);
//
//            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getSenha(), new ArrayList<>());
//
//            Authentication auth = authenticationManager.authenticate(authToken);
//            return auth;
//        }
//        catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//
//    protected void successfulAuthentication(HttpServletRequest req,
//                                            HttpServletResponse res,
//                                            FilterChain chain,
//                                            Authentication auth) throws IOException, ServletException {
//
//        String username = ((UserDetailsServiceSecurity) auth.getPrincipal()).getUsername();
//        String token = jwtUtil.generateToken(username);
//        res.addHeader("Authorization", "Bearer " + token);
//        //res.addHeader("access-control-expose-headers", "Authorization");
//    }
//
//    private class JWTAuthenticationFailureHandler implements AuthenticationFailureHandler {
//
//        @Override
//        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
//                throws IOException, ServletException {
//            response.setStatus(401);
//            response.setContentType("application/json");
//            response.getWriter().append(json());
//        }
//
//        private String json() {
//            long date = new Date().getTime();
//            return "{\"timestamp\": " + date + ", "
//                    + "\"status\": 401, "
//                    + "\"error\": \"Não autorizado\", "
//                    + "\"message\": \"Email ou senha inválidos\", "
//                    + "\"path\": \"/login\"}";
//        }
//    }
//}
