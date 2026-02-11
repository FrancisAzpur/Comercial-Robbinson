package com.Robbinson.ComRobinson.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Robbinson.ComRobinson.modelo.Cliente;
import com.Robbinson.ComRobinson.repositorio.ClienteRepository;

/**
 * =========================================================================
 * SERVICIO DE CLIENTES - Lógica de negocio para la tabla 'clientes'
 * =========================================================================
 * PUNTO DE EVALUACIÓN: CRUD completo + Autenticación
 * 
 * @Service: Marca esta clase como componente de servicio en Spring
 * @Transactional: Todas las operaciones se ejecutan dentro de una transacción
 *                 Si ocurre un error, se hace ROLLBACK automático
 * 
 * OPERACIONES CRUD:
 *   C (Create) → guardarCliente()
 *   R (Read)   → obtenerTodosLosClientes(), obtenerClientePorId(), buscarPorNombre()
 *   U (Update) → actualizarCliente()
 *   D (Delete) → eliminarCliente()
 * 
 * FUNCIONALIDAD ADICIONAL:
 *   - autenticar(): Verifica correo + contraseña para login
 *   - correoExiste(), documentoExiste(): Validaciones de unicidad
 * =========================================================================
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

    /**
     * Autentica un cliente por correo y contraseña
     * Retorna el Cliente si las credenciales son correctas, o vacío si no
     */
    public Optional<Cliente> autenticar(String correo, String contrasena) {
        Optional<Cliente> cliente = clienteRepository.findByCorreoElectronico(correo);
        if (cliente.isPresent() && cliente.get().getContrasenaHash().equals(contrasena)) {
            // Solo permite login si el cliente está activo
            if (Boolean.TRUE.equals(cliente.get().getActivo())) {
                return cliente;
            }
        }
        return Optional.empty();
    }
}