package br.com.ecomerce.ecomerce.repositoriesTest;

import br.com.ecomerce.ecomerce.model.Cliente;
import br.com.ecomerce.ecomerce.model.enums.TipoCliente;
import br.com.ecomerce.ecomerce.repositories.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest // Sobe um banco em memória (H2) automático
public class ClienteRepositoryTest {

    @Autowired
    private ClienteRepository repository;

    @Test
    void findByEmailSucesso() {

        Cliente cliente = new Cliente(null, "João", "joao@gmail.com", "12345678901", TipoCliente.PESSOAFISICA);
        repository.save(cliente);


        Cliente encontrado = repository.findByEmail("joao@gmail.com");


        assertNotNull(encontrado);
        assertEquals("João", encontrado.getNome());
    }

    @Test
    void findByEmailInexistente() {
        Cliente encontrado = repository.findByEmail("inexistente@gmail.com");
        assertNull(encontrado);
    }
}