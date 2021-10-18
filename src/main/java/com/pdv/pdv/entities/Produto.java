package com.pdv.pdv.entities;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private BigDecimal preco;
    private Long quantidade;
    private String descricao;
    private String tipoProduto;
    private String cor;
    private String tamanho;
}
