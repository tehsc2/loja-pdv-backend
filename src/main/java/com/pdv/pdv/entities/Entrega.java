package com.pdv.pdv.entities;

import com.pdv.pdv.entities.enums.TipoEntregaEnum;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
public class Entrega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private TipoEntregaEnum tipoEntrega;
    private BigDecimal valorEntrega;
    private String endereco;
    private String nomeCliente;
}
