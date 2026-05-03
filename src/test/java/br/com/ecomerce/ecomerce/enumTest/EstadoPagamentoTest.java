package br.com.ecomerce.ecomerce.enumTest;

import br.com.ecomerce.ecomerce.model.enums.EstadoPagamento;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EstadoPagamentoTest {

    @Test
    void retornarEstadoParaCodigoValido() {
        assertEquals(EstadoPagamento.PENDENTE, EstadoPagamento.toEnum(1));
        assertEquals(EstadoPagamento.QUITADO, EstadoPagamento.toEnum(2));
        assertEquals(EstadoPagamento.CANCELADO, EstadoPagamento.toEnum(3));
    }

    @Test
    void retornarNullParaCodigoNulo() {
        assertNull(EstadoPagamento.toEnum(null));
    }

    @Test
    void lancarExcecaoParaCodigoInvalido() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            EstadoPagamento.toEnum(99);
        });

        assertEquals("Id inválido: 99", exception.getMessage());
    }
}

