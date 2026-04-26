package br.com.ecomerce.ecomerce.serviceTest;

import br.com.ecomerce.ecomerce.exception.DataIntegrityException;
import br.com.ecomerce.ecomerce.exception.ObjectNotFoundException;
import br.com.ecomerce.ecomerce.model.Categoria;
import br.com.ecomerce.ecomerce.repositories.CategoriaRepository;
import br.com.ecomerce.ecomerce.service.CategoriaService;
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
class CategoriaServiceTest {

    @InjectMocks
    private CategoriaService service;

    @Mock
    private CategoriaRepository repository;

    private Categoria categoria;

    @BeforeEach
    void setUp() {
        categoria = new Categoria(1, "Informática");
    }

    @Test
    void retornarCategoriaQuandoIdExistir() {
        when(repository.findById(1)).thenReturn(Optional.of(categoria));

        Categoria result = service.find(1);

        assertNotNull(result);
        assertEquals("Informática", result.getNome());
    }

    @Test
    void lancarExcecaoQuandoIdNaoExistir() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> {
            service.find(1);
        });
    }

    @Test
    void inserirCategoriaComIdNulo() {
        Categoria nova = new Categoria(null, "Livros");

        when(repository.save(any())).thenReturn(nova);

        Categoria result = service.insert(nova);

        assertNull(result.getId());
        assertEquals("Livros", result.getNome());
        verify(repository).save(nova);
    }

    @Test
    void atualizarCategoria() {
        Categoria atualizada = new Categoria(1, "Eletrônicos");

        when(repository.findById(1)).thenReturn(Optional.of(categoria));
        when(repository.save(any())).thenReturn(atualizada);

        Categoria result = service.update(atualizada);

        assertEquals("Eletrônicos", result.getNome());
        verify(repository).save(any());
    }

    @Test
    void deletarCategoriaComSucesso() {
        when(repository.findById(1)).thenReturn(Optional.of(categoria));

        service.delete(1);

        verify(repository).deleteById(1);
    }

    @Test
    void lancarDataIntegrityExceptionAoDeletar() {
        when(repository.findById(1)).thenReturn(Optional.of(categoria));
        doThrow(DataIntegrityViolationException.class)
                .when(repository).deleteById(1);

        assertThrows(DataIntegrityException.class, () -> {
            service.delete(1);
        });
    }

    @Test
    void retornarListaDeCategorias() {
        when(repository.findAll()).thenReturn(List.of(categoria));

        List<Categoria> lista = service.findAll();

        assertFalse(lista.isEmpty());
        assertEquals(1, lista.size());
    }

    @Test
    void retornarPaginaDeCategorias() {
        Page<Categoria> page = new PageImpl<>(List.of(categoria));

        when(repository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<Categoria> result = service.findPage(0, 10, "nome", "ASC");

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }
}