package com.Robbinson.ComRobinson.modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Modelo de Pedido para registrar compras de clientes
 */
public class Pedido {
    
    private Long id;
    private Long clienteId;
    private String numeroOrden;
    private LocalDateTime fechaPedido;
    private BigDecimal totalMoneda;
    private String estado; // Pendiente, Procesando, Enviado, Entregado
    private String metodoPago; // Efectivo, Tarjeta, Transferencia
    private String descripcionProductos;

    // Constructor vacío
    public Pedido() {
        this.estado = "Pendiente";
        this.fechaPedido = LocalDateTime.now();
    }

    // Constructor con parámetros
    public Pedido(Long id, Long clienteId, String numeroOrden, BigDecimal totalMoneda, 
                  String estado, String metodoPago, String descripcionProductos) {
        this.id = id;
        this.clienteId = clienteId;
        this.numeroOrden = numeroOrden;
        this.totalMoneda = totalMoneda;
        this.estado = estado;
        this.metodoPago = metodoPago;
        this.descripcionProductos = descripcionProductos;
        this.fechaPedido = LocalDateTime.now();
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public String getNumeroOrden() {
        return numeroOrden;
    }

    public void setNumeroOrden(String numeroOrden) {
        this.numeroOrden = numeroOrden;
    }

    public LocalDateTime getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDateTime fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public BigDecimal getTotalMoneda() {
        return totalMoneda;
    }

    public void setTotalMoneda(BigDecimal totalMoneda) {
        this.totalMoneda = totalMoneda;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getDescripcionProductos() {
        return descripcionProductos;
    }

    public void setDescripcionProductos(String descripcionProductos) {
        this.descripcionProductos = descripcionProductos;
    }
}
