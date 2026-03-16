package br.com.ecomerce.ecomerce.repositories;

import br.com.ecomerce.ecomerce.model.Cidade;
import br.com.ecomerce.ecomerce.model.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Integer> {
}
