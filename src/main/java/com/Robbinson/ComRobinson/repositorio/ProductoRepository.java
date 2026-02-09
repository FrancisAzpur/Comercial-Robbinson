package com.Robbinson.ComRobinson.repositorio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.Robbinson.ComRobinson.modelo.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    Optional<Producto> findByCodigoProducto(String codigoProducto);

    List<Producto> findByNombreProductoContainingIgnoreCase(String nombre);

    List<Producto> findByActivo(Boolean activo);

    List<Producto> findByEtiqueta(String etiqueta);

    boolean existsByCodigoProducto(String codigoProducto);

    // Productos con stock bajo (stock actual <= stock mínimo)
    @Query("SELECT p FROM Producto p WHERE p.stockActual <= p.stockMinimo AND p.activo = true")
    List<Producto> findProductosConStockBajo();

    // Productos activos ordenados por nombre
    List<Producto> findByActivoTrueOrderByNombreProductoAsc();

    // Contar productos activos
    long countByActivo(Boolean activo);
}