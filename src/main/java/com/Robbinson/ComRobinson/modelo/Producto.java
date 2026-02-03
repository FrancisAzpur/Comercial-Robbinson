package com.Robbinson.ComRobinson.modelo;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    // Constructor vacío requerido por JPA
    public Producto() {
        this.fechaRegistro = LocalDateTime.now();
        this.ultimaActualizacion = LocalDateTime.now();
    }

    // Constructor con campos principales
    public Producto(String codigoProducto, String nombreProducto, BigDecimal precioVenta, BigDecimal precioCompra) {
        this();
        this.codigoProducto = codigoProducto;
        this.nombreProducto = nombreProducto;
        this.precioVenta = precioVenta;
        this.precioCompra = precioCompra;
    }

    // Métodos de ciclo de vida JPA
    @PrePersist
    protected void onCreate() {
        fechaRegistro = LocalDateTime.now();
        ultimaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        ultimaActualizacion = LocalDateTime.now();
    }

    // Método para calcular margen de utilidad
    public BigDecimal getMargenUtilidad() {
        if (precioCompra != null && precioCompra.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal diferencia = precioVenta.subtract(precioCompra);
            return diferencia.divide(precioCompra, 4, java.math.RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));
        }
        return BigDecimal.ZERO;
    }

    // Método para verificar stock bajo
    public boolean tieneStockBajo() {
        return stockActual <= stockMinimo;
    }

    // Getters y Setters
    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    // Alias para compatibilidad
    public Long getId() {
        return idProducto;
    }

    public void setId(Long id) {
        this.idProducto = id;
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    // Alias para compatibilidad
    public String getNombre() {
        return nombreProducto;
    }

    public void setNombre(String nombre) {
        this.nombreProducto = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(BigDecimal precioVenta) {
        this.precioVenta = precioVenta;
    }

    // Alias para compatibilidad
    public BigDecimal getPrecio() {
        return precioVenta;
    }

    public void setPrecio(BigDecimal precio) {
        this.precioVenta = precio;
    }

    public BigDecimal getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(BigDecimal precioCompra) {
        this.precioCompra = precioCompra;
    }

    public Integer getStockActual() {
        return stockActual;
    }

    public void setStockActual(Integer stockActual) {
        this.stockActual = stockActual;
    }

    // Alias para compatibilidad
    public int getStock() {
        return stockActual != null ? stockActual : 0;
    }

    public void setStock(int stock) {
        this.stockActual = stock;
    }

    public Integer getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(Integer stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getImagenPrincipal() {
        return imagenPrincipal;
    }

    public void setImagenPrincipal(String imagenPrincipal) {
        this.imagenPrincipal = imagenPrincipal;
    }

    // Alias para compatibilidad
    public String getImagen() {
        return imagenPrincipal;
    }

    public void setImagen(String imagen) {
        this.imagenPrincipal = imagen;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    // Método para verificar si está en oferta
    public boolean isEnOferta() {
        return "OFERTA".equalsIgnoreCase(etiqueta) || "DESCUENTO".equalsIgnoreCase(etiqueta);
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public LocalDateTime getUltimaActualizacion() {
        return ultimaActualizacion;
    }

    public void setUltimaActualizacion(LocalDateTime ultimaActualizacion) {
        this.ultimaActualizacion = ultimaActualizacion;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "idProducto=" + idProducto +
                ", codigoProducto='" + codigoProducto + '\'' +
                ", nombreProducto='" + nombreProducto + '\'' +
                ", precioVenta=" + precioVenta +
                ", stockActual=" + stockActual +
                ", activo=" + activo +
                '}';
    }
}
