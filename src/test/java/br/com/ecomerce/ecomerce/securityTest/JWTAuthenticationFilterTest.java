package br.com.ecomerce.ecomerce.securityTest;

import br.com.ecomerce.ecomerce.config.security.JWTAuthenticationFilter;
import br.com.ecomerce.ecomerce.config.security.JWTUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.DelegatingServletInputStream;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JWTAuthenticationFilterTest {

    private JWTAuthenticationFilter filter;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JWTUtil jwtUtil;

    // Usaremos uma instância real do ObjectMapper para serializar/deserializar os JSONs nativamente
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain chain;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetails userDetails;

    private StringWriter responseWriter;

    @BeforeEach
    void setUp() throws IOException {
        filter = new JWTAuthenticationFilter(authenticationManager, jwtUtil, objectMapper);
        responseWriter = new StringWriter();
        // Permite capturar e ler o JSON escrito na resposta
        lenient().when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));
    }

    // --- TESTES DO MÉTODO: attemptAuthentication ---

    @Test
    void attemptAuthentication_DeveRetornarAuthenticationQuandoCredenciaisForemValidas() throws IOException {
        // Given
        String jsonCredenciais = "{\"email\":\"teste@email.com\",\"senha\":\"123456\"}";
        ServletInputStream inputStream = new DelegatingServletInputStream(
                new ByteArrayInputStream(jsonCredenciais.getBytes(StandardCharsets.UTF_8))
        );
        when(request.getInputStream()).thenReturn(inputStream);

        ArgumentCaptor<UsernamePasswordAuthenticationToken> captor =
                ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);

        when(authenticationManager.authenticate(captor.capture())).thenReturn(authentication);

        // When
        Authentication result = filter.attemptAuthentication(request, response);

        // Then
        assertThat(result).isEqualTo(authentication);
        // Garante que o filtro extraiu os dados do JSON corretamente antes de enviar ao Manager
        assertThat(captor.getValue().getPrincipal()).isEqualTo("teste@email.com");
        assertThat(captor.getValue().getCredentials()).isEqualTo("123456");
    }

    @Test
    void attemptAuthentication_DeveLancarExcecaoQuandoOcorrerErroDeLeituraDoJson() throws IOException {
        // Given: Um fluxo de entrada quebrado que força uma IOException
        when(request.getInputStream()).thenThrow(new IOException("Erro de stream"));

        // When & Then
        assertThatThrownBy(() -> filter.attemptAuthentication(request, response))
                .isInstanceOf(AuthenticationServiceException.class)
                .hasMessageContaining("Erro ao ler credenciais de autenticação");
    }

    // --- TESTES DO MÉTODO: successfulAuthentication ---

    @Test
    void successfulAuthentication_DeveAdicionarHeadersEGerarJsonDeTokenComSucesso() throws IOException, ServletException {
        // Given
        String email = "admin@email.com";
        String tokenGerado = "jwt.gerado.aqui";

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(email);
        when(jwtUtil.generateToken(email)).thenReturn(tokenGerado);

        // When
        filter.successfulAuthentication(request, response, chain, authentication);

        // Then
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response).setContentType("application/json");
        verify(response).addHeader("Authorization", "Bearer " + tokenGerado);
        verify(response).addHeader("Access-Control-Expose-Headers", "Authorization");

        // Analisa o corpo do JSON retornado
        Map<?, ?> body = objectMapper.readValue(responseWriter.toString(), Map.class);
        assertThat(body.get("token")).isEqualTo(tokenGerado);
        assertThat(body.get("type")).isEqualTo("Bearer");
        assertThat(body.get("username")).isEqualTo(email);
    }

    // --- TESTES DO MÉTODO: unsuccessfulAuthentication ---

    @Test
    void unsuccessfulAuthentication_DeveRetornarStatus401EJsonDeErro() throws IOException, ServletException {
        // Given
        AuthenticationException excecaoDeLogin = new BadCredentialsException("Bad credentials");
        when(request.getServletPath()).thenReturn("/login");

        // When
        filter.unsuccessfulAuthentication(request, response, excecaoDeLogin);

        // Then
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType("application/json");

        // Analisa o corpo do JSON de erro retornado
        Map<?, ?> body = objectMapper.readValue(responseWriter.toString(), Map.class);
        assertThat(body.get("status")).isEqualTo(401);
        assertThat(body.get("error")).isEqualTo("Unauthorized");
        assertThat(body.get("message")).isEqualTo("Email ou senha inválidos");
        assertThat(body.get("path")).isEqualTo("/login");
        assertThat(body.get("timestamp")).isNotNull();
    }

}
