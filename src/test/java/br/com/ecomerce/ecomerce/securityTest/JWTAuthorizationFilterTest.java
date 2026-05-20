package br.com.ecomerce.ecomerce.securityTest;

import br.com.ecomerce.ecomerce.config.security.JWTAuthorizationFilter;
import br.com.ecomerce.ecomerce.config.security.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collections;



import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JWTAuthorizationFilterTest {

    private JWTAuthorizationFilter filter;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JWTUtil jwtUtil;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain chain;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        filter = new JWTAuthorizationFilter(authenticationManager, jwtUtil, userDetailsService);
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    // =========================
    // 1. TOKEN VÁLIDO
    // =========================
    @Test
    void deveAutenticarQuandoTokenForValido() throws Exception {

        String token = "token.valido";
        String email = "user@email.com";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.tokenValido(token)).thenReturn(true);
        when(jwtUtil.getUserName(token)).thenReturn(email);
        when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);
        when(userDetails.getAuthorities()).thenReturn(Collections.emptyList());

        filter.doFilterInternal(request, response, chain);

        var auth = SecurityContextHolder.getContext().getAuthentication();

        assertThat(auth).isNotNull();
        assertThat(auth.getPrincipal()).isEqualTo(userDetails);
        assertThat(auth.getAuthorities()).isEmpty();
//        assertThat(auth.getName()).isEqualTo(email);

        verify(chain).doFilter(request, response);
    }

    // =========================
    // 2. SEM HEADER
    // =========================
    @Test
    void naoDeveAutenticarQuandoNaoHouverHeader() throws Exception {

        when(request.getHeader("Authorization")).thenReturn(null);

        filter.doFilterInternal(request, response, chain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();

        verify(chain).doFilter(request, response);
        verifyNoInteractions(jwtUtil, userDetailsService);
    }

    // =========================
    // 3. HEADER INVÁLIDO
    // =========================
    @Test
    void naoDeveAutenticarQuandoHeaderNaoForBearer() throws Exception {

        when(request.getHeader("Authorization")).thenReturn("Basic abc123");

        filter.doFilterInternal(request, response, chain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();

        verify(chain).doFilter(request, response);
        verifyNoInteractions(jwtUtil, userDetailsService);
    }

    // =========================
    // 4. TOKEN INVÁLIDO
    // =========================
    @Test
    void naoDeveAutenticarQuandoTokenForInvalido() throws Exception {

        String token = "token.invalido";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.tokenValido(token)).thenReturn(false);

        filter.doFilterInternal(request, response, chain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();

        verify(chain).doFilter(request, response);
        verify(jwtUtil).tokenValido(token);
        verifyNoMoreInteractions(userDetailsService);
    }

    // =========================
    // 5. EXCEÇÃO NO JWT
    // =========================
    @Test
    void deveContinuarFluxoMesmoComErro() throws Exception {

        String token = "token.erro";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.tokenValido(token)).thenThrow(new RuntimeException("erro"));

        filter.doFilterInternal(request, response, chain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();

        verify(chain).doFilter(request, response);
    }
}