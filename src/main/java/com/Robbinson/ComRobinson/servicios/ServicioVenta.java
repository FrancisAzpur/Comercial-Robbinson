package com.Robbinson.ComRobinson.servicios;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.Robbinson.ComRobinson.modelo.Venta;

/**
 * Servicio para gestionar operaciones CRUD de Ventas
 * Maneja adiciones, listados, consultas, eliminaciones y búsquedas de ventas
 */
@Service
public class ServicioVenta {

    // Simulamos una base de datos en memoria
    private List<Venta> ventas = new ArrayList<>();
    private Long contadorId = 1L;

    /**
     * Registrar una nueva venta en el sistema
     * @param venta - La venta a registrar
     * @return - La venta registrada con ID
     */
    public Venta agregarVenta(Venta venta) {
        if (venta.getId() == null) {
            venta.setId(contadorId++);
        }
        ventas.add(venta);
        return venta;
    }

    /**
     * Obtener todas las ventas registradas
     * @return - Lista de todas las ventas
     */
    public List<Venta> obtenerTodasLasVentas() {
        return new ArrayList<>(ventas);
    }

    /**
     * Buscar una venta por su ID
     * @param id - ID de la venta
     * @return - Optional con la venta si existe
     */
    public Optional<Venta> obtenerVentaPorId(Long id) {
        return ventas.stream()
                .filter(v -> v.getId().equals(id))
                .findFirst();
    }

    /**
     * Obtener todas las ventas de un producto específico
     * @param productoId - ID del producto
     * @return - Lista de ventas de ese producto
     */
    public List<Venta> obtenerVentasPorProducto(Long productoId) {
        return ventas.stream()
                .filter(v -> v.getProductoId().equals(productoId))
                .collect(Collectors.toList());
    }

    /**
     * Obtener todas las ventas de un cliente específico
     * @param clienteId - ID del cliente
     * @return - Lista de ventas del cliente
     */
    public List<Venta> obtenerVentasPorCliente(Long clienteId) {
        return ventas.stream()
                .filter(v -> v.getClienteId().equals(clienteId))
                .collect(Collectors.toList());
    }

    /**
     * Obtener ventas en un rango de fechas
     * @param fechaInicio - Fecha inicial
     * @param fechaFin - Fecha final
     * @return - Lista de ventas en ese rango
     */
    public List<Venta> obtenerVentasPorFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return ventas.stream()
                .filter(v -> !v.getFechaVenta().isBefore(fechaInicio) && 
                           !v.getFechaVenta().isAfter(fechaFin))
                .collect(Collectors.toList());
    }

    /**
     * Buscar ventas por nombre de producto (búsqueda parcial)
     * @param nombre - Parte del nombre del producto
     * @return - Lista de ventas de productos que coinciden
     */
    public List<Venta> buscarPorNombreProducto(String nombre) {
        return ventas.stream()
                .filter(v -> v.getNombreProducto().toLowerCase().contains(nombre.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Actualizar datos de una venta existente
     * @param id - ID de la venta a actualizar
     * @param ventaActualizada - Los nuevos datos de la venta
     * @return - True si se actualizó, False si no encontró la venta
     */
    public boolean actualizarVenta(Long id, Venta ventaActualizada) {
        Optional<Venta> venta = obtenerVentaPorId(id);
        if (venta.isPresent()) {
            Venta v = venta.get();
            v.setFechaVenta(ventaActualizada.getFechaVenta());
            v.setNombreProducto(ventaActualizada.getNombreProducto());
            v.setCantidadVendida(ventaActualizada.getCantidadVendida());
            v.setPrecioUnitario(ventaActualizada.getPrecioUnitario());
            v.setVendedor(ventaActualizada.getVendedor());
            return true;
        }
        return false;
    }

    /**
     * Eliminar una venta por su ID
     * @param id - ID de la venta a eliminar
     * @return - True si se eliminó, False si no encontró la venta
     */
    public boolean eliminarVenta(Long id) {
        return ventas.removeIf(v -> v.getId().equals(id));
    }

    /**
     * Obtener el total de dinero en soles generado por todas las ventas
     * @return - Monto total en soles
     */
    public BigDecimal calcularTotalVentas() {
        return ventas.stream()
                .map(Venta::getMontoTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Obtener el total de dinero generado en un rango de fechas
     * @param fechaInicio - Fecha inicial
     * @param fechaFin - Fecha final
     * @return - Monto total en el rango
     */
    public BigDecimal calcularTotalVentasPorFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return obtenerVentasPorFechas(fechaInicio, fechaFin).stream()
                .map(Venta::getMontoTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Obtener cantidad total de ventas registradas
     * @return - Número de ventas
     */
    public int contarVentas() {
        return ventas.size();
    }

    /**
     * Obtener la cantidad total de unidades vendidas
     * @return - Suma de todas las cantidades vendidas
     */
    public int obtenerTotalUnidadesVendidas() {
        return ventas.stream()
                .mapToInt(Venta::getCantidadVendida)
                .sum();
    }

    /**
     * Obtener ventas registradas en una fecha específica
     * @param fecha - Fecha a buscar
     * @return - Lista de ventas de ese día
     */
    public List<Venta> obtenerVentasPorFecha(LocalDate fecha) {
        return ventas.stream()
                .filter(v -> v.getFechaVenta().equals(fecha))
                .collect(Collectors.toList());
    }

    /**
     * Obtener información estadística de vendedores
     * @param vendedor - Nombre del vendedor
     * @return - Lista de ventas de ese vendedor
     */
    public List<Venta> obtenerVentasPorVendedor(String vendedor) {
        return ventas.stream()
                .filter(v -> v.getVendedor().equalsIgnoreCase(vendedor))
                .collect(Collectors.toList());
    }
}
