package com.Robbinson.ComRobinson.modelo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

/**
 * Entidad Producto - Representa los productos del catálogo
 * Mapeada a la tabla 'productos' en la base de datos
 */

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long idProducto;

    @Column(name = "codigo_producto", nullable = false, unique = true, length = 50)
    private String codigoProducto;

    @Column(name = "nombre_producto", nullable = false, length = 150)
    private String nombreProducto;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "precio_venta", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioVenta;

    @Column(name = "precio_compra", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioCompra;

    @Column(name = "stock_actual", nullable = false)
    private Integer stockActual = 0;

    @Column(name = "stock_minimo", nullable = false)
    private Integer stockMinimo = 5;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @Column(name = "imagen_principal", length = 255)
    private String imagenPrincipal;

    @Column(name = "etiqueta", length = 50)
    private String etiqueta;

    @Column(name = "fecha_registro", updatable = false)
    private LocalDateTime fechaRegistro;

    @Column(name = "ultima_actualizacion")
    private LocalDateTime ultimaActualizacion;

    // Constructores
    public Producto() {
        this.fechaRegistro = LocalDateTime.now();
        this.ultimaActualizacion = LocalDateTime.now();
    }

    public Producto(String codigoProducto, String nombreProducto, BigDecimal precioVenta, BigDecimal precioCompra) {
        this();
        this.codigoProducto = codigoProducto;
        this.nombreProducto = nombreProducto;
        this.precioVenta = precioVenta;
        this.precioCompra = precioCompra;
    }

    // Getters y Setters
    public Long getIdProducto() { return idProducto; }
    public void setIdProducto(Long idProducto) { this.idProducto = idProducto; }

    public String getCodigoProducto() { return codigoProducto; }
    public void setCodigoProducto(String codigoProducto) { this.codigoProducto = codigoProducto; }

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public BigDecimal getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(BigDecimal precioVenta) { this.precioVenta = precioVenta; }

    public BigDecimal getPrecioCompra() { return precioCompra; }
    public void setPrecioCompra(BigDecimal precioCompra) { this.precioCompra = precioCompra; }

    public Integer getStockActual() { return stockActual; }
    public void setStockActual(Integer stockActual) { this.stockActual = stockActual; }

    public Integer getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(Integer stockMinimo) { this.stockMinimo = stockMinimo; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public String getImagenPrincipal() { return imagenPrincipal; }
    public void setImagenPrincipal(String imagenPrincipal) { this.imagenPrincipal = imagenPrincipal; }

    public String getEtiqueta() { return etiqueta; }
    public void setEtiqueta(String etiqueta) { this.etiqueta = etiqueta; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public LocalDateTime getUltimaActualizacion() { return ultimaActualizacion; }
    public void setUltimaActualizacion(LocalDateTime ultimaActualizacion) { this.ultimaActualizacion = ultimaActualizacion; }

    /**
     * Calcula el margen de utilidad como porcentaje: ((precioVenta - precioCompra) / precioCompra) * 100
     * Método transient (no se persiste en BD), usado por las plantillas Thymeleaf
     */
    @Transient
    public BigDecimal getMargenUtilidad() {
        if (precioCompra != null && precioVenta != null && precioCompra.compareTo(BigDecimal.ZERO) > 0) {
            return precioVenta.subtract(precioCompra)
                    .divide(precioCompra, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(2, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }

    @PreUpdate
    public void preUpdate() {
        this.ultimaActualizacion = LocalDateTime.now();
    }
}