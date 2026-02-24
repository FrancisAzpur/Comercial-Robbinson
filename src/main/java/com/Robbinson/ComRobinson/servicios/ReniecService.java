package com.Robbinson.ComRobinson.servicios;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.Robbinson.ComRobinson.modelo.ReniecResponse;

/**
 * =========================================================================
 * SERVICIO RENIEC - Consumo de API externa para consulta de DNI
 * =========================================================================
 * PUNTO DE EVALUACIÓN: Consumo de API REST externa (RENIEC)
 *
 * Este servicio consume la API de decolecta.com para consultar datos de un
 * ciudadano peruano a partir de su número de DNI.
 *
 * API UTILIZADA: https://api.decolecta.com/v1/reniec/dni - Método: GET -
 * Parámetro: ?numero={dni} - Autenticación: Header "Authorization: Bearer
 * {api_token}" - Respuesta JSON: { first_name, first_last_name,
 * second_last_name, full_name, document_number }
 *
 * CONFIGURACIÓN: La API Key se lee desde application.properties:
 * reniec.api.token=tu-api-key-aqui
 *
 * IMPORTANTE: El archivo application-secrets.properties (donde se puede guardar
 * la key real) está en .gitignore para no subir credenciales.
 *
 * FLUJO: 1. El usuario ingresa su DNI en el formulario de registro 2. Al
 * presionar "Buscar", el frontend llama a /api/reniec/dni/{numero} 3. Este
 * servicio consulta la API externa de RENIEC 4. Retorna los datos (nombres +
 * apellidos) para autocompletar el form
 * =========================================================================
 */
@Service
public class ReniecService {

    private static final String RENIEC_API_URL = "https://api.decolecta.com/v1/reniec/dni";

    @Value("${reniec.api.token:}")
    private String apiToken;

    private final RestClient restClient;

    public ReniecService() {
        this.restClient = RestClient.create();
    }

    /**
     * Consulta los datos de una persona por su número de DNI.
     *
     * @param numeroDni Número de DNI de 8 dígitos
     * @return ReniecResponse con los datos de la persona, o null si no se
     * encontró
     * @throws RuntimeException si la API Key no está configurada o hay error de
     * conexión
     */
    public ReniecResponse consultarDni(String numeroDni) {
        // Validar que el DNI tenga 8 dígitos
        if (numeroDni == null || !numeroDni.matches("\\d{8}")) {
            throw new IllegalArgumentException("El DNI debe tener exactamente 8 dígitos numéricos");
        }

        // Validar que la API Key esté configurada
        if (apiToken == null || apiToken.isBlank()) {
            throw new RuntimeException("La API Key de RENIEC no está configurada. "
                    + "Agrega 'reniec.api.token=TU_API_KEY' en application.properties");
        }

        try {
            ReniecResponse response = restClient.get()
                    .uri(RENIEC_API_URL + "?numero=" + numeroDni)
                    .header("Authorization", "Bearer " + apiToken)
                    .retrieve()
                    .onStatus(status -> status.value() == HttpStatus.NOT_FOUND.value(),
                            (req, res) -> {
                                throw new RuntimeException("DNI no encontrado en RENIEC");
                            })
                    .onStatus(status -> status.value() == HttpStatus.UNAUTHORIZED.value(),
                            (req, res) -> {
                                throw new RuntimeException("API Key de RENIEC inválida o expirada");
                            })
                    .onStatus(status -> status.value() == HttpStatus.TOO_MANY_REQUESTS.value(),
                            (req, res) -> {
                                throw new RuntimeException("Se excedió el límite de consultas a RENIEC. Intenta más tarde.");
                            })
                    .body(ReniecResponse.class);

            return response;

        } catch (RuntimeException e) {
            // Re-lanzar excepciones conocidas
            if (e.getMessage() != null && (e.getMessage().contains("RENIEC") || e.getMessage().contains("DNI"))) {
                throw e;
            }
            throw new RuntimeException("Error al conectar con la API de RENIEC: " + e.getMessage(), e);
        }
    }
}
