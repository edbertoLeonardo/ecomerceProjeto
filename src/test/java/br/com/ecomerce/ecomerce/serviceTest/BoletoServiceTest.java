package br.com.ecomerce.ecomerce.serviceTest;

import br.com.ecomerce.ecomerce.model.PagamentoComBoleto;
import br.com.ecomerce.ecomerce.service.BoletoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BoletoServiceTest {

    private BoletoService boletoService;

    @BeforeEach
    void setUp() {
        boletoService = new BoletoService();
    }

    @Test
    void calcularDataVencimentoParaSeteDiasDepois() {

        PagamentoComBoleto pagto = new PagamentoComBoleto();


        Calendar cal = Calendar.getInstance();
        cal.set(2024, Calendar.JANUARY, 1, 10, 0, 0);
        Date instantePedido = cal.getTime();

        boletoService.preencherPagamentoComBoleto(pagto, instantePedido);


        Calendar resultCal = Calendar.getInstance();
        resultCal.setTime(pagto.getDataVencimento());

        assertEquals(2024, resultCal.get(Calendar.YEAR));
        assertEquals(Calendar.JANUARY, resultCal.get(Calendar.MONTH));
        assertEquals(8, resultCal.get(Calendar.DAY_OF_MONTH));
    }
}