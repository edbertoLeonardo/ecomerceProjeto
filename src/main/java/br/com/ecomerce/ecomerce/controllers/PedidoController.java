package br.com.ecomerce.ecomerce.controllers;

import br.com.ecomerce.ecomerce.model.Pedido;
import br.com.ecomerce.ecomerce.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;



    @GetMapping(value = "/{id}")
    public ResponseEntity<Pedido> find(@PathVariable Integer id) {
        Pedido obj = pedidoService.findById(id);
        return ResponseEntity.ok().body(obj);
    }



    @PostMapping
    public ResponseEntity<Void> insert(@Valid @RequestBody Pedido pedido) {
        pedido = pedidoService.insert(pedido);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(pedido.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }
    @RequestMapping(method=RequestMethod.GET)
    public ResponseEntity<Page<Pedido>> findPage(
            @RequestParam(value="page", defaultValue="0") Integer page,
            @RequestParam(value="linesPerPage", defaultValue="24") Integer linesPerPage,
            @RequestParam(value="orderBy", defaultValue="instante") String orderBy,
            @RequestParam(value="direction", defaultValue="DESC") String direction) {
        Page<Pedido> list = pedidoService.findPage(page, linesPerPage, orderBy, direction);
        return ResponseEntity.ok().body(list);
    }

}
