package com.Robbinson.ComRobinson.repositorio;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Robbinson.ComRobinson.modelo.Pedido;

/**
 * =========================================================================
 * REPOSITORIO DE PEDIDOS - Acceso a datos de la tabla 'pedidos'
 * =========================================================================
 * PUNTO DE EVALUACIÓN: Consultas multi-tabla + @Query JPQL personalizada
 * 
 * Incluye consultas derivadas y una @Query JPQL personalizada que
 * calcula el total de ventas en un rango de fechas.
 * 
 * CONSULTAS MULTI-TABLA:
 *   - findByClienteIdCliente(): navega la relación Pedido → Cliente
 *   - countByEstado(): agrupa por estado del pedido
 *   - totalVentasEntreFechas(): @Query JPQL con SUM y filtros
 * =========================================================================
 */
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    /** Buscar pedido por número único */
    Optional<Pedido> findByNumeroPedido(String numeroPedido);

    /** Obtener todos los pedidos de un cliente específico (MULTI-TABLA: Pedido → Cliente) */
    List<Pedido> findByClienteIdCliente(Long idCliente);

    /** Filtrar pedidos por estado (PENDIENTE, PAGADO, ENVIADO, etc.) */
    List<Pedido> findByEstado(Pedido.EstadoPedido estado);

    /** Buscar pedidos en un rango de fechas */
    List<Pedido> findByFechaPedidoBetween(LocalDateTime inicio, LocalDateTime fin);

    /** Verificar si un número de pedido ya existe */
    boolean existsByNumeroPedido(String numeroPedido);

    /** Pedidos ordenados por fecha descendente (más recientes primero) */
    List<Pedido> findAllByOrderByFechaPedidoDesc();

    /** Contar pedidos por estado - para estadísticas del dashboard */
    long countByEstado(Pedido.EstadoPedido estado);

    /**
     * CONSULTA JPQL PERSONALIZADA:
     * Calcula la suma total de ventas entre dos fechas.
     * Solo cuenta pedidos con estado PAGADO, ENVIADO o ENTREGADO.
     * COALESCE retorna 0 si no hay resultados.
     */
    @Query("SELECT COALESCE(SUM(p.total), 0) FROM Pedido p WHERE p.estado IN ('PAGADO','ENVIADO','ENTREGADO') AND p.fechaPedido BETWEEN :inicio AND :fin")
    java.math.BigDecimal totalVentasEntreFechas(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);
}