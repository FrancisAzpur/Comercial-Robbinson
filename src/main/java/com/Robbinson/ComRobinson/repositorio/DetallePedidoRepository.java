package com.Robbinson.ComRobinson.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Robbinson.ComRobinson.modelo.DetallePedido;

/**
 * =========================================================================
 * REPOSITORIO DE DETALLE PEDIDO - Tabla intermedia 'detalle_pedidos'
 * =========================================================================
 * PUNTO DE EVALUACIÓN: Consultas multi-tabla (DetallePedido → Pedido/Producto)
 * 
 * Permite buscar los productos de un pedido o los pedidos de un producto.
 * =========================================================================
 */
@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {

    /** Obtener todos los detalles/productos de un pedido específico */
    List<DetallePedido> findByPedidoIdPedido(Long idPedido);

    /** Obtener todos los pedidos donde aparece un producto */
    List<DetallePedido> findByProductoIdProducto(Long idProducto);

    /** Eliminar todos los detalles de un pedido */
    void deleteByPedidoIdPedido(Long idPedido);
}