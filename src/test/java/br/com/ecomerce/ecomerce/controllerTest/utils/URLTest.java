package br.com.ecomerce.ecomerce.controllerTest.utils;

import br.com.ecomerce.ecomerce.controllers.utils.URL;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class URLTest {

    @Test
    void decodificarEspacos() {
        String result = URL.decodeParam("teste%20abc");
        assertEquals("teste abc", result);
    }

    @Test
    void manterStringNormal() {
        String result = URL.decodeParam("abc");
        assertEquals("abc", result);
    }

    @Test
    void decodificarCaracteresEspeciais() {
        String result = URL.decodeParam("Jo%C3%A3o");
        assertEquals("João", result);
    }

    @Test
    void converterListaSimples() {
        List<Integer> result = URL.decodeIntList("1,2,3");
        assertEquals(List.of(1, 2, 3), result);
    }

    @Test
    void ignorarEspacos() {
        List<Integer> result = URL.decodeIntList("1, 2 , 3");
        assertEquals(List.of(1, 2, 3), result);
    }

    @Test
    void ignorarValoresVazios() {
        List<Integer> result = URL.decodeIntList("1,,2,");
        assertEquals(List.of(1, 2), result);
    }

    @Test
    void retornarListaVaziaQuandoStringVazia() {
        List<Integer> result = URL.decodeIntList("");
        assertTrue(result.isEmpty());
    }

    @Test
    void retornarListaVaziaQuandoNull() {
        List<Integer> result = URL.decodeIntList(null);
        assertTrue(result.isEmpty());
    }

    @Test
    void lancarExcecaoQuandoValorInvalido() {
        assertThrows(NumberFormatException.class, () -> {
            URL.decodeIntList("1,a,3");
        });
    }
}