package com.Robbinson.ComRobinson.repositorio;

import com.Robbinson.ComRobinson.modelo.Compra;
import com.Robbinson.ComRobinson.modelo.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad Compra
 * Proporciona operaciones CRUD y consultas personalizadas
 */
@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {

    // Buscar compra por número de compra
    Optional<Compra> findByNumeroCompra(String numeroCompra);

    // Obtener compras por proveedor
    List<Compra> findByProveedor(Proveedor proveedor);

    // Obtener compras por ID de proveedor
    List<Compra> findByProveedorIdProveedor(Long proveedorId);

    // Obtener compras por estado
    List<Compra> findByEstado(Compra.EstadoCompra estado);

    // Obtener compras en un rango de fechas
    List<Compra> findByFechaCompraBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    // Contar compras por estado
    long countByEstado(Compra.EstadoCompra estado);

    // Verificar si existe una compra con el número dado
    boolean existsByNumeroCompra(String numeroCompra);

    // Obtener últimas compras
    @Query("SELECT c FROM Compra c ORDER BY c.fechaCompra DESC")
    List<Compra> findUltimasCompras();
}
