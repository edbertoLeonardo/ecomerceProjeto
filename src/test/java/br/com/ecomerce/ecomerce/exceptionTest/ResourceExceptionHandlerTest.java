package br.com.ecomerce.ecomerce.exceptionTest;

import br.com.ecomerce.ecomerce.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ResourceExceptionHandlerTest {

    @InjectMocks
    private ResourceExceptionHandler handler;

    @Mock
    private HttpServletRequest request;

    @Test
    void retornarNotFoundQuandoObjectNotFoundException() {
        ObjectNotFoundException ex = new ObjectNotFoundException("Objeto não encontrado");

        ResponseEntity<StandardError> response = handler.objectNotFound(ex, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Objeto não encontrado", response.getBody().getMsg());
        assertEquals(404, response.getBody().getStatus());
    }

    @Test
    void retornarBadRequestQuandoDataIntegrityException() {
        DataIntegrityException ex = new DataIntegrityException("Erro de integridade");

        ResponseEntity<StandardError> response = handler.dataIntegrity(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Erro de integridade", response.getBody().getMsg());
    }

    @Test
    void retornarErrosDeValidacao() {
        ResourceExceptionHandler handler = new ResourceExceptionHandler();

        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        when(ex.getBindingResult()).thenReturn(bindingResult);

        List<FieldError> fieldErrors = List.of(
                new FieldError("obj", "email", "Email inválido"),
                new FieldError("obj", "cpf", "CPF inválido")
        );

        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        ResponseEntity<StandardError> response =
                handler.validation(ex, null);

        assertEquals(400, response.getStatusCodeValue());

        ValidationError body = (ValidationError) response.getBody();

        assertEquals("Erro de validação", body.getMsg());
        assertEquals(2, body.getErrors().size());

        assertEquals("email", body.getErrors().get(0).getFieldName());
        assertEquals("Email inválido", body.getErrors().get(0).getMessage());
    }
}