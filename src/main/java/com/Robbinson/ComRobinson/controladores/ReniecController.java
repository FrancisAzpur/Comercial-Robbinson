package com.Robbinson.ComRobinson.controladores;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Robbinson.ComRobinson.modelo.ReniecResponse;
import com.Robbinson.ComRobinson.servicios.ReniecService;

/**
 * =========================================================================
 * CONTROLADOR REST RENIEC - API para consulta de DNI
 * =========================================================================
 * PUNTO DE EVALUACIÓN: Consumo de API externa + Endpoint REST propio
 *
 * Este controlador expone un endpoint REST que actúa como INTERMEDIARIO entre
 * el frontend y la API externa de RENIEC.
 *
 * ¿Por qué un intermediario? - La API Key NO se expone al navegador del usuario
 * (seguridad) - Se validan los datos antes de enviarlos a la API externa - Se
 * puede cachear o limitar las consultas
 *
 * FLUJO: Frontend (JS) → /api/reniec/dni/12345678 → ReniecController ↓
 * ReniecService ↓ API externa apis.net.pe ↓ Retorna JSON al frontend
 *
 * ENDPOINT: GET /api/reniec/dni/{numero} → Consulta DNI y retorna datos
 * personales
 * =========================================================================
 */
@RestController
@RequestMapping("/api/reniec")
public class ReniecController {

    @Autowired
    private ReniecService reniecService;

    /**
     * GET /api/reniec/dni/{numero} Consulta un DNI en la API de RENIEC y
     * retorna los datos de la persona.
     *
     * @param numero Número de DNI de 8 dígitos
     * @return JSON con: exito, nombres, apellidoPaterno, apellidoMaterno,
     * nombreCompleto
     */
    @GetMapping("/dni/{numero}")
    public ResponseEntity<Map<String, Object>> consultarDni(@PathVariable String numero) {
        Map<String, Object> response = new HashMap<>();

        try {
            ReniecResponse datos = reniecService.consultarDni(numero);

            if (datos == null || datos.getNombres() == null) {
                response.put("exito", false);
                response.put("mensaje", "No se encontraron datos para el DNI ingresado");
                return ResponseEntity.ok(response);
            }

            response.put("exito", true);
            response.put("nombres", datos.getNombres());
            response.put("apellidoPaterno", datos.getApellidoPaterno());
            response.put("apellidoMaterno", datos.getApellidoMaterno());
            response.put("nombreCompleto", datos.getNombreCompletoFormateado());
            response.put("numeroDocumento", datos.getNumeroDocumento());

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            response.put("exito", false);
            response.put("mensaje", e.getMessage());
            return ResponseEntity.badRequest().body(response);

        } catch (RuntimeException e) {
            response.put("exito", false);
            response.put("mensaje", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
