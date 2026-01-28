package com.Robbinson.ComRobinson.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.Robbinson.ComRobinson.modelo.Pedido;

/**
 * Servicio para gestionar operaciones CRUD de Pedidos
 * Maneja adiciones, listados, consultas, eliminaciones y búsquedas de pedidos
 */
@Service
public class ServicioPedido {

    // Simulamos una base de datos en memoria
    private List<Pedido> pedidos = new ArrayList<>();
    private Long contadorId = 1L;

    /**
     * Agregar un nuevo pedido al sistema
     * @param pedido - El pedido a agregar
     * @return - El pedido agregado con ID
     */
    public Pedido agregarPedido(Pedido pedido) {
        if (pedido.getId() == null) {
            pedido.setId(contadorId++);
        }
        pedidos.add(pedido);
        return pedido;
    }

    /**
     * Obtener todos los pedidos registrados
     * @return - Lista de todos los pedidos
     */
    public List<Pedido> obtenerTodosLosPedidos() {
        return new ArrayList<>(pedidos);
    }

    /**
     * Buscar un pedido por su ID
     * @param id - ID del pedido
     * @return - Optional con el pedido si existe
     */
    public Optional<Pedido> obtenerPedidoPorId(Long id) {
        return pedidos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    /**
     * Buscar pedidos por número de orden
     * @param numeroOrden - Número de la orden
     * @return - Optional con el pedido si existe
     */
    public Optional<Pedido> buscarPorNumeroOrden(String numeroOrden) {
        return pedidos.stream()
                .filter(p -> p.getNumeroOrden().equals(numeroOrden))
                .findFirst();
    }

    /**
     * Obtener todos los pedidos de un cliente específico
     * @param clienteId - ID del cliente
     * @return - Lista de pedidos del cliente
     */
    public List<Pedido> obtenerPedidosPorCliente(Long clienteId) {
        return pedidos.stream()
                .filter(p -> p.getClienteId().equals(clienteId))
                .collect(Collectors.toList());
    }

    /**
     * Obtener pedidos por estado (Pendiente, Procesando, Enviado, Entregado)
     * @param estado - Estado del pedido a buscar
     * @return - Lista de pedidos con ese estado
     */
    public List<Pedido> obtenerPedidosPorEstado(String estado) {
        return pedidos.stream()
                .filter(p -> p.getEstado().equalsIgnoreCase(estado))
                .collect(Collectors.toList());
    }

    /**
     * Actualizar los datos de un pedido existente
     * @param id - ID del pedido a actualizar
     * @param pedidoActualizado - Los nuevos datos del pedido
     * @return - True si se actualizó, False si no encontró el pedido
     */
    public boolean actualizarPedido(Long id, Pedido pedidoActualizado) {
        Optional<Pedido> pedido = obtenerPedidoPorId(id);
        if (pedido.isPresent()) {
            Pedido p = pedido.get();
            p.setEstado(pedidoActualizado.getEstado());
            p.setMetodoPago(pedidoActualizado.getMetodoPago());
            p.setTotalMoneda(pedidoActualizado.getTotalMoneda());
            p.setDescripcionProductos(pedidoActualizado.getDescripcionProductos());
            return true;
        }
        return false;
    }

    /**
     * Cambiar el estado de un pedido
     * @param id - ID del pedido
     * @param nuevoEstado - Nuevo estado (Pendiente, Procesando, Enviado, Entregado)
     * @return - True si se cambió, False si no encontró el pedido
     */
    public boolean cambiarEstadoPedido(Long id, String nuevoEstado) {
        Optional<Pedido> pedido = obtenerPedidoPorId(id);
        if (pedido.isPresent()) {
            pedido.get().setEstado(nuevoEstado);
            return true;
        }
        return false;
    }

    /**
     * Eliminar un pedido por su ID
     * @param id - ID del pedido a eliminar
     * @return - True si se eliminó, False si no encontró el pedido
     */
    public boolean eliminarPedido(Long id) {
        return pedidos.removeIf(p -> p.getId().equals(id));
    }

    /**
     * Obtener la cantidad total de pedidos
     * @return - Número de pedidos registrados
     */
    public int contarPedidos() {
        return pedidos.size();
    }

    /**
     * Obtener estadísticas: contar pedidos por estado
     * @return - Array con los conteos [Pendiente, Procesando, Enviado, Entregado]
     */
    public int[] contarPedidosPorEstado() {
        int pendiente = (int) pedidos.stream().filter(p -> p.getEstado().equals("Pendiente")).count();
        int procesando = (int) pedidos.stream().filter(p -> p.getEstado().equals("Procesando")).count();
        int enviado = (int) pedidos.stream().filter(p -> p.getEstado().equals("Enviado")).count();
        int entregado = (int) pedidos.stream().filter(p -> p.getEstado().equals("Entregado")).count();
        return new int[]{pendiente, procesando, enviado, entregado};
    }
}
