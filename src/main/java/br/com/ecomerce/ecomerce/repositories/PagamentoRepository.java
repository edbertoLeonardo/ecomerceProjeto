package br.com.ecomerce.ecomerce.repositories;

import br.com.ecomerce.ecomerce.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Integer> {
}
