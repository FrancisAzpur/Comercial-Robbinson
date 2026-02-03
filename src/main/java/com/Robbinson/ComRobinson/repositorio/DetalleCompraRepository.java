package com.Robbinson.ComRobinson.repositorio;

import com.Robbinson.ComRobinson.modelo.DetalleCompra;
import com.Robbinson.ComRobinson.modelo.Compra;
import com.Robbinson.ComRobinson.modelo.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para la entidad DetalleCompra
 * Proporciona operaciones CRUD y consultas personalizadas
 */
@Repository
public interface DetalleCompraRepository extends JpaRepository<DetalleCompra, Long> {

    // Obtener detalles por compra
    List<DetalleCompra> findByCompra(Compra compra);

    // Obtener detalles por ID de compra
    List<DetalleCompra> findByCompraIdCompra(Long compraId);

    // Obtener detalles por producto
    List<DetalleCompra> findByProducto(Producto producto);

    // Obtener detalles por ID de producto
    List<DetalleCompra> findByProductoIdProducto(Long productoId);

    // Contar cuántas unidades se han comprado de un producto
    @Query("SELECT SUM(d.cantidad) FROM DetalleCompra d WHERE d.producto.idProducto = :productoId")
    Long contarUnidadesCompradasPorProducto(@Param("productoId") Long productoId);

    // Eliminar detalles de una compra
    void deleteByCompra(Compra compra);
}
