package com.Robbinson.ComRobinson.servicios;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Robbinson.ComRobinson.modelo.DetallePedido;
import com.Robbinson.ComRobinson.modelo.Pedido;
import com.Robbinson.ComRobinson.modelo.Producto;
import com.Robbinson.ComRobinson.repositorio.DetallePedidoRepository;
import com.Robbinson.ComRobinson.repositorio.PedidoRepository;
import com.Robbinson.ComRobinson.repositorio.ProductoRepository;

@Service
@Transactional
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    /**
     * Genera un número de pedido único con formato PED-YYYY-NNN
     */
    public String generarNumeroPedido() {
        long count = pedidoRepository.count() + 1;
        return String.format("PED-%d-%03d", Year.now().getValue(), count);
    }

    /**
     * Crear pedido completo.
     * NOTA: Al insertar cada DetallePedido, el TRIGGER de MySQL
     * 'actualizar_stock_venta' se ejecuta automáticamente y resta el stock.
     * El TRIGGER 'calcular_subtotal_detalle_pedido' calcula el subtotal de cada línea.
     */
    public Pedido crearPedido(Pedido pedido) {
        // Generar número de pedido si no tiene
        if (pedido.getNumeroPedido() == null || pedido.getNumeroPedido().isEmpty()) {
            pedido.setNumeroPedido(generarNumeroPedido());
        }

        // Calcular totales
        BigDecimal subtotal = BigDecimal.ZERO;
        for (DetallePedido detalle : pedido.getDetalles()) {
            // Validar stock disponible
            Producto producto = productoRepository.findById(detalle.getProducto().getIdProducto())
                    .orElseThrow(() -> new RuntimeException(
                            "Producto no encontrado: " + detalle.getProducto().getIdProducto()));

            if (producto.getStockActual() < detalle.getCantidad()) {
                throw new RuntimeException(
                        "Stock insuficiente para: " + producto.getNombreProducto()
                                + ". Disponible: " + producto.getStockActual());
            }

            BigDecimal lineaSubtotal = detalle.getPrecioUnitario()
                    .multiply(BigDecimal.valueOf(detalle.getCantidad()));
            subtotal = subtotal.add(lineaSubtotal);
            detalle.setSubtotal(lineaSubtotal);
            detalle.setPedido(pedido);
        }

        pedido.setSubtotal(subtotal);

        // Calcular IGV 18%
        BigDecimal igv = subtotal.multiply(new BigDecimal("0.18"));
        pedido.setImpuesto(igv);

        // Total = subtotal + impuesto + envío
        BigDecimal total = subtotal.add(igv).add(
                pedido.getCostoEnvio() != null ? pedido.getCostoEnvio() : BigDecimal.ZERO);
        pedido.setTotal(total);

        // Guardar pedido (cascade guarda los detalles)
        // Al insertar cada detalle, MySQL ejecuta:
        // 1. TRIGGER calcular_subtotal_detalle_pedido (BEFORE INSERT)
        // 2. TRIGGER actualizar_stock_venta (AFTER INSERT) → resta stock
        return pedidoRepository.save(pedido);
    }
    
    @Transactional(readOnly = true)
    public List<Pedido> obtenerTodosLosPedidos(String estado) {
        return pedidoRepository.findAllByOrderByFechaPedidoDesc();
    }

    public Optional<Pedido> obtenerPedidoPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    public Optional<Pedido> buscarPorNumeroPedido(String numeroPedido) {
        return pedidoRepository.findByNumeroPedido(numeroPedido);
    }

    public List<Pedido> obtenerPedidosPorCliente(Long idCliente) {
        return pedidoRepository.findByClienteIdCliente(idCliente);
    }

    // ==================== GUARDAR / ACTUALIZAR ====================

    /**
     * Guardar pedido simple (usado por el formulario del controlador)
     * Genera número de pedido automáticamente si no tiene uno
     */
    public Pedido guardarPedido(Pedido pedido) {
        if (pedido.getNumeroPedido() == null || pedido.getNumeroPedido().isEmpty()) {
            pedido.setNumeroPedido(generarNumeroPedido());
        }
        return pedidoRepository.save(pedido);
    }

    /**
     * Actualizar datos de un pedido existente (observaciones, método de pago, envío)
     */
    public Pedido actualizarPedido(Long id, Pedido pedidoActualizado) {
        return pedidoRepository.findById(id)
                .map(pedido -> {
                    pedido.setObservaciones(pedidoActualizado.getObservaciones());
                    pedido.setMetodoPago(pedidoActualizado.getMetodoPago());
                    pedido.setCostoEnvio(pedidoActualizado.getCostoEnvio());
                    return pedidoRepository.save(pedido);
                })
                .orElse(null);
    }

    // ==================== OBTENER POR ESTADO ====================

    /**
     * Obtener pedidos por estado usando Enum directamente
     */
    public List<Pedido> obtenerPedidosPorEstado(Pedido.EstadoPedido estado) {
        return pedidoRepository.findByEstado(estado);
    }

    /**
     * Obtener pedidos por estado usando String (usado por el controlador)
     * Convierte el String a EstadoPedido antes de buscar
     */
    public List<Pedido> obtenerPedidosPorEstado(String estado) {
        Pedido.EstadoPedido estadoEnum = Pedido.EstadoPedido.valueOf(estado.toUpperCase());
        return pedidoRepository.findByEstado(estadoEnum);
    }

    // ==================== CAMBIAR ESTADO ====================

    /**
     * Cambiar estado de un pedido usando Enum
     */
    public Pedido actualizarEstado(Long id, Pedido.EstadoPedido nuevoEstado) {
        return pedidoRepository.findById(id)
                .map(pedido -> {
                    pedido.setEstado(nuevoEstado);
                    return pedidoRepository.save(pedido);
                })
                .orElse(null);
    }

    /**
     * Cambiar estado de un pedido usando String (usado por el controlador)
     * Convierte el String a EstadoPedido antes de actualizar
     */
    public Pedido cambiarEstadoPedido(Long id, String estado) {
        Pedido.EstadoPedido estadoEnum = Pedido.EstadoPedido.valueOf(estado.toUpperCase());
        return pedidoRepository.findById(id)
                .map(pedido -> {
                    pedido.setEstado(estadoEnum);
                    return pedidoRepository.save(pedido);
                })
                .orElse(null);
    }

    // ==================== ELIMINAR ====================

    public boolean eliminarPedido(Long id) {
        if (pedidoRepository.existsById(id)) {
            pedidoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // ==================== CONTEOS ====================

    public long contarPedidos() {
        return pedidoRepository.count();
    }

    public long contarPedidosPorEstado(Pedido.EstadoPedido estado) {
        return pedidoRepository.countByEstado(estado);
    }

    /**
     * Retorna array con conteo por estado: [PENDIENTE, PROCESANDO, ENVIADO, ENTREGADO]
     * Usado por el controlador para mostrar resumen en el listado de pedidos
     */
    public int[] contarPedidosPorEstado() {
        int[] conteos = new int[4];
        conteos[0] = (int) pedidoRepository.countByEstado(Pedido.EstadoPedido.PENDIENTE);
        conteos[1] = (int) pedidoRepository.countByEstado(Pedido.EstadoPedido.PROCESANDO);
        conteos[2] = (int) pedidoRepository.countByEstado(Pedido.EstadoPedido.ENVIADO);
        conteos[3] = (int) pedidoRepository.countByEstado(Pedido.EstadoPedido.ENTREGADO);
        return conteos;
    }

    // ==================== VENTAS ====================

    public BigDecimal obtenerTotalVentas(LocalDateTime inicio, LocalDateTime fin) {
        return pedidoRepository.totalVentasEntreFechas(inicio, fin);
    }

    // ==================== DETALLES ====================

    public List<DetallePedido> obtenerDetallesPorPedido(Long idPedido) {
        return detallePedidoRepository.findByPedidoIdPedido(idPedido);
    }
}