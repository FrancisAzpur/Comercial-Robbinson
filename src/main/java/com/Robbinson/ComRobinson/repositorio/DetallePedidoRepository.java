package com.Robbinson.ComRobinson.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Robbinson.ComRobinson.modelo.DetallePedido;

@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {

    List<DetallePedido> findByPedidoIdPedido(Long idPedido);

    List<DetallePedido> findByProductoIdProducto(Long idProducto);

    void deleteByPedidoIdPedido(Long idPedido);
}