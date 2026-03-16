package br.com.ecomerce.ecomerce.controllers;

import br.com.ecomerce.ecomerce.dto.CategoriaDto;
import br.com.ecomerce.ecomerce.model.Categoria;
import br.com.ecomerce.ecomerce.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;



    @GetMapping(value = "/{id}")
    public ResponseEntity<Categoria> find(@PathVariable Integer id) {
        Categoria obj = categoriaService.find(id);
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping
    public ResponseEntity<Void> insert(@Valid @RequestBody CategoriaDto categoriaDto){
        Categoria categoria = categoriaService.fromDto(categoriaDto);
        categoriaService.insert(categoria);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(categoriaDto.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Void> update(@Valid @RequestBody CategoriaDto categoriaDto, @PathVariable Integer id) {
        Categoria categoria = categoriaService.fromDto(categoriaDto);
        categoria.setId(id);
        categoria = categoriaService.update(categoria);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
      categoriaService.delete(id);
      return ResponseEntity.noContent().build();

    }

    @GetMapping
    public ResponseEntity<List<CategoriaDto>> findAll() {
        List<Categoria> categorias = categoriaService.findAll();
        List<CategoriaDto> categoriasDtos = categorias.stream().map( x -> new CategoriaDto(x)).collect(Collectors.toList());
        return ResponseEntity.ok().body(categoriasDtos);
    }

    @GetMapping(value = "/page")
    public ResponseEntity<Page<CategoriaDto>> findPage(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue = "nome") String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC")String direction) {
        Page<Categoria> categorias = categoriaService.findPage(page, linesPerPage, orderBy, direction);
        Page<CategoriaDto> categoriasDtos = categorias.map( x -> new CategoriaDto(x));
        return ResponseEntity.ok().body(categoriasDtos);
    }

    }
