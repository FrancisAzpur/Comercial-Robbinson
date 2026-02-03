package com.Robbinson.ComRobinson.repositorio;

import com.Robbinson.ComRobinson.modelo.Pedido;
import com.Robbinson.ComRobinson.modelo.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad Pedido
 * Proporciona operaciones CRUD y consultas personalizadas
 */
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    // Buscar pedido por número de pedido
    Optional<Pedido> findByNumeroPedido(String numeroPedido);

    // Obtener pedidos por cliente
    List<Pedido> findByCliente(Cliente cliente);

    // Obtener pedidos por ID de cliente
    List<Pedido> findByClienteIdCliente(Long clienteId);

    // Obtener pedidos por estado
    List<Pedido> findByEstado(Pedido.EstadoPedido estado);

    // Obtener pedidos por método de pago
    List<Pedido> findByMetodoPago(Pedido.MetodoPago metodoPago);

    // Obtener pedidos en un rango de fechas
    List<Pedido> findByFechaPedidoBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    // Contar pedidos por estado
    long countByEstado(Pedido.EstadoPedido estado);

    // Obtener pedidos pendientes
    @Query("SELECT p FROM Pedido p WHERE p.estado = 'PENDIENTE' ORDER BY p.fechaPedido DESC")
    List<Pedido> findPedidosPendientes();

    // Obtener estadísticas de pedidos por estado
    @Query("SELECT p.estado, COUNT(p) FROM Pedido p GROUP BY p.estado")
    List<Object[]> contarPedidosPorEstado();

    // Obtener últimos pedidos
    @Query("SELECT p FROM Pedido p ORDER BY p.fechaPedido DESC")
    List<Pedido> findUltimosPedidos();

    // Verificar si existe un pedido con el número dado
    boolean existsByNumeroPedido(String numeroPedido);

    // Buscar pedidos por cliente y estado
    List<Pedido> findByClienteAndEstado(Cliente cliente, Pedido.EstadoPedido estado);
}
