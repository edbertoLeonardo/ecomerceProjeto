package br.com.ecomerce.ecomerce.modelTest;

import br.com.ecomerce.ecomerce.model.ItemPedido;
import br.com.ecomerce.ecomerce.model.Pedido;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class Pedidotest {

    @Test
    void calcularValorTotalDoPedido() {

        Pedido pedido = new Pedido();

        ItemPedido item1 = mock(ItemPedido.class);
        ItemPedido item2 = mock(ItemPedido.class);

        when(item1.getSubTotal()).thenReturn(100.0);
        when(item2.getSubTotal()).thenReturn(250.50);

        pedido.getItensDoPedido().add(item1);
        pedido.getItensDoPedido().add(item2);


        double total = pedido.getValorTotal();
        assertEquals(350.50, total);
    }
}
