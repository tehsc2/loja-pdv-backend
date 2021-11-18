package com.pdv.pdv.repositories;

import com.pdv.pdv.entities.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    List<Produto> findAllByIdLoja(Long idLoja);
}
