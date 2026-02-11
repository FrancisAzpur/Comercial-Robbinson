package com.Robbinson.ComRobinson.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Robbinson.ComRobinson.modelo.DetalleCompra;

/**
 * =========================================================================
 * REPOSITORIO DE DETALLE COMPRA - Tabla intermedia 'detalle_compras'
 * =========================================================================
 * PUNTO DE EVALUACIÓN: Consultas multi-tabla (DetalleCompra → Compra/Producto)
 * =========================================================================
 */
@Repository
public interface DetalleCompraRepository extends JpaRepository<DetalleCompra, Long> {

    /** Obtener todos los detalles/productos de una compra específica */
    List<DetalleCompra> findByCompraIdCompra(Long idCompra);

    /** Obtener todas las compras donde aparece un producto */
    List<DetalleCompra> findByProductoIdProducto(Long idProducto);

    /** Eliminar todos los detalles de una compra */
    void deleteByCompraIdCompra(Long idCompra);
}