package br.com.ecomerce.ecomerce.repositories;

import br.com.ecomerce.ecomerce.model.Cidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Integer> {
}
