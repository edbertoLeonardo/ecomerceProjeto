package br.com.ecomerce.ecomerce.service;

import br.com.ecomerce.ecomerce.exception.ObjectNotFoundException;
import br.com.ecomerce.ecomerce.model.ItemPedido;
import br.com.ecomerce.ecomerce.model.PagamentoComBoleto;
import br.com.ecomerce.ecomerce.model.Pedido;
import br.com.ecomerce.ecomerce.model.enums.EstadoPagamento;
import br.com.ecomerce.ecomerce.repositories.ItemPedidoRepository;
import br.com.ecomerce.ecomerce.repositories.PagamentoRepository;
import br.com.ecomerce.ecomerce.repositories.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private BoletoService boletoService;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    public Pedido findById(Integer id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Pedido não encontrada com ID: " + id
                        +   ", Tipo: " + Pedido.class.getName()));
    }


//    public Pedido insert(Pedido pedido) {
//        pedido.setId(null);
//        pedido.setInstante(new Date());
//        pedido.setCliente(clienteService.findById(pedido.getCliente().getId()));
//        pedido.getPagamento().setEstadoPagamento(EstadoPagamento.PENDENTE);
//        pedido.getPagamento().setPedido(pedido);
//        if (pedido.getPagamento() instanceof PagamentoComBoleto) {
//            PagamentoComBoleto pagto = (PagamentoComBoleto) pedido.getPagamento();
//            boletoService.preencherPagamentoComBoleto(pagto, pedido.getInstante());
//        }
//        pedido = pedidoRepository.save(pedido);
//        pagamentoRepository.save(pedido.getPagamento());
//        for (ItemPedido ip : pedido.getItensDoPedido()) {
//            ip.setDesconto(0.0);
//            ip.setProduto(produtoService.findById(ip.getProduto().getId()));
//            ip.setPreco(ip.getProduto().getPreco());
//            ip.setPedido(pedido);
//        }
//        itemPedidoRepository.saveAll(pedido.getItensDoPedido());
//        System.out.println(pedido);
////emailService.sendOrderConfirmationEmail(pedido);
//        return pedido;
//    }

public Pedido insert(Pedido pedido) {
    pedido.setId(null);
    pedido.setInstante(new Date());
    pedido.setCliente(clienteService.findById(pedido.getCliente().getId()));
    pedido.getPagamento().setEstadoPagamento(EstadoPagamento.PENDENTE);
    pedido.getPagamento().setPedido(pedido);

    if (pedido.getPagamento() instanceof PagamentoComBoleto pagto) {
        boletoService.preencherPagamentoComBoleto(pagto, pedido.getInstante());
    }


    for (ItemPedido ip : pedido.getItensDoPedido()) {
        ip.setDesconto(0.0);
        ip.setProduto(produtoService.findById(ip.getProduto().getId()));
        ip.setPreco(ip.getProduto().getPreco());
        ip.setPedido(pedido);
    }


    pedido = pedidoRepository.save(pedido);
    pagamentoRepository.save(pedido.getPagamento());
    itemPedidoRepository.saveAll(pedido.getItensDoPedido());

    System.out.println(pedido);
    emailService.sendOrderConfirmationHtmlEmail(pedido);
    return pedido;
}

}
