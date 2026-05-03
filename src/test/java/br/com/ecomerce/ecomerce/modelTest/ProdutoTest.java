package br.com.ecomerce.ecomerce.modelTest;

import br.com.ecomerce.ecomerce.model.ItemPedido;
import br.com.ecomerce.ecomerce.model.Pedido;
import br.com.ecomerce.ecomerce.model.Produto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class ProdutoTest {

    @Test
    void retornarListaDePedidos() {

        Produto p1 = new Produto(1, "Notebook", 3000.0);
        Pedido ped1 = new Pedido();
        ItemPedido item1 = mock(ItemPedido.class);
        when(item1.getPedido()).thenReturn(ped1);

        p1.getItensDoPedido().add(item1);


        List<Pedido> resultado = p1.getPedidos();


        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals(ped1, resultado.get(0));
    }

}
