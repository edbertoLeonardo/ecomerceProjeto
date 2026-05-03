package br.com.ecomerce.ecomerce.enumTest;

import br.com.ecomerce.ecomerce.model.enums.TipoCliente;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TipoClienteTest {

    @Test
    void retornarTipoClienteParaCodigoValido() {
        assertEquals(TipoCliente.PESSOAFISICA, TipoCliente.toEnum(1));
        assertEquals(TipoCliente.PESSOAJURIDICA, TipoCliente.toEnum(2));
    }

    @Test
    void lancarExcecaoParaCodigoInvalido() {
        assertThrows(IllegalArgumentException.class, () -> {
            TipoCliente.toEnum(3);
        });
    }
}
