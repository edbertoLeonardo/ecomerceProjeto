package br.com.ecomerce.ecomerce.controllers;

import br.com.ecomerce.ecomerce.dto.ClienteDto;
import br.com.ecomerce.ecomerce.dto.ClienteNewDto;
import br.com.ecomerce.ecomerce.model.Cliente;
import br.com.ecomerce.ecomerce.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<Optional<Cliente>> find(@PathVariable Integer id) {
        Optional<Cliente> obj = Optional.ofNullable(clienteService.findById(id));
        return ResponseEntity.ok().body(obj);
    }

    @RequestMapping(method=RequestMethod.POST)
    public ResponseEntity<Void> insert(@Valid @RequestBody ClienteNewDto clienteNewDto) {
        Cliente cliente = clienteService.fromDTO(clienteNewDto);
        cliente = clienteService.insert(cliente);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(cliente.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Void> update(@Valid @RequestBody ClienteDto clienteDto, @PathVariable Integer id) {
        Cliente cliente = clienteService.fromDto(clienteDto);
        cliente.setId(id);
        cliente = clienteService.update(cliente);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        clienteService.delete(id);
        return ResponseEntity.noContent().build();

    }

    @GetMapping
    public ResponseEntity<List<ClienteDto>> findAll() {
        List<Cliente> clientes = clienteService.findAll();
        List<ClienteDto> categoriasDtos = clientes.stream().map( x -> new ClienteDto(x)).collect(Collectors.toList());
        return ResponseEntity.ok().body(categoriasDtos);
    }

    @GetMapping(value = "/page")
    public ResponseEntity<Page<ClienteDto>> findPage(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue = "nome") String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC")String direction) {
        Page<Cliente> categorias = clienteService.findPage(page, linesPerPage, orderBy, direction);
        Page<ClienteDto> categoriasDtos = categorias.map( x -> new ClienteDto(x));
        return ResponseEntity.ok().body(categoriasDtos);
    }
}
