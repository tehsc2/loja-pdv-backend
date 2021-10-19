package com.pdv.pdv.repositories;

import com.pdv.pdv.entities.Pedido;
import com.pdv.pdv.entities.enums.StatusPedidoEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findAllByVendedorId(Long id);

    List<Pedido> findAllByStatusPedidoAndVendedorId(StatusPedidoEnum status, Long idUsuario);
}
