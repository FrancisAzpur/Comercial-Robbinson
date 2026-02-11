package com.Robbinson.ComRobinson.modelo;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

/**
 * =========================================================================
 * ENTIDAD CLIENTE - Tabla 'clientes' en la Base de Datos
 * =========================================================================
 * PUNTO DE EVALUACIÓN: CRUD de tablas con conexión a BDD
 * 
 * Esta clase es una ENTIDAD JPA que mapea directamente a la tabla 'clientes'
 * en MySQL. Spring Data JPA + Hibernate se encarga de:
 *   - Crear/actualizar la tabla automáticamente (ddl-auto=update)
 *   - Generar las consultas SQL (SELECT, INSERT, UPDATE, DELETE)
 *   - Manejar las relaciones con otras tablas (DireccionCliente, Pedido)
 * 
 * ANOTACIONES JPA CLAVE:
 *   @Entity     → Marca esta clase como entidad persistente en la BD
 *   @Table      → Especifica el nombre de la tabla en MySQL
 *   @Id         → Define la clave primaria
 *   @GeneratedValue(IDENTITY) → Auto-incremento en MySQL
 *   @Column     → Configura cada columna (nombre, restricciones, longitud)
 *   @Enumerated → Almacena el enum TipoDocumento como STRING en la BD
 *   @PreUpdate  → Callback que actualiza la fecha antes de cada UPDATE
 * 
 * RELACIONES CON OTRAS TABLAS:
 *   Cliente 1 → N DireccionCliente (un cliente tiene muchas direcciones)
 *   Cliente 1 → N Pedido (un cliente puede hacer muchos pedidos)
 * =========================================================================
 */
@Entity
@Table(name = "clientes")
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Long idCliente;

    @Column(name = "nombre_completo", nullable = false, length = 100)
    private String nombreCompleto;

    @Column(name = "correo_electronico", nullable = false, unique = true, length = 100)
    private String correoElectronico;

    @Column(name = "contrasena_hash", nullable = false, length = 255)
    private String contrasenaHash;

    @Column(name = "telefono", length = 15)
    private String telefono;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_documento")
    private TipoDocumento tipoDocumento = TipoDocumento.DNI;

    @Column(name = "documento_identidad", unique = true, length = 20)
    private String documentoIdentidad; // nombre cambiado para mayor claridad

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @Column(name = "fecha_registro", updatable = false)
    private LocalDateTime fechaRegistro;

    @Column(name = "ultima_actualizacion")
    private LocalDateTime ultimaActualizacion;

    public enum TipoDocumento {
        DNI, RUC, PASAPORTE
    }

    // Constructores
    public Cliente() {
        this.fechaRegistro = LocalDateTime.now();
        this.ultimaActualizacion = LocalDateTime.now();
    }

    public Cliente(String nombreCompleto, String correoElectronico, String contrasenaHash) {
        this();
        this.nombreCompleto = nombreCompleto;
        this.correoElectronico = correoElectronico;
        this.contrasenaHash = contrasenaHash;
    }

    // Getters y Setters
    public Long getIdCliente() { return idCliente; }
    public void setIdCliente(Long idCliente) { this.idCliente = idCliente; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getCorreoElectronico() { return correoElectronico; }
    public void setCorreoElectronico(String correoElectronico) { this.correoElectronico = correoElectronico; }

    public String getContrasenaHash() { return contrasenaHash; }
    public void setContrasenaHash(String contrasenaHash) { this.contrasenaHash = contrasenaHash; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public TipoDocumento getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(TipoDocumento tipoDocumento) { this.tipoDocumento = tipoDocumento; }

    public String getDocumentoIdentidad() { return documentoIdentidad; }
    public void setDocumentoIdentidad(String documentoIdentidad) { this.documentoIdentidad = documentoIdentidad; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public LocalDateTime getUltimaActualizacion() { return ultimaActualizacion; }
    public void setUltimaActualizacion(LocalDateTime ultimaActualizacion) { this.ultimaActualizacion = ultimaActualizacion; }

    @PreUpdate
    public void preUpdate() {
        this.ultimaActualizacion = LocalDateTime.now();
    }
}