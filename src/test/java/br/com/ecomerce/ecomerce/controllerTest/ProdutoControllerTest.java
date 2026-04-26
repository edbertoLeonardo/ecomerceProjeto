package br.com.ecomerce.ecomerce.controllerTest;

import br.com.ecomerce.ecomerce.controllers.ProdutoController;
import br.com.ecomerce.ecomerce.model.Produto;
import br.com.ecomerce.ecomerce.service.ProdutoService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProdutoController.class)
class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProdutoService produtoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void buscarProdutoPorId() throws Exception {
        Produto produto = new Produto();
        produto.setId(1);
        produto.setNome("Notebook");

        when(produtoService.findById(1)).thenReturn(produto);

        mockMvc.perform(get("/produtos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Notebook"));
    }

    @Test
    void retornarPaginaDeProdutos() throws Exception {
        Produto p1 = new Produto();
        p1.setId(1);
        p1.setNome("Notebook");

        Produto p2 = new Produto();
        p2.setId(2);
        p2.setNome("Mouse");

        List<Produto> lista = Arrays.asList(p1, p2);

        Page<Produto> page = new PageImpl<>(lista, PageRequest.of(0, 24), lista.size());

        when(produtoService.search(
                "", List.of(), 0, 24, "nome", "ASC"
        )).thenReturn(page);

        mockMvc.perform(get("/produtos")
                        .param("nome", "")
                        .param("categorias", "")
                        .param("page", "0")
                        .param("linesPerPage", "24")
                        .param("orderBy", "nome")
                        .param("direction", "ASC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].nome").value("Notebook"))
                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.content[1].nome").value("Mouse"));
    }
}