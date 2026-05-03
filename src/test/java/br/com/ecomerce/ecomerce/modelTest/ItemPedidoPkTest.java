package br.com.ecomerce.ecomerce.modelTest;

import br.com.ecomerce.ecomerce.model.ItemPedidoPk;
import br.com.ecomerce.ecomerce.model.Pedido;
import br.com.ecomerce.ecomerce.model.Produto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ItemPedidoPkTest {

    @Test
    void chavesIguaisDevemTerMesmoHashCodeEEsquals() {
        Pedido ped = new Pedido();
        ped.setId(1);
        Produto prod = new Produto();
        prod.setId(10);

        ItemPedidoPk pk1 = new ItemPedidoPk();
        pk1.setPedido(ped);
        pk1.setProduto(prod);

        ItemPedidoPk pk2 = new ItemPedidoPk();
        pk2.setPedido(ped);
        pk2.setProduto(prod);

        // Verifica se o equals e hashCode estão funcionando para o JPA
        assertEquals(pk1, pk2);
        assertEquals(pk1.hashCode(), pk2.hashCode());
    }
}
