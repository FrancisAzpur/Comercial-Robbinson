package com.Robbinson.ComRobinson.modelo;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad Pedido - Representa las órdenes de compra de los clientes
 * Mapeada a la tabla 'pedidos' en la base de datos
 */
@Entity
@Table(name = "pedidos")
public class Pedido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido")
    private Long idPedido;
    
    @Column(name = "numero_pedido", nullable = false, unique = true, length = 20)
    private String numeroPedido;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;
    
    @Column(name = "fecha_pedido", updatable = false)
    private LocalDateTime fechaPedido;
    
    @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;
    
    @Column(name = "impuesto", nullable = false, precision = 10, scale = 2)
    private BigDecimal impuesto = BigDecimal.ZERO;
    
    @Column(name = "costo_envio", nullable = false, precision = 10, scale = 2)
    private BigDecimal costoEnvio = BigDecimal.ZERO;
    
    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    private BigDecimal total;
    
    @Column(name = "metodo_pago", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private MetodoPago metodoPago = MetodoPago.EFECTIVO;
    
    @Column(name = "estado", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private EstadoPedido estado = EstadoPedido.PENDIENTE;
    
    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;
    
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    // Enums
    public enum MetodoPago {
        EFECTIVO, TARJETA_CREDITO, TARJETA_DEBITO, TRANSFERENCIA, YAPE, PLIN
    }

    public enum EstadoPedido {
        PENDIENTE, PAGADO, PROCESANDO, ENVIADO, ENTREGADO, CANCELADO
    }

    // Constructor vacío requerido por JPA
    public Pedido() {
        this.fechaPedido = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }

    // Métodos de ciclo de vida JPA
    @PrePersist
    protected void onCreate() {
        fechaPedido = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
        if (numeroPedido == null) {
            numeroPedido = generarNumeroPedido();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }

    // Método para generar número de pedido automático
    private String generarNumeroPedido() {
        return "PED-" + java.time.Year.now().getValue() + "-" + 
               String.format("%06d", System.currentTimeMillis() % 1000000);
    }

    // Método para calcular el total
    public void calcularTotal() {
        if (subtotal != null) {
            BigDecimal impuestoCalculado = subtotal.multiply(new BigDecimal("0.18"));
            this.impuesto = impuestoCalculado;
            this.total = subtotal.add(impuesto).add(costoEnvio != null ? costoEnvio : BigDecimal.ZERO);
        }
    }

    // Getters y Setters
    public Long getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Long idPedido) {
        this.idPedido = idPedido;
    }

    // Alias para compatibilidad
    public Long getId() {
        return idPedido;
    }

    public void setId(Long id) {
        this.idPedido = id;
    }

    public String getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(String numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    // Alias para compatibilidad
    public String getNumeroOrden() {
        return numeroPedido;
    }

    public void setNumeroOrden(String numeroOrden) {
        this.numeroPedido = numeroOrden;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    // Alias para compatibilidad
    public Long getClienteId() {
        return cliente != null ? cliente.getIdCliente() : null;
    }

    public LocalDateTime getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDateTime fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(BigDecimal impuesto) {
        this.impuesto = impuesto;
    }

    public BigDecimal getCostoEnvio() {
        return costoEnvio;
    }

    public void setCostoEnvio(BigDecimal costoEnvio) {
        this.costoEnvio = costoEnvio;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    // Alias para compatibilidad
    public BigDecimal getTotalMoneda() {
        return total;
    }

    public void setTotalMoneda(BigDecimal totalMoneda) {
        this.total = totalMoneda;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    // Alias para compatibilidad con String
    public String getMetodoPagoString() {
        return metodoPago != null ? metodoPago.name() : null;
    }

    public void setMetodoPagoString(String metodoPago) {
        this.metodoPago = MetodoPago.valueOf(metodoPago);
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    // Alias para compatibilidad con String
    public String getEstadoString() {
        return estado != null ? estado.name() : null;
    }

    public void setEstadoString(String estado) {
        this.estado = EstadoPedido.valueOf(estado);
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    // Alias para compatibilidad
    public String getDescripcionProductos() {
        return observaciones;
    }

    public void setDescripcionProductos(String descripcionProductos) {
        this.observaciones = descripcionProductos;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "idPedido=" + idPedido +
                ", numeroPedido='" + numeroPedido + '\'' +
                ", estado=" + estado +
                ", total=" + total +
                '}';
    }
}
