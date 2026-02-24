package com.Robbinson.ComRobinson.modelo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * ========================================================================= DTO
 * RENIEC RESPONSE - Datos devueltos por la API de RENIEC (decolecta.com)
 * =========================================================================
 * Clase DTO (Data Transfer Object) que mapea la respuesta JSON de la API de
 * consulta de DNI vía decolecta.com/v1/reniec/dni.
 *
 * Campos que retorna la API: - first_name: Nombre(s) de la persona -
 * first_last_name: Apellido paterno - second_last_name: Apellido materno -
 * full_name: Nombre completo (APELLIDOS + NOMBRES) - document_number: Número de
 * DNI consultado
 *
 * Se usa @JsonProperty para mapear los nombres snake_case del JSON a los campos
 * camelCase de Java.
 *
 * Método auxiliar: - getNombreCompletoFormateado(): Reordena a "NOMBRES
 * APELLIDO_PAT APELLIDO_MAT" para el campo 'nombre_completo' de la tabla
 * clientes.
 * =========================================================================
 */
public class ReniecResponse {

    @JsonProperty("first_name")
    private String nombres;

    @JsonProperty("first_last_name")
    private String apellidoPaterno;

    @JsonProperty("second_last_name")
    private String apellidoMaterno;

    @JsonProperty("full_name")
    private String nombreCompleto;

    @JsonProperty("document_number")
    private String numeroDocumento;

    // Constructores
    public ReniecResponse() {
    }

    // Getters y Setters
    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    /**
     * Retorna el nombre completo en formato "NOMBRES APELLIDO_PATERNO
     * APELLIDO_MATERNO". La API retorna full_name como "APELLIDOS NOMBRES",
     * este método reordena usando los campos individuales para que sea más
     * legible. Este valor se usa para rellenar el campo nombreCompleto del
     * Cliente.
     */
    public String getNombreCompletoFormateado() {
        StringBuilder sb = new StringBuilder();
        if (nombres != null && !nombres.isBlank()) {
            sb.append(nombres.trim());
        }
        if (apellidoPaterno != null && !apellidoPaterno.isBlank()) {
            if (sb.length() > 0) {
                sb.append(" ");
            }
            sb.append(apellidoPaterno.trim());
        }
        if (apellidoMaterno != null && !apellidoMaterno.isBlank()) {
            if (sb.length() > 0) {
                sb.append(" ");
            }
            sb.append(apellidoMaterno.trim());
        }
        return sb.toString();
    }
}
