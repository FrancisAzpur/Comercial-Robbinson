package com.Robbinson.ComRobinson.servicios;

import com.Robbinson.ComRobinson.modelo.Cliente;
import com.Robbinson.ComRobinson.repositorio.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar operaciones de Clientes
 * Utiliza JPA Repository para interactuar con la base de datos
 */
@Service
@Transactional
public class ClienteService {

    private final ClienteRepository clienteRepository;

    // Inyección de dependencias por constructor (recomendado)
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    /**
     * Guardar o actualizar un cliente
     */
    public Cliente guardarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    /**
     * Obtener todos los clientes
     */
    @Transactional(readOnly = true)
    public List<Cliente> obtenerTodosLosClientes() {
        return clienteRepository.findAll();
    }

    /**
     * Obtener cliente por ID
     */
    @Transactional(readOnly = true)
    public Optional<Cliente> obtenerClientePorId(Long id) {
        return clienteRepository.findById(id);
    }

    /**
     * Buscar clientes por nombre
     */
    @Transactional(readOnly = true)
    public List<Cliente> buscarPorNombre(String nombre) {
        return clienteRepository.findByNombreCompletoContainingIgnoreCase(nombre);
    }

    /**
     * Buscar cliente por email
     */
    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorEmail(String email) {
        return clienteRepository.findByCorreoElectronico(email);
    }

    /**
     * Obtener clientes activos
     */
    @Transactional(readOnly = true)
    public List<Cliente> obtenerClientesActivos() {
        return clienteRepository.findByActivoTrue();
    }

    /**
     * Actualizar un cliente existente
     */
    public boolean actualizarCliente(Long id, Cliente clienteActualizado) {
        Optional<Cliente> clienteExistente = clienteRepository.findById(id);
        if (clienteExistente.isPresent()) {
            Cliente cliente = clienteExistente.get();
            cliente.setNombreCompleto(clienteActualizado.getNombreCompleto());
            cliente.setCorreoElectronico(clienteActualizado.getCorreoElectronico());
            cliente.setTelefono(clienteActualizado.getTelefono());
            cliente.setTipoDocumento(clienteActualizado.getTipoDocumento());
            cliente.setDocumentoIdentidad(clienteActualizado.getDocumentoIdentidad());
            cliente.setActivo(clienteActualizado.getActivo());
            clienteRepository.save(cliente);
            return true;
        }
        return false;
    }

    /**
     * Eliminar un cliente por ID
     */
    public boolean eliminarCliente(Long id) {
        if (clienteRepository.existsById(id)) {
            clienteRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Contar total de clientes
     */
    @Transactional(readOnly = true)
    public long contarClientes() {
        return clienteRepository.count();
    }

    /**
     * Contar clientes activos
     */
    @Transactional(readOnly = true)
    public long contarClientesActivos() {
        return clienteRepository.countByActivoTrue();
    }

    /**
     * Verificar si el email ya existe
     */
    @Transactional(readOnly = true)
    public boolean emailYaExiste(String email) {
        return clienteRepository.existsByCorreoElectronico(email);
    }

    /**
     * Agregar cliente (alias para compatibilidad)
     */
    public Cliente agregarCliente(Cliente cliente) {
        return guardarCliente(cliente);
    }
}
