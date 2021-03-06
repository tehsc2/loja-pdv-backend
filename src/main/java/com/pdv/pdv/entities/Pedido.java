package com.pdv.pdv.entities;

import com.pdv.pdv.entities.enums.FormaPagamentoEnum;
import com.pdv.pdv.entities.enums.StatusPedidoEnum;
import com.pdv.pdv.exceptions.ProdutoSemEstoqueException;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Usuario vendedor;
    @Enumerated(EnumType.STRING)
    private StatusPedidoEnum statusPedido;
    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "pedido_id")
    private List<ItemPedido> itensPedido;
    private LocalDateTime dataPedido;
    @Enumerated(EnumType.STRING)
    private FormaPagamentoEnum formaPagamento;
    private BigDecimal valorTotal;
    @OneToOne(cascade = CascadeType.PERSIST)
    private Entrega entrega;

    public void calcularValorTotal(){
        this.setValorTotal(this.getItensPedido().stream()
                .map(itemPedido -> itemPedido.getProduto().getPreco().multiply(BigDecimal.valueOf(itemPedido.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .add(this.getEntrega().getValorEntrega()));
    }

    public List<String> verificaProdutosSemEstoque(){
        return this.getItensPedido().stream()
                .filter(itemPedido -> itemPedido.getQuantidade() > itemPedido.getProduto().getQuantidade())
                .map(itemPedido -> itemPedido.getProduto().getNome())
                .collect(Collectors.toList());
    }

    public void removeProdutosEstoque() {
        this.getItensPedido().forEach(itemPedido -> {
            Produto produto = itemPedido.getProduto();

            long quantidade = produto.getQuantidade() - itemPedido.getQuantidade();

            if (quantidade >= 0) {
                produto.setQuantidade(quantidade);
            } else {
                throw new ProdutoSemEstoqueException("PRODUTO SEM ESTOQUE DISPONIVEL", produto.getNome());
            }
        });
    }
}
