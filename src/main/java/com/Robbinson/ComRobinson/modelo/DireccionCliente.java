package com.Robbinson.ComRobinson.modelo;

import jakarta.persistence.*;

/**
 * Entidad DireccionCliente - Representa las direcciones de envío de los clientes
 * Mapeada a la tabla 'direcciones_cliente' en la base de datos
 */
@Entity
@Table(name = "direcciones_cliente")
public class DireccionCliente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_direccion")
    private Long idDireccion;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;
    
    @Column(name = "alias", length = 50)
    private String alias;
    
    @Column(name = "direccion", nullable = false, length = 255)
    private String direccion;
    
    @Column(name = "referencia", length = 255)
    private String referencia;
    
    @Column(name = "distrito", length = 100)
    private String distrito;
    
    @Column(name = "provincia", length = 100)
    private String provincia;
    
    @Column(name = "departamento", length = 100)
    private String departamento;
    
    @Column(name = "codigo_postal", length = 10)
    private String codigoPostal;
    
    @Column(name = "es_principal", nullable = false)
    private Boolean esPrincipal = false;

    // Constructor vacío requerido por JPA
    public DireccionCliente() {
    }

    // Constructor con campos principales
    public DireccionCliente(Cliente cliente, String direccion) {
        this.cliente = cliente;
        this.direccion = direccion;
    }

    // Getters y Setters
    public Long getIdDireccion() {
        return idDireccion;
    }

    public void setIdDireccion(Long idDireccion) {
        this.idDireccion = idDireccion;
    }

    public Long getId() {
        return idDireccion;
    }

    public void setId(Long id) {
        this.idDireccion = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public Boolean getEsPrincipal() {
        return esPrincipal;
    }

    public void setEsPrincipal(Boolean esPrincipal) {
        this.esPrincipal = esPrincipal;
    }

    // Método para obtener dirección completa
    public String getDireccionCompleta() {
        StringBuilder sb = new StringBuilder();
        sb.append(direccion);
        if (distrito != null && !distrito.isEmpty()) {
            sb.append(", ").append(distrito);
        }
        if (provincia != null && !provincia.isEmpty()) {
            sb.append(", ").append(provincia);
        }
        if (departamento != null && !departamento.isEmpty()) {
            sb.append(", ").append(departamento);
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "DireccionCliente{" +
                "idDireccion=" + idDireccion +
                ", alias='" + alias + '\'' +
                ", direccion='" + direccion + '\'' +
                ", esPrincipal=" + esPrincipal +
                '}';
    }
}
