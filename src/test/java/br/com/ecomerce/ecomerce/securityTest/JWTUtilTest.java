package br.com.ecomerce.ecomerce.securityTest;

import br.com.ecomerce.ecomerce.config.security.JWTUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class JWTUtilTest {

private JWTUtil jwtUtil;
private final String segredoFicticio = "meusegredomuitolongoemuitoseguro1234567890";

@BeforeEach
void setUp() {
    jwtUtil = new JWTUtil();

    ReflectionTestUtils.setField(jwtUtil, "secret", segredoFicticio);
    ReflectionTestUtils.setField(jwtUtil, "expiration", 60000L);
}

@Test
void deveGerarTokenValidoERecuperarUsername() {
    // Given
    String email = "usuario@email.com";

    // When
    String token = jwtUtil.generateToken(email);

    // Then
    assertThat(token).isNotNull();
    assertThat(jwtUtil.tokenValido(token)).isTrue();
    assertThat(jwtUtil.getUserName(token)).isEqualTo(email);
}

@Test
void deveRetornarFalseParaTokenInvalido() {
    // Given
    String tokenInvalido = "token.totalmente.invalido";

    // When
    boolean resultado = jwtUtil.tokenValido(tokenInvalido);

    // Then
    assertThat(resultado).isFalse();
}

@Test
void deveRetornarFalseParaTokenExpirado() {
    // Given
    JWTUtil utilExpirado = new JWTUtil();

    ReflectionTestUtils.setField(utilExpirado, "secret", segredoFicticio);
    ReflectionTestUtils.setField(utilExpirado, "expiration", -1L); // já nasce expirado

    String token = utilExpirado.generateToken("expirado@email.com");

    // When
    boolean valido = utilExpirado.tokenValido(token);

    // Then
    assertThat(valido).isFalse();
}

@Test
void deveRetornarFalseParaTokenAlterado() {
    // Given
    String token = jwtUtil.generateToken("usuario@email.com");

    // altera o token (simulando ataque)
    String tokenAlterado = token + "abc";

    // When
    boolean valido = jwtUtil.tokenValido(tokenAlterado);

    // Then
    assertThat(valido).isFalse();
}
}