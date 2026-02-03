package com.Robbinson.ComRobinson.repositorio;

import com.Robbinson.ComRobinson.modelo.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad Producto
 * Proporciona operaciones CRUD y consultas personalizadas
 */
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Buscar producto por código
    Optional<Producto> findByCodigoProducto(String codigoProducto);

    // Buscar productos por nombre (búsqueda parcial)
    List<Producto> findByNombreProductoContainingIgnoreCase(String nombre);

    // Obtener productos activos
    List<Producto> findByActivoTrue();

    // Obtener productos por etiqueta (OFERTA, NUEVO, etc.)
    List<Producto> findByEtiquetaIgnoreCase(String etiqueta);

    // Obtener productos con stock bajo
    @Query("SELECT p FROM Producto p WHERE p.stockActual <= p.stockMinimo AND p.activo = true")
    List<Producto> findProductosConStockBajo();

    // Obtener productos en rango de precio
    List<Producto> findByPrecioVentaBetweenAndActivoTrue(BigDecimal precioMin, BigDecimal precioMax);

    // Obtener productos con stock disponible
    @Query("SELECT p FROM Producto p WHERE p.stockActual > 0 AND p.activo = true")
    List<Producto> findProductosDisponibles();

    // Verificar si existe un producto con el código dado
    boolean existsByCodigoProducto(String codigoProducto);

    // Contar productos activos
    long countByActivoTrue();

    // Obtener productos en oferta
    @Query("SELECT p FROM Producto p WHERE p.etiqueta IN ('OFERTA', 'DESCUENTO') AND p.activo = true")
    List<Producto> findProductosEnOferta();

    // Buscar productos por nombre o descripción
    @Query("SELECT p FROM Producto p WHERE (LOWER(p.nombreProducto) LIKE LOWER(CONCAT('%', :texto, '%')) OR LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :texto, '%'))) AND p.activo = true")
    List<Producto> buscarPorTexto(@Param("texto") String texto);
}
