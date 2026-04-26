package br.com.ecomerce.ecomerce.serviceTest.validation;

import br.com.ecomerce.ecomerce.dto.ClienteDto;
import br.com.ecomerce.ecomerce.model.Cliente;
import br.com.ecomerce.ecomerce.repositories.ClienteRepository;
import br.com.ecomerce.ecomerce.service.validation.ClienteUpdateValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.HandlerMapping;

import java.util.HashMap;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class ClienteUpdateValidatorTest {

    @InjectMocks
    private ClienteUpdateValidator validator;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private HttpServletRequest request;


    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ConstraintValidatorContext context;

    private ClienteDto dto;

    @BeforeEach
    void setUp() {
        dto = new ClienteDto();
        dto.setEmail("novoemail@gmail.com");

        Map<String, String> uriVars = new HashMap<>();
        uriVars.put("id", "1");

        Mockito.when(request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE))
                .thenReturn(uriVars);
    }

    @Test
    void retornarTrueQuandoEmailPertenceAoProprioCliente() {
        Cliente clienteExistente = new Cliente();
        clienteExistente.setId(1);
        clienteExistente.setEmail("novoemail@gmail.com");

        Mockito.when(clienteRepository.findByEmail(dto.getEmail())).thenReturn(clienteExistente);

        boolean result = validator.isValid(dto, context);

        Assertions.assertTrue(result);
    }

    @Test
    void retornarFalseQuandoEmailJaPertenceAOutroCliente() {

        Cliente outroCliente = new Cliente();
        outroCliente.setId(2);
        outroCliente.setEmail("novoemail@gmail.com");

        Mockito.when(clienteRepository.findByEmail(dto.getEmail())).thenReturn(outroCliente);

        boolean result = validator.isValid(dto, context);

        Assertions.assertFalse(result);
    }

    @Test
    void retornarTrueQuandoEmailNaoExisteNoBanco() {

        Mockito.when(clienteRepository.findByEmail(dto.getEmail())).thenReturn(null);

        boolean result = validator.isValid(dto, context);

        Assertions.assertTrue(result);
    }
}