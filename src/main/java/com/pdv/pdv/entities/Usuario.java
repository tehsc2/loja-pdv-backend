package com.pdv.pdv.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Usuario {

    @Id
    @GeneratedValue
    private Long id;
    private String usuario;
    private String senha;
    private String idLoja;
}
