package com.Robbinson.ComRobinson.repositorio;

import com.Robbinson.ComRobinson.modelo.DetallePedido;
import com.Robbinson.ComRobinson.modelo.Pedido;
import com.Robbinson.ComRobinson.modelo.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para la entidad DetallePedido
 * Proporciona operaciones CRUD y consultas personalizadas
 */
@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {

    // Obtener detalles por pedido
    List<DetallePedido> findByPedido(Pedido pedido);

    // Obtener detalles por ID de pedido
    List<DetallePedido> findByPedidoIdPedido(Long pedidoId);

    // Obtener detalles por producto
    List<DetallePedido> findByProducto(Producto producto);

    // Obtener detalles por ID de producto
    List<DetallePedido> findByProductoIdProducto(Long productoId);

    // Contar cuántas veces se ha vendido un producto
    @Query("SELECT SUM(d.cantidad) FROM DetallePedido d WHERE d.producto.idProducto = :productoId")
    Long contarUnidadesVendidasPorProducto(@Param("productoId") Long productoId);

    // Obtener productos más vendidos
    @Query("SELECT d.producto, SUM(d.cantidad) as total FROM DetallePedido d GROUP BY d.producto ORDER BY total DESC")
    List<Object[]> findProductosMasVendidos();

    // Eliminar detalles de un pedido
    void deleteByPedido(Pedido pedido);
}
