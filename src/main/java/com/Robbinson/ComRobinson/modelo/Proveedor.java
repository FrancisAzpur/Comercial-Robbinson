package com.Robbinson.ComRobinson.modelo;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad Proveedor - Representa los proveedores de productos
 * Mapeada a la tabla 'proveedores' en la base de datos
 */
@Entity
@Table(name = "proveedores")
public class Proveedor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proveedor")
    private Long idProveedor;
    
    @Column(name = "nombre_empresa", nullable = false, length = 100)
    private String nombreEmpresa;
    
    @Column(name = "ruc", nullable = false, unique = true, length = 11)
    private String ruc;
    
    @Column(name = "contacto_nombre", length = 100)
    private String contactoNombre;
    
    @Column(name = "contacto_telefono", length = 15)
    private String contactoTelefono;
    
    @Column(name = "contacto_email", length = 100)
    private String contactoEmail;
    
    @Column(name = "direccion", length = 255)
    private String direccion;
    
    @Column(name = "ciudad", length = 100)
    private String ciudad;
    
    @Column(name = "pais", length = 50)
    private String pais = "Perú";
    
    @Column(name = "activo", nullable = false)
    private Boolean activo = true;
    
    @Column(name = "fecha_registro", updatable = false)
    private LocalDateTime fechaRegistro;
    
    @Column(name = "ultima_actualizacion")
    private LocalDateTime ultimaActualizacion;

    // Constructor vacío requerido por JPA
    public Proveedor() {
        this.fechaRegistro = LocalDateTime.now();
        this.ultimaActualizacion = LocalDateTime.now();
    }

    // Constructor con campos principales
    public Proveedor(String nombreEmpresa, String ruc) {
        this();
        this.nombreEmpresa = nombreEmpresa;
        this.ruc = ruc;
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

    // Getters y Setters
    public Long getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Long idProveedor) {
        this.idProveedor = idProveedor;
    }

    public Long getId() {
        return idProveedor;
    }

    public void setId(Long id) {
        this.idProveedor = id;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getContactoNombre() {
        return contactoNombre;
    }

    public void setContactoNombre(String contactoNombre) {
        this.contactoNombre = contactoNombre;
    }

    public String getContactoTelefono() {
        return contactoTelefono;
    }

    public void setContactoTelefono(String contactoTelefono) {
        this.contactoTelefono = contactoTelefono;
    }

    public String getContactoEmail() {
        return contactoEmail;
    }

    public void setContactoEmail(String contactoEmail) {
        this.contactoEmail = contactoEmail;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
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
        return "Proveedor{" +
                "idProveedor=" + idProveedor +
                ", nombreEmpresa='" + nombreEmpresa + '\'' +
                ", ruc='" + ruc + '\'' +
                ", activo=" + activo +
                '}';
    }
}
