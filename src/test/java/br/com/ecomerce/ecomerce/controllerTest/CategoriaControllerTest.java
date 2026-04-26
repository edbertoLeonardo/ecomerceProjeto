package br.com.ecomerce.ecomerce.controllerTest;

import br.com.ecomerce.ecomerce.controllers.CategoriaController;
import br.com.ecomerce.ecomerce.dto.CategoriaDto;
import br.com.ecomerce.ecomerce.model.Categoria;
import br.com.ecomerce.ecomerce.service.CategoriaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;





@WebMvcTest(CategoriaController.class)
class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoriaService categoriaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void retornarCategoriaPorId() throws Exception {
        Categoria categoria = new Categoria(1, "Eletrônicos");

        when(categoriaService.find(1)).thenReturn(categoria);

        mockMvc.perform(get("/categorias/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Eletrônicos"));
    }

    @Test
    void inserirCategoria() throws Exception {
        CategoriaDto dto = new CategoriaDto();
        dto.setId(1);
        dto.setNome("Livros");

        Categoria categoria = new Categoria(1, "Livros");

        when(categoriaService.fromDto(any())).thenReturn(categoria);

        mockMvc.perform(post("/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void deletarCategoria() throws Exception {
        Mockito.doNothing().when(categoriaService).delete(1);

        mockMvc.perform(delete("/categorias/1"))
                .andExpect(status().isNoContent());
    }
}


