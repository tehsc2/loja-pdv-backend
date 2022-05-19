package com.pdv.pdv.entities;


import com.pdv.pdv.exceptions.ProdutoSemEstoqueException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(JUnit4.class)
public class PedidoTest {

    @Test
    public void testCalculoValorTotalPedido() {
        Pedido pedido = novoPedidoMock(10L);

        pedido.calcularValorTotal();

        assertEquals(BigDecimal.valueOf(100), pedido.getValorTotal());
    }

    @Test
    public void testVerificacaoProdutosSemEstoque() {
        Pedido pedido = novoPedidoMock(9L);

        assertTrue(pedido.verificaProdutosSemEstoque().size() == 1);
    }

    @Test(expected = ProdutoSemEstoqueException.class)
    public void testProdutoSemEstoque() {
        Pedido pedido = novoPedidoMock(9L);
        pedido.removeProdutosEstoque();
    }

    @Test
    public void testProdutoComEstoque() {
        Pedido pedido = novoPedidoMock(20L);
        pedido.removeProdutosEstoque();

        assertEquals(10L, pedido.getItensPedido().get(0).getProduto().getQuantidade());
    }

    private Pedido novoPedidoMock(long quantidadeProduto) {
        Pedido pedido = new Pedido();
        List<ItemPedido> itemsPedido = new ArrayList<>();
        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setId(1L);
        itemPedido.setQuantidade(10L);
        Produto produto = new Produto();
        produto.setNome("Bolsa");
        produto.setQuantidade(quantidadeProduto);
        produto.setPreco(BigDecimal.TEN);
        itemPedido.setProduto(produto);
        itemsPedido.add(itemPedido);
        Entrega entrega = new Entrega();
        entrega.setValorEntrega(BigDecimal.ZERO);
        pedido.setEntrega(entrega);
        pedido.setItensPedido(itemsPedido);
        return pedido;
    }
}