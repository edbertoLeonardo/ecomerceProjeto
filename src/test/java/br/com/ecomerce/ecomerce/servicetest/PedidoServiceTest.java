package br.com.ecomerce.ecomerce.servicetest;


import br.com.ecomerce.ecomerce.model.*;
import br.com.ecomerce.ecomerce.model.enums.EstadoPagamento;
import br.com.ecomerce.ecomerce.repositories.ItemPedidoRepository;
import br.com.ecomerce.ecomerce.repositories.PagamentoRepository;
import br.com.ecomerce.ecomerce.repositories.PedidoRepository;
import br.com.ecomerce.ecomerce.service.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @InjectMocks
    private PedidoService service;

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private BoletoService boletoService;

    @Mock
    private PagamentoRepository pagamentoRepository;

    @Mock
    private ProdutoService produtoService;

    @Mock
    private ClienteService clienteService;

    @Mock
    private EmailService emailService;

    @Mock
    private ItemPedidoRepository itemPedidoRepository;


    @Test
    void inserirPedidoComSucesso() {

        Pedido pedido = new Pedido();
        Cliente cliente = new Cliente();
        cliente.setId(1);
        pedido.setCliente(cliente);

        Pagamento pagamento = new PagamentoComCartao();
        pedido.setPagamento(pagamento);

        Produto produto = new Produto();
        produto.setId(1);
        produto.setPreco(100.0);

        ItemPedido item = new ItemPedido();
        item.setProduto(produto);
        item.setQuantidade(1);


        Set<ItemPedido> itens = new HashSet<>();
        itens.add(item);
        pedido.setItensDoPedido(itens);


        when(clienteService.findById(1)).thenReturn(cliente);
        when(produtoService.findById(1)).thenReturn(produto);
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);


        Pedido resultado = service.insert(pedido);


        assertNotNull(resultado);
        assertEquals(EstadoPagamento.PENDENTE, resultado.getPagamento().getEstadoPagamento());


        ItemPedido itemResultante = resultado.getItensDoPedido().iterator().next();
        assertEquals(100.0, itemResultante.getPreco());
        assertEquals(0.0, itemResultante.getDesconto());


        verify(pedidoRepository).save(any());
        verify(pagamentoRepository).save(any());
        verify(itemPedidoRepository).saveAll(any());
        verify(emailService).sendOrderConfirmationHtmlEmail(any());
    }



}