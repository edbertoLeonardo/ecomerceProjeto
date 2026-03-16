package br.com.ecomerce.ecomerce.service;

import br.com.ecomerce.ecomerce.dto.CategoriaDto;
import br.com.ecomerce.ecomerce.exception.DataIntegrityException;
import br.com.ecomerce.ecomerce.exception.ObjectNotFoundException;
import br.com.ecomerce.ecomerce.model.Categoria;
import br.com.ecomerce.ecomerce.model.Cliente;
import br.com.ecomerce.ecomerce.repositories.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    CategoriaRepository categoriaRepository;


    public Categoria find(Integer id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Categoria não encontrada com ID: " + id
                +   ", Tipo: " + Categoria.class.getName()));
    }

    public Categoria insert(Categoria categoria){
        categoria.setId(null);
        return categoriaRepository.save(categoria);
    }

//    public Categoria update(Categoria categoria){
//        find(categoria.getId());
//        return categoriaRepository.save(categoria);
//    }

    public Categoria update(Categoria categoria){
        Categoria newCategoria = find(categoria.getId());
        updateData(newCategoria, categoria);
        return categoriaRepository.save(newCategoria);
    }

private void updateData(Categoria newCategoria, Categoria categoria) {
    newCategoria.setNome(categoria.getNome());
    }
    public void delete(Integer id){
        find(id);
        try {
            categoriaRepository.deleteById(id);
        }catch (DataIntegrityViolationException e){
            throw new DataIntegrityException("Não é possível excluir uma categoria com produtos");
        }
    }

    public List<Categoria> findAll(){
        return categoriaRepository.findAll();
    }

    public Page<Categoria> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
        return categoriaRepository.findAll(pageRequest);
    }

    public Categoria fromDto(CategoriaDto categoriaDto){
        return new Categoria(categoriaDto.getId(), categoriaDto.getNome());
    }
}
