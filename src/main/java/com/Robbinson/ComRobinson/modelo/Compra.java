package com.Robbinson.ComRobinson.modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoCompra estado = EstadoCompra.PENDIENTE;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleCompra> detalles = new ArrayList<>();

    public enum EstadoCompra {
        PENDIENTE, RECIBIDA, PAGADA, CANCELADA
    }

    // Constructores
    public Compra() {
        this.fechaCompra = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }

    public Compra(String numeroCompra, Proveedor proveedor) {
        this();
        this.numeroCompra = numeroCompra;
        this.proveedor = proveedor;
    }

    // Getters y Setters
    public Long getIdCompra() { return idCompra; }
    public void setIdCompra(Long idCompra) { this.idCompra = idCompra; }

    public String getNumeroCompra() { return numeroCompra; }
    public void setNumeroCompra(String numeroCompra) { this.numeroCompra = numeroCompra; }

    public Proveedor getProveedor() { return proveedor; }
    public void setProveedor(Proveedor proveedor) { this.proveedor = proveedor; }

    public LocalDateTime getFechaCompra() { return fechaCompra; }
    public void setFechaCompra(LocalDateTime fechaCompra) { this.fechaCompra = fechaCompra; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public EstadoCompra getEstado() { return estado; }
    public void setEstado(EstadoCompra estado) { this.estado = estado; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }

    public List<DetalleCompra> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleCompra> detalles) { this.detalles = detalles; }

    public void agregarDetalle(DetalleCompra detalle) {
        detalles.add(detalle);
        detalle.setCompra(this);
    }

    @PreUpdate
    public void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }
}