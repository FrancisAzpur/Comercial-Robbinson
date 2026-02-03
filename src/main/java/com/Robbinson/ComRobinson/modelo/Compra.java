package com.Robbinson.ComRobinson.modelo;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad Compra - Representa las órdenes de compra a proveedores
 * Mapeada a la tabla 'compras' en la base de datos
 */
@Entity
@Table(name = "compras")
public class Compra {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_compra")
    private Long idCompra;
    
    @Column(name = "numero_compra", nullable = false, unique = true, length = 20)
    private String numeroCompra;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_proveedor", nullable = false)
    private Proveedor proveedor;
    
    @Column(name = "fecha_compra", updatable = false)
    private LocalDateTime fechaCompra;
    
    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    private BigDecimal total;
    
    @Column(name = "estado", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private EstadoCompra estado = EstadoCompra.PENDIENTE;
    
    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;
    
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    // Enum para estado de compra
    public enum EstadoCompra {
        PENDIENTE, RECIBIDA, PAGADA, CANCELADA
    }

    // Constructor vacío requerido por JPA
    public Compra() {
        this.fechaCompra = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }

    // Métodos de ciclo de vida JPA
    @PrePersist
    protected void onCreate() {
        fechaCompra = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
        if (numeroCompra == null) {
            numeroCompra = generarNumeroCompra();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }

    // Método para generar número de compra automático
    private String generarNumeroCompra() {
        return "COM-" + java.time.Year.now().getValue() + "-" + 
               String.format("%06d", System.currentTimeMillis() % 1000000);
    }

    // Getters y Setters
    public Long getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(Long idCompra) {
        this.idCompra = idCompra;
    }

    public Long getId() {
        return idCompra;
    }

    public void setId(Long id) {
        this.idCompra = id;
    }

    public String getNumeroCompra() {
        return numeroCompra;
    }

    public void setNumeroCompra(String numeroCompra) {
        this.numeroCompra = numeroCompra;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public LocalDateTime getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDateTime fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public EstadoCompra getEstado() {
        return estado;
    }

    public void setEstado(EstadoCompra estado) {
        this.estado = estado;
    }

    public String getEstadoString() {
        return estado != null ? estado.name() : null;
    }

    public void setEstadoString(String estado) {
        this.estado = EstadoCompra.valueOf(estado);
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    @Override
    public String toString() {
        return "Compra{" +
                "idCompra=" + idCompra +
                ", numeroCompra='" + numeroCompra + '\'' +
                ", estado=" + estado +
                ", total=" + total +
                '}';
    }
}
