package br.com.ecomerce.ecomerce.repositoriesTest;

import br.com.ecomerce.ecomerce.model.Categoria;
import br.com.ecomerce.ecomerce.model.Produto;
import br.com.ecomerce.ecomerce.repositories.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
public class ProdutoRepositoryTest {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private TestEntityManager entityManager; // Auxiliar para salvar dependências

    @Test
    void buscarProdutosPorNomeECategorias() {
        // Arrange
        Categoria cat1 = new Categoria(null, "Informática");
        entityManager.persist(cat1);

        Produto p1 = new Produto(null, "Computador", 2000.0);
        p1.getCategorias().add(cat1);
        entityManager.persist(p1);

        // Act
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Produto> result = produtoRepository.findDistinctByNomeContainingAndCategoriasIn(
                "Comput",
                List.of(cat1),
                pageRequest
        );

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
        assertEquals("Computador", result.getContent().get(0).getNome());
    }
}