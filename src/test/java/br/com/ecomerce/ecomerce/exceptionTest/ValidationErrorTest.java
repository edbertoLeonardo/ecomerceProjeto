package br.com.ecomerce.ecomerce.exceptionTest;

import br.com.ecomerce.ecomerce.exception.ValidationError;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ValidationErrorTest {

    @Test
    void adicionarErroCorretamente() {

        ValidationError error = new ValidationError(400, "Erro de validação", System.currentTimeMillis());


        error.addError("email", "Email inválido");
        error.addError("cpf", "CPF deve conter 11 dígitos");


        assertEquals(2, error.getErrors().size());
        assertEquals("email", error.getErrors().get(0).getFieldName());
        assertEquals("Email inválido", error.getErrors().get(0).getMessage());

        assertEquals("cpf", error.getErrors().get(1).getFieldName());
        assertEquals("CPF deve conter 11 dígitos", error.getErrors().get(1).getMessage());
    }
}

