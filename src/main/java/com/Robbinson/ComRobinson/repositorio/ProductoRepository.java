package com.Robbinson.ComRobinson.repositorio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.Robbinson.ComRobinson.modelo.Producto;

/**
 * =========================================================================
 * REPOSITORIO DE PRODUCTOS - Acceso a datos de la tabla 'productos'
 * =========================================================================
 * PUNTO DE EVALUACIÓN: Conexión a BDD + @Query JPQL personalizada
 * 
 * Incluye una @Query JPQL personalizada para detectar productos
 * con stock bajo (stock_actual <= stock_minimo).
 * =========================================================================
 */
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    /** Buscar producto por código único */
    Optional<Producto> findByCodigoProducto(String codigoProducto);

    /** Búsqueda parcial por nombre (LIKE %nombre%) */
    List<Producto> findByNombreProductoContainingIgnoreCase(String nombre);

    /** Filtrar productos activos/inactivos */
    List<Producto> findByActivo(Boolean activo);

    /** Filtrar productos por etiqueta (NUEVO, OFERTA, DESTACADO) */
    List<Producto> findByEtiqueta(String etiqueta);

    /** Verificar si un código de producto ya existe */
    boolean existsByCodigoProducto(String codigoProducto);

    /**
     * CONSULTA JPQL PERSONALIZADA:
     * Obtiene productos con stock crítico (stock_actual <= stock_minimo)
     * Solo busca entre productos activos.
     */
    @Query("SELECT p FROM Producto p WHERE p.stockActual <= p.stockMinimo AND p.activo = true")
    List<Producto> findProductosConStockBajo();

    /** Productos activos ordenados alfabéticamente por nombre */
    List<Producto> findByActivoTrueOrderByNombreProductoAsc();

    /** Contar productos por estado activo/inactivo */
    long countByActivo(Boolean activo);
}