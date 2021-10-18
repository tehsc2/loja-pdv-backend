package com.pdv.pdv.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private Produto produto;
    private Long quantidade;
}
