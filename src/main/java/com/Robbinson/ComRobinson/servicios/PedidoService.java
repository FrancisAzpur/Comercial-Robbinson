package com.Robbinson.ComRobinson.servicios;

import com.Robbinson.ComRobinson.modelo.Pedido;
import com.Robbinson.ComRobinson.modelo.Cliente;
import com.Robbinson.ComRobinson.repositorio.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Servicio para gestionar operaciones de Pedidos
 * Utiliza JPA Repository para interactuar con la base de datos
 */
@Service
@Transactional
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    /**
     * Guardar o actualizar un pedido
     */
    public Pedido guardarPedido(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    /**
     * Agregar pedido (alias para compatibilidad)
     */
    public Pedido agregarPedido(Pedido pedido) {
        return guardarPedido(pedido);
    }

    /**
     * Obtener todos los pedidos
     */
    @Transactional(readOnly = true)
    public List<Pedido> obtenerTodosLosPedidos() {
        return pedidoRepository.findAll();
    }

    /**
     * Obtener pedido por ID
     */
    @Transactional(readOnly = true)
    public Optional<Pedido> obtenerPedidoPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    /**
     * Buscar pedido por número de pedido
     */
    @Transactional(readOnly = true)
    public Optional<Pedido> buscarPorNumeroPedido(String numeroPedido) {
        return pedidoRepository.findByNumeroPedido(numeroPedido);
    }

    /**
     * Alias para compatibilidad
     */
    @Transactional(readOnly = true)
    public Optional<Pedido> buscarPorNumeroOrden(String numeroOrden) {
        return buscarPorNumeroPedido(numeroOrden);
    }

    /**
     * Obtener pedidos por cliente
     */
    @Transactional(readOnly = true)
    public List<Pedido> obtenerPedidosPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteIdCliente(clienteId);
    }

    /**
     * Obtener pedidos por estado
     */
    @Transactional(readOnly = true)
    public List<Pedido> obtenerPedidosPorEstado(String estado) {
        return pedidoRepository.findByEstado(Pedido.EstadoPedido.valueOf(estado.toUpperCase()));
    }

    /**
     * Obtener pedidos por estado (usando enum)
     */
    @Transactional(readOnly = true)
    public List<Pedido> obtenerPedidosPorEstado(Pedido.EstadoPedido estado) {
        return pedidoRepository.findByEstado(estado);
    }

    /**
     * Obtener pedidos en rango de fechas
     */
    @Transactional(readOnly = true)
    public List<Pedido> obtenerPedidosPorFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return pedidoRepository.findByFechaPedidoBetween(fechaInicio, fechaFin);
    }

    /**
     * Actualizar estado de un pedido
     */
    public boolean cambiarEstadoPedido(Long id, String nuevoEstado) {
        Optional<Pedido> pedidoOpt = pedidoRepository.findById(id);
        if (pedidoOpt.isPresent()) {
            Pedido pedido = pedidoOpt.get();
            pedido.setEstado(Pedido.EstadoPedido.valueOf(nuevoEstado.toUpperCase()));
            pedidoRepository.save(pedido);
            return true;
        }
        return false;
    }

    /**
     * Actualizar pedido
     */
    public boolean actualizarPedido(Long id, Pedido pedidoActualizado) {
        Optional<Pedido> pedidoOpt = pedidoRepository.findById(id);
        if (pedidoOpt.isPresent()) {
            Pedido pedido = pedidoOpt.get();
            pedido.setEstado(pedidoActualizado.getEstado());
            pedido.setMetodoPago(pedidoActualizado.getMetodoPago());
            pedido.setTotal(pedidoActualizado.getTotal());
            pedido.setObservaciones(pedidoActualizado.getObservaciones());
            pedidoRepository.save(pedido);
            return true;
        }
        return false;
    }

    /**
     * Eliminar un pedido por ID
     */
    public boolean eliminarPedido(Long id) {
        if (pedidoRepository.existsById(id)) {
            pedidoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Contar pedidos
     */
    @Transactional(readOnly = true)
    public long contarPedidos() {
        return pedidoRepository.count();
    }

    /**
     * Contar pedidos por estado
     * @return Array con conteos [Pendiente, Procesando, Enviado, Entregado]
     */
    @Transactional(readOnly = true)
    public int[] contarPedidosPorEstado() {
        int pendiente = (int) pedidoRepository.countByEstado(Pedido.EstadoPedido.PENDIENTE);
        int procesando = (int) pedidoRepository.countByEstado(Pedido.EstadoPedido.PROCESANDO);
        int enviado = (int) pedidoRepository.countByEstado(Pedido.EstadoPedido.ENVIADO);
        int entregado = (int) pedidoRepository.countByEstado(Pedido.EstadoPedido.ENTREGADO);
        return new int[]{pendiente, procesando, enviado, entregado};
    }

    /**
     * Obtener estadísticas de pedidos
     */
    @Transactional(readOnly = true)
    public Map<String, Long> obtenerEstadisticasPorEstado() {
        Map<String, Long> stats = new HashMap<>();
        for (Pedido.EstadoPedido estado : Pedido.EstadoPedido.values()) {
            stats.put(estado.name(), pedidoRepository.countByEstado(estado));
        }
        return stats;
    }
}
