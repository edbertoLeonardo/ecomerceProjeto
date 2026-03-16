package br.com.ecomerce.ecomerce.repositories;

import br.com.ecomerce.ecomerce.model.Categoria;
import br.com.ecomerce.ecomerce.model.Produto;
import jdk.jfr.TransitionTo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

    @Transactional(readOnly = true)
    Page<Produto> findDistinctByNomeContainingAndCategoriasIn(String nome, List<Categoria> categoriasList, Pageable pageRequest);

}
