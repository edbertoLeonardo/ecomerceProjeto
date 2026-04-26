package br.com.ecomerce.ecomerce.serviceTest.validation;

import br.com.ecomerce.ecomerce.dto.ClienteNewDto;
import br.com.ecomerce.ecomerce.model.Cliente;
import br.com.ecomerce.ecomerce.model.enums.TipoCliente;
import br.com.ecomerce.ecomerce.repositories.ClienteRepository;
import br.com.ecomerce.ecomerce.service.validation.ClienteInsertValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ClienteInsertValidatorTest {

    @InjectMocks
    private ClienteInsertValidator validator;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ConstraintValidatorContext context;// = Mockito.mock(ConstraintValidatorContext.class, Mockito.RETURNS_DEEP_STUBS);

    @Test
    void retornarFalseQuandoCpfInvalido() {
        ClienteNewDto dto = new ClienteNewDto();
        dto.setTipo(TipoCliente.PESSOAFISICA.getCodigo());
        dto.setCpfOuCnpj("123"); // inválido
        dto.setEmail("teste@email.com");

        Mockito.when(clienteRepository.findByEmail(Mockito.any()))
                .thenReturn(null);

        boolean result = validator.isValid(dto, context);

        Assertions.assertFalse(result);
    }

    @Test
    void retornarFalseQuandoEmailJaExiste() {
        ClienteNewDto dto = new ClienteNewDto();
        dto.setTipo(TipoCliente.PESSOAFISICA.getCodigo());
        dto.setCpfOuCnpj("52998224725"); // CPF válido
        dto.setEmail("teste@email.com");


        ConstraintValidatorContext.ConstraintViolationBuilder builder =
                Mockito.mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext nodeBuilder =
                Mockito.mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class);


        Mockito.when(context.buildConstraintViolationWithTemplate(Mockito.anyString())).thenReturn(builder);
        Mockito.when(builder.addPropertyNode(Mockito.anyString())).thenReturn(nodeBuilder);
        Mockito.when(clienteRepository.findByEmail(dto.getEmail())).thenReturn(new Cliente());

        boolean result = validator.isValid(dto, context);

        Assertions.assertFalse(result);
    }

    @Test
    void retornarTrueQuandoTudoValido() {
        ClienteNewDto dto = new ClienteNewDto();
        dto.setTipo(TipoCliente.PESSOAFISICA.getCodigo());
        dto.setCpfOuCnpj("52998224725");
        dto.setEmail("teste@email.com");

        Mockito.when(clienteRepository.findByEmail(Mockito.any()))
                .thenReturn(null);

        boolean result = validator.isValid(dto, context);

        Assertions.assertTrue(result);
    }
}