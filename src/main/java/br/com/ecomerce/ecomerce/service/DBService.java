package br.com.ecomerce.ecomerce.service;

import br.com.ecomerce.ecomerce.model.*;
import br.com.ecomerce.ecomerce.model.enums.EstadoPagamento;
import br.com.ecomerce.ecomerce.model.enums.TipoCliente;
import br.com.ecomerce.ecomerce.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

@Service
public class DBService {



    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private CidadeRepository cidadeRepository;
    @Autowired
    private EstadoRepository estadoRepository;
    @Autowired
    private EnderecoRepository enderecoRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private PagamentoRepository pagamentoRepository;
    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    public void instantiateTestDatabase() throws ParseException {

        Categoria cat1 = new Categoria(null, "Infomática");
        Categoria cat2 = new Categoria(null, "Escritório");
        Categoria cat3 = new Categoria(null, "Cama mesa e banho");
        Categoria cat4 = new Categoria(null, "Eletrônicos");
        Categoria cat5 = new Categoria(null, "Jardinagem");
        Categoria cat6 = new Categoria(null, "Decoração");
        Categoria cat7 = new Categoria(null, "Perfumaria");

        Produto p1 = new Produto(null, "Computador", 2000.00);
        Produto p2 = new Produto(null, "Impressora", 800.00);
        Produto p3 = new Produto(null, "Mouse", 70.00);
        Produto p4 = new Produto(null, "Mesa de escritório", 300.00);
        Produto p5 = new Produto(null, "Toalha", 50.00);
        Produto p6 = new Produto(null, "Colcha", 100.00);
        Produto p7 = new Produto(null, "Tv Led", 1500.00);
        Produto p8 = new Produto(null, "Roçadeira", 800.00);
        Produto p9 = new Produto(null, "Abajour", 100.00);
        Produto p10 = new Produto(null, "Pendente", 180.00);
        Produto p11 = new Produto(null, "Shampoo", 90.00);

        cat1.getProdutosList().addAll(Arrays.asList(p1,p2, p3));
        cat2.getProdutosList().addAll(Arrays.asList(p2, p4));
        cat3.getProdutosList().addAll(Arrays.asList(p5, p6));
        cat4.getProdutosList().addAll(Arrays.asList(p1, p2,p3, p7));
        cat5.getProdutosList().addAll(Arrays.asList(p8));
        cat6.getProdutosList().addAll(Arrays.asList(p9, p10));
        cat7.getProdutosList().addAll(Arrays.asList(p11));

        p1.getCategorias().addAll(Arrays.asList(cat1, cat4));
        p2.getCategorias().addAll(Arrays.asList(cat1, cat2, cat4));
        p3.getCategorias().addAll(Arrays.asList(cat1, cat4));
        p4.getCategorias().addAll(Arrays.asList(cat2));
        p5.getCategorias().addAll(Arrays.asList(cat3));
        p6.getCategorias().addAll(Arrays.asList(cat3));
        p7.getCategorias().addAll(Arrays.asList(cat4));
        p8.getCategorias().addAll(Arrays.asList(cat5));
        p9.getCategorias().addAll(Arrays.asList(cat6));
        p10.getCategorias().addAll(Arrays.asList(cat6));
        p11.getCategorias().addAll(Arrays.asList(cat7));

        categoriaRepository.saveAll(Arrays.asList(cat1, cat2, cat3, cat4, cat5, cat6, cat7));
        produtoRepository.saveAll(Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11));

        Estado estado = new Estado(null, "Minas Gerais");
        Estado estado2 = new Estado(null, "São Paulo");

        Cidade cidade = new Cidade(null, "Uberlândia", estado);
        Cidade cidade2 = new Cidade(null, "São Paulo", estado2);
        Cidade cidade3 = new Cidade(null, "Caminas", estado2);

        estado.getCidades().addAll(Arrays.asList(cidade));
        estado2.getCidades().addAll(Arrays.asList(cidade2, cidade3));

        estadoRepository.saveAll(Arrays.asList(estado, estado2));
        cidadeRepository.saveAll(Arrays.asList(cidade, cidade2, cidade3));

        Cliente cliente = new Cliente(null, "Maria Silva", "edbleonardo@gmail.com", "123456789", TipoCliente.PESSOAFISICA);
        cliente.getTelefones().addAll(Arrays.asList("11122233", "22233344"));

        Endereco endereco = new Endereco(null, "Rua Flores", "300", "Ap 20", "Jardim", "05810-100", cliente, cidade);
        Endereco endereco2 = new Endereco(null, "Rua Sete", "10", "Sala 8", "Centro", "05810-200", cliente, cidade2);

        cliente.getEnderecosList().addAll(Arrays.asList(endereco, endereco2));

        clienteRepository.saveAll(Arrays.asList(cliente));
        enderecoRepository.saveAll(Arrays.asList(endereco, endereco2));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        Pedido pedido = new Pedido(null, sdf.parse("10/10/2020 10:30"), cliente, endereco);
        Pedido pedido2 = new Pedido(null, sdf.parse("11/10/2020 10:40"), cliente, endereco2);

        Pagamento pagamento = new PagamentoComCartao(null, EstadoPagamento.QUITADO, pedido, 6);
        pedido.setPagamento(pagamento);
        Pagamento pagamento2 = new PagamentoComBoleto(null, EstadoPagamento.PENDENTE, pedido2, sdf.parse("25/10/2025 00:00"), null);
        pedido2.setPagamento(pagamento2);

        cliente.getPedidosList().addAll(Arrays.asList(pedido, pedido2));

        pedidoRepository.saveAll(Arrays.asList(pedido, pedido2));
        pagamentoRepository.saveAll(Arrays.asList(pagamento, pagamento2));

        ItemPedido itemPedido = new ItemPedido(pedido, p1, 1, 0.00, 2000.00);
        ItemPedido itemPedido2 = new ItemPedido(pedido, p3, 2, 0.00, 80.00);
        ItemPedido itemPedido3 = new ItemPedido(pedido2, p2, 1, 100.00, 800.00);

        pedido.getItensDoPedido().addAll(Arrays.asList(itemPedido, itemPedido2));
        pedido2.getItensDoPedido().addAll(Arrays.asList(itemPedido3));

        p1.getItensDoPedido().addAll(Arrays.asList(itemPedido));
        p2.getItensDoPedido().addAll(Arrays.asList(itemPedido3));
        p3.getItensDoPedido().addAll(Arrays.asList(itemPedido2));

        itemPedidoRepository.saveAll(Arrays.asList(itemPedido, itemPedido2, itemPedido3));

    }
}
