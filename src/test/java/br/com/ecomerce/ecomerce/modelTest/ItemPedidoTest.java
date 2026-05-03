package br.com.ecomerce.ecomerce.modelTest;

import br.com.ecomerce.ecomerce.model.ItemPedido;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ItemPedidoTest {

    @Test
    void calcularSubTotalComDesconto() {

        ItemPedido item = new ItemPedido(null, null, 2, 10.0, 100.0);
        double resultado = item.getSubTotal();
        assertEquals(180.0, resultado);
    }

    @Test
    void calcularSubTotalSemDesconto() {
        ItemPedido item = new ItemPedido(null, null, 3, 0.0, 50.0);
        assertEquals(150.0, item.getSubTotal());
    }
}
