package com.Robbinson.ComRobinson.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Robbinson.ComRobinson.modelo.DireccionCliente;
import com.Robbinson.ComRobinson.repositorio.DireccionClienteRepository;

/**
 * =========================================================================
 * SERVICIO DE DIRECCIONES - Lógica de negocio para 'direcciones_cliente'
 * =========================================================================
 * PUNTO DE EVALUACIÓN: CRUD + Consulta multi-tabla (Dirección → Cliente)
 * 
 * Gestiona las direcciones de envío de los clientes.
 * Incluye la lógica de dirección principal (esPrincipal=true).
 * =========================================================================
 */
@Service
@Transactional
public class DireccionClienteService {

    @Autowired
    private DireccionClienteRepository direccionClienteRepository;

    public DireccionCliente guardarDireccion(DireccionCliente direccion) {
        return direccionClienteRepository.save(direccion);
    }

    public List<DireccionCliente> obtenerTodasLasDirecciones() {
        return direccionClienteRepository.findAll();
    }

    public Optional<DireccionCliente> obtenerDireccionPorId(Long id) {
        return direccionClienteRepository.findById(id);
    }

    public List<DireccionCliente> obtenerDireccionesPorCliente(Long idCliente) {
        return direccionClienteRepository.findByClienteIdCliente(idCliente);
    }

    public Optional<DireccionCliente> obtenerDireccionPrincipal(Long idCliente) {
        return direccionClienteRepository.findByClienteIdClienteAndEsPrincipal(idCliente, true);
    }

    public DireccionCliente actualizarDireccion(Long id, DireccionCliente direccionActualizada) {
        return direccionClienteRepository.findById(id)
                .map(direccion -> {
                    direccion.setAlias(direccionActualizada.getAlias());
                    direccion.setDireccion(direccionActualizada.getDireccion());
                    direccion.setReferencia(direccionActualizada.getReferencia());
                    direccion.setDistrito(direccionActualizada.getDistrito());
                    direccion.setProvincia(direccionActualizada.getProvincia());
                    direccion.setDepartamento(direccionActualizada.getDepartamento());
                    direccion.setCodigoPostal(direccionActualizada.getCodigoPostal());
                    direccion.setEsPrincipal(direccionActualizada.getEsPrincipal());
                    return direccionClienteRepository.save(direccion);
                })
                .orElse(null);
    }

    public boolean eliminarDireccion(Long id) {
        if (direccionClienteRepository.existsById(id)) {
            direccionClienteRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public long contarDirecciones() {
        return direccionClienteRepository.count();
    }
}
