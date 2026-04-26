package br.com.ecomerce.ecomerce.controllerTest;

import br.com.ecomerce.ecomerce.controllers.ClienteController;
import br.com.ecomerce.ecomerce.dto.ClienteDto;
import br.com.ecomerce.ecomerce.dto.ClienteNewDto;
import br.com.ecomerce.ecomerce.model.Cliente;
import br.com.ecomerce.ecomerce.repositories.ClienteRepository;
import br.com.ecomerce.ecomerce.service.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClienteService clienteService;

    @MockitoBean
    private ClienteRepository clienteRepository;

    @Autowired
    private ObjectMapper objectMapper;

    // -------------------- GET BY ID --------------------

    @Test
    void buscarClientePorId() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setId(1);

        when(clienteService.findById(1)).thenReturn(cliente);

        mockMvc.perform(get("/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }



    @Test
    public void inserirClienteComSucesso() throws Exception {

        ClienteNewDto clienteNewDto = new ClienteNewDto();
        clienteNewDto.setNome("Leonardo");
        clienteNewDto.setEmail("leonardo@email.com");
        clienteNewDto.setCpfOuCnpj("52998224725"); // CPF válido
        clienteNewDto.setTipo(1);
        clienteNewDto.setSenha("123456");
        clienteNewDto.setLogradouro("Rua A");
        clienteNewDto.setNumero("100");
        clienteNewDto.setBairro("Centro");
        clienteNewDto.setCep("12345678");
        clienteNewDto.setTelefone("1122233553");
        clienteNewDto.setTelefone2("1122222222");
        clienteNewDto.setTelefone3("1122233553");
        clienteNewDto.setCidadeId(1);

        Cliente cliente = new Cliente();
        cliente.setId(1);

        when(clienteService.fromDTO(any(ClienteNewDto.class))).thenReturn(cliente);
        when(clienteService.insert(any(Cliente.class))).thenReturn(cliente);

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteNewDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/clientes/1"));
    }


    @Test
    void atualizarCliente() throws Exception {

        Cliente cliente = new Cliente();
        cliente.setId(1);

        when(clienteService.fromDto(any())).thenReturn(cliente);
        when(clienteService.update(any())).thenReturn(cliente);

        ClienteDto dto = new ClienteDto();
        dto.setNome("Atualizado");
        dto.setEmail("atualizado@email.com");

        mockMvc.perform(put("/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNoContent());
    }



    @Test
    void deletarCliente() throws Exception {
        Mockito.doNothing().when(clienteService).delete(1);

        mockMvc.perform(delete("/clientes/1"))
                .andExpect(status().isNoContent());
    }



    @Test
    void buscarTodosClientes() throws Exception {
        Cliente c1 = new Cliente();
        c1.setId(1);

        Cliente c2 = new Cliente();
        c2.setId(2);

        List<Cliente> lista = Arrays.asList(c1, c2);

        when(clienteService.findAll()).thenReturn(lista);

        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk());
    }



    @Test
    void buscarClientesPaginados() throws Exception {
        Page<Cliente> page = Mockito.mock(Page.class);

        when(clienteService.findPage(
                Mockito.anyInt(),
                Mockito.anyInt(),
                Mockito.anyString(),
                Mockito.anyString()
        )).thenReturn(page);

        mockMvc.perform(get("/clientes/page"))
                .andExpect(status().isOk());
    }
}