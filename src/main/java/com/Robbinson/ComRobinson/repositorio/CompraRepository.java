package com.Robbinson.ComRobinson.repositorio;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Robbinson.ComRobinson.modelo.Compra;

/**
 * =========================================================================
 * REPOSITORIO DE COMPRAS - Acceso a datos de la tabla 'compras'
 * =========================================================================
 * PUNTO DE EVALUACIÓN: Consultas multi-tabla (Compra → Proveedor)
 * 
 * Permite buscar compras por proveedor, estado y rango de fechas.
 * =========================================================================
 */
@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {

    /** Buscar compra por número único */
    Optional<Compra> findByNumeroCompra(String numeroCompra);

    /** Obtener compras de un proveedor específico (MULTI-TABLA: Compra → Proveedor) */
    List<Compra> findByProveedorIdProveedor(Long idProveedor);

    /** Filtrar compras por estado */
    List<Compra> findByEstado(Compra.EstadoCompra estado);

    /** Buscar compras en un rango de fechas */
    List<Compra> findByFechaCompraBetween(LocalDateTime inicio, LocalDateTime fin);

    /** Verificar si un número de compra ya existe */
    boolean existsByNumeroCompra(String numeroCompra);

    /** Todas las compras ordenadas por fecha descendente */
    List<Compra> findAllByOrderByFechaCompraDesc();
}