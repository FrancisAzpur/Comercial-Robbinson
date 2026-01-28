package com.Robbinson.ComRobinson.modelo;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Modelo de Venta para registrar todas las transacciones de ventas
 */
public class Venta {
    
    private Long id;
    private LocalDate fechaVenta;
    private Long productoId;
    private String nombreProducto;
    private int cantidadVendida;
    private BigDecimal precioUnitario;
    private BigDecimal montoTotal;
    private Long clienteId;
    private String vendedor;

    // Constructor vacío
    public Venta() {
        this.fechaVenta = LocalDate.now();
    }

    // Constructor con parámetros
    public Venta(Long id, LocalDate fechaVenta, Long productoId, String nombreProducto, 
                 int cantidadVendida, BigDecimal precioUnitario, Long clienteId, String vendedor) {
        this.id = id;
        this.fechaVenta = fechaVenta;
        this.productoId = productoId;
        this.nombreProducto = nombreProducto;
        this.cantidadVendida = cantidadVendida;
        this.precioUnitario = precioUnitario;
        this.clienteId = clienteId;
        this.vendedor = vendedor;
        this.montoTotal = precioUnitario.multiply(new BigDecimal(cantidadVendida));
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(LocalDate fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public int getCantidadVendida() {
        return cantidadVendida;
    }

    public void setCantidadVendida(int cantidadVendida) {
        this.cantidadVendida = cantidadVendida;
        // Recalcular monto total cuando cambia cantidad
        if (this.precioUnitario != null) {
            this.montoTotal = precioUnitario.multiply(new BigDecimal(cantidadVendida));
        }
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
        // Recalcular monto total cuando cambia precio
        if (this.cantidadVendida > 0) {
            this.montoTotal = precioUnitario.multiply(new BigDecimal(cantidadVendida));
        }
    }

    public BigDecimal getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(BigDecimal montoTotal) {
        this.montoTotal = montoTotal;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }
}
