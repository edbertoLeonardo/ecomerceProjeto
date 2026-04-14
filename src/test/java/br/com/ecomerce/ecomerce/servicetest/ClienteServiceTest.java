package br.com.ecomerce.ecomerce.servicetest;

import br.com.ecomerce.ecomerce.dto.ClienteDto;
import br.com.ecomerce.ecomerce.dto.ClienteNewDto;
import br.com.ecomerce.ecomerce.exception.DataIntegrityException;
import br.com.ecomerce.ecomerce.exception.ObjectNotFoundException;
import br.com.ecomerce.ecomerce.model.Cidade;
import br.com.ecomerce.ecomerce.model.Cliente;
import br.com.ecomerce.ecomerce.model.enums.TipoCliente;
import br.com.ecomerce.ecomerce.repositories.CidadeRepository;
import br.com.ecomerce.ecomerce.repositories.ClienteRepository;
import br.com.ecomerce.ecomerce.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @InjectMocks
    private ClienteService service;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private CidadeRepository cidadeRepository;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = new Cliente(1, "João", "joao@email.com", "12345678900", TipoCliente.PESSOAFISICA);
    }

    @Test
    void retornarClienteQuandoIdExistir() {
        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));

        Cliente result = service.findById(1);

        assertNotNull(result);
        assertEquals("João", result.getNome());
    }

    @Test
    void lancarExcecaoQuandoClienteNaoExistir() {
        when(clienteRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> {
            service.findById(1);
        });
    }

    @Test
    void inserirClienteComIdNulo() {
        Cliente novo = new Cliente(null, "Maria", "maria@email.com", "98765432100", TipoCliente.PESSOAFISICA);

        when(clienteRepository.save(any())).thenReturn(novo);

        Cliente result = service.insert(novo);

        assertNull(result.getId());
        verify(clienteRepository).save(novo);
    }

    @Test
    void atualizarCliente() {
        Cliente atualizado = new Cliente(1, "João Silva", "novo@email.com", "12345678900", TipoCliente.PESSOAFISICA);

        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any())).thenReturn(atualizado);

        Cliente result = service.update(atualizado);

        assertEquals("João Silva", result.getNome());
        assertEquals("novo@email.com", result.getEmail());
    }

    @Test
    void deletarClienteComSucesso() {
        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));

        service.delete(1);

        verify(clienteRepository).deleteById(1);
    }

    @Test
    void lancarDataIntegrityExceptionAoDeletar() {
        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));

        doThrow(DataIntegrityViolationException.class)
                .when(clienteRepository).deleteById(1);

        assertThrows(DataIntegrityException.class, () -> {
            service.delete(1);
        });
    }

    @Test
    void retornarListaDeClientes() {
        when(clienteRepository.findAll()).thenReturn(List.of(cliente));

        List<Cliente> lista = service.findAll();

        assertFalse(lista.isEmpty());
        assertEquals(1, lista.size());
    }

    @Test
    void retornarPaginaDeClientes() {
        Page<Cliente> page = new PageImpl<>(List.of(cliente));

        when(clienteRepository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<Cliente> result = service.findPage(0, 10, "nome", "ASC");

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void converterClienteDto() {
        Cliente cliente = new Cliente(1, "Carlos", "carlos@email.com", "123", TipoCliente.PESSOAFISICA);
        ClienteDto dto = new ClienteDto(cliente);
        Cliente result = service.fromDto(dto);

        assertEquals("Carlos", result.getNome());
        assertEquals("carlos@email.com", result.getEmail());
    }

    @Test
    void converterClienteNewDto() {
        ClienteNewDto dto = new ClienteNewDto();
        dto.setNome("Ana");
        dto.setEmail("ana@email.com");
        dto.setCpfOuCnpj("12345678900");
        dto.setTipo(1);
        dto.setCidadeId(1);
        dto.setLogradouro("Rua A");
        dto.setNumero("100");
        dto.setComplemento("Ap 1");
        dto.setBairro("Centro");
        dto.setCep("00000-000");
        dto.setTelefone("11111111");

        Cidade cidade = new Cidade(1, "São Paulo", null);

        when(cidadeRepository.getReferenceById(1)).thenReturn(cidade);

        Cliente result = service.fromDTO(dto);

        assertEquals("Ana", result.getNome());
        assertEquals("ana@email.com", result.getEmail());
        assertEquals(1, result.getEnderecosList().size());
        assertTrue(result.getTelefones().contains("11111111"));
    }
}