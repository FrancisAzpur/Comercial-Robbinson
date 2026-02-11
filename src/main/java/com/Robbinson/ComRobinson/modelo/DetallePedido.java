package com.Robbinson.ComRobinson.modelo;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

/**
 * =========================================================================
 * ENTIDAD DETALLE_PEDIDO - Tabla 'detalle_pedidos' (Tabla intermedia)
 * =========================================================================
 * PUNTO DE EVALUACIÓN: Consultas con múltiples tablas + DTO
 * 
 * Tabla intermedia que relaciona PEDIDOS con PRODUCTOS.
 * Cada fila representa UN producto dentro de un pedido con su cantidad y precio.
 * 
 * RELACIONES JPA (MULTI-TABLA):
 *   @ManyToOne Pedido   → Cada detalle pertenece a UN pedido
 *   @ManyToOne Producto → Cada detalle referencia a UN producto
 * 
 * CONSULTA EN CASCADA:
 *   Pedido →1→N DetallePedido N→1→ Producto
 *   Cuando se consulta un pedido, se obtienen sus detalles,
 *   y de cada detalle se accede al producto (nombre, precio, stock).
 * 
 * @PrePersist: Calcula el subtotal automáticamente antes de insertar
 *   (también existe un TRIGGER en MySQL como respaldo)
 * =========================================================================
 */

@Entity
@Table(name = "detalle_pedidos")
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Long idDetalle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido", nullable = false)
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    // Constructores
    public DetallePedido() {}

    public DetallePedido(Pedido pedido, Producto producto, Integer cantidad, BigDecimal precioUnitario) {
        this.pedido = pedido;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }

    // Getters y Setters
    public Long getIdDetalle() { return idDetalle; }
    public void setIdDetalle(Long idDetalle) { this.idDetalle = idDetalle; }

    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    // El subtotal se calcula automáticamente por el TRIGGER en SQL
    // Pero también lo calculamos aquí por seguridad
    @PrePersist
    public void calcularSubtotal() {
        if (this.cantidad != null && this.precioUnitario != null) {
            this.subtotal = this.precioUnitario.multiply(BigDecimal.valueOf(this.cantidad));
        }
    }
}