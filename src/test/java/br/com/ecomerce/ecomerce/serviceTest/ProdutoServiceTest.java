package br.com.ecomerce.ecomerce.serviceTest;

import br.com.ecomerce.ecomerce.exception.ObjectNotFoundException;
import br.com.ecomerce.ecomerce.model.Categoria;
import br.com.ecomerce.ecomerce.model.Produto;
import br.com.ecomerce.ecomerce.repositories.CategoriaRepository;
import br.com.ecomerce.ecomerce.repositories.ProdutoRepository;
import br.com.ecomerce.ecomerce.service.ProdutoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @InjectMocks
    private ProdutoService produtoService;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    private Produto produto;
    private Categoria categoria;

    @BeforeEach
    void setUp() {
        produto = new Produto();
        produto.setId(1);
        produto.setNome("Notebook");

        categoria = new Categoria();
        categoria.setId(1);
        categoria.setNome("Informática");
    }



    @Test
    void retornaProdutoquandoIdExistir() {
        when(produtoRepository.findById(1)).thenReturn(Optional.of(produto));

        Produto result = produtoService.findById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
    }

    @Test
    void lancaExcecptionQuandoNaoEncontrarProduto() {
        when(produtoRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> {
            produtoService.findById(1);
        });
    }


    @Test
    void retornaPaginaDeProdutos() {
        List<Categoria> categorias = Arrays.asList(categoria);
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.Direction.ASC, "nome");

        Page<Produto> page = new PageImpl<>(Arrays.asList(produto));

        when(categoriaRepository.findAllById(Arrays.asList(1)))
                .thenReturn(categorias);

        when(produtoRepository.findDistinctByNomeContainingAndCategoriasIn(
                eq("Note"), eq(categorias), any(PageRequest.class)))
                .thenReturn(page);

        Page<Produto> result = produtoService.search(
                "Note",
                Arrays.asList(1),
                0,
                10,
                "nome",
                "ASC"
        );

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Notebook", result.getContent().get(0).getNome());
    }
}