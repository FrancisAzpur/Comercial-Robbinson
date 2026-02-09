package com.Robbinson.ComRobinson.repositorio;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Robbinson.ComRobinson.modelo.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    Optional<Pedido> findByNumeroPedido(String numeroPedido);

    List<Pedido> findByClienteIdCliente(Long idCliente);

    List<Pedido> findByEstado(Pedido.EstadoPedido estado);

    List<Pedido> findByFechaPedidoBetween(LocalDateTime inicio, LocalDateTime fin);

    boolean existsByNumeroPedido(String numeroPedido);

    // Pedidos ordenados por fecha descendente
    List<Pedido> findAllByOrderByFechaPedidoDesc();

    // Contar pedidos por estado
    long countByEstado(Pedido.EstadoPedido estado);

    // Total de ventas en un rango de fechas
    @Query("SELECT COALESCE(SUM(p.total), 0) FROM Pedido p WHERE p.estado IN ('PAGADO','ENVIADO','ENTREGADO') AND p.fechaPedido BETWEEN :inicio AND :fin")
    java.math.BigDecimal totalVentasEntreFechas(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);
}