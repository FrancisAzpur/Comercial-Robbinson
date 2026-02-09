package com.Robbinson.ComRobinson.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Robbinson.ComRobinson.modelo.Cliente;
import com.Robbinson.ComRobinson.repositorio.ClienteRepository;

/**
 * Servicio para gestionar operaciones de Clientes
 * Utiliza JPA Repository para interactuar con la base de datos
 */
@Service
@Transactional
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente guardarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public List<Cliente> obtenerTodosLosClientes() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> obtenerClientePorId(Long id) {
        return clienteRepository.findById(id);
    }

    public Optional<Cliente> buscarPorCorreo(String correo) {
        return clienteRepository.findByCorreoElectronico(correo);
    }

    public Optional<Cliente> buscarPorDocumento(String documento) {
        return clienteRepository.findByDocumentoIdentidad(documento);
    }

    public List<Cliente> buscarPorNombre(String nombre) {
        return clienteRepository.findByNombreCompletoContainingIgnoreCase(nombre);
    }

    public List<Cliente> obtenerClientesActivos() {
        return clienteRepository.findByActivo(true);
    }

    public boolean correoExiste(String correo) {
        return clienteRepository.existsByCorreoElectronico(correo);
    }

    public boolean documentoExiste(String documento) {
        return clienteRepository.existsByDocumentoIdentidad(documento);
    }

    public Cliente actualizarCliente(Long id, Cliente clienteActualizado) {
        return clienteRepository.findById(id)
                .map(cliente -> {
                    cliente.setNombreCompleto(clienteActualizado.getNombreCompleto());
                    cliente.setTelefono(clienteActualizado.getTelefono());
                    cliente.setTipoDocumento(clienteActualizado.getTipoDocumento());
                    cliente.setDocumentoIdentidad(clienteActualizado.getDocumentoIdentidad());
                    cliente.setActivo(clienteActualizado.getActivo());
                    return clienteRepository.save(cliente);
                })
                .orElse(null);
    }

    public boolean eliminarCliente(Long id) {
        if (clienteRepository.existsById(id)) {
            clienteRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public long contarClientes() {
        return clienteRepository.count();
    }
}