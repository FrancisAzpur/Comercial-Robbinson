package com.Robbinson.ComRobinson.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
 * 
 * =========================================================================
 * ENCRIPTACIÓN DE CONTRASEÑAS - BCryptPasswordEncoder
 * =========================================================================
 * Se usa BCryptPasswordEncoder (de spring-security-crypto) para:
 *   1. ENCRIPTAR la contraseña al registrar un cliente → guardarCliente()
 *   2. VERIFICAR la contraseña al hacer login → autenticar()
 *   3. ENCRIPTAR la nueva contraseña al recuperarla → actualizarContrasenaPorCorreo()
 * 
 * BCrypt genera un hash con salt aleatorio incorporado, por lo que
 * dos encriptaciones de la misma contraseña producen hashes diferentes,
 * pero bcrypt.matches() sabe compararlos correctamente.
 * 
 * La instancia se crea directamente (new BCryptPasswordEncoder()) sin
 * necesidad de Spring Security completo, solo el módulo 'crypto'.
 * =========================================================================
 */
@Service
@Transactional
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    // ========================================================================
    // BCryptPasswordEncoder — Encriptación de contraseñas
    // Se instancia una vez y se reutiliza en todo el servicio.
    // Ubicación: com.Robbinson.ComRobinson.servicios.ClienteService
    // ========================================================================
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Guarda un nuevo cliente en la BD.
     * ENCRIPTACIÓN BCrypt: La contraseña en texto plano (contrasenaHash)
     * se encripta con BCrypt antes de persistirla en la base de datos.
     */
    public Cliente guardarCliente(Cliente cliente) {
        // ── BCrypt: encriptar contraseña antes de guardar ──
        if (cliente.getContrasenaHash() != null && !cliente.getContrasenaHash().isEmpty()) {
            String hashEncriptado = passwordEncoder.encode(cliente.getContrasenaHash());
            cliente.setContrasenaHash(hashEncriptado);
        }
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
     * Autentica un cliente por correo y contraseña.
     * VERIFICACIÓN BCrypt: Compara la contraseña en texto plano ingresada
     * contra el hash almacenado en la BD usando passwordEncoder.matches().
     * Retorna el Cliente si las credenciales son correctas, o vacío si no.
     */
    public Optional<Cliente> autenticar(String correo, String contrasena) {
        Optional<Cliente> cliente = clienteRepository.findByCorreoElectronico(correo);
        if (cliente.isPresent()) {
            String hashAlmacenado = cliente.get().getContrasenaHash();
            // ── BCrypt: verificar contraseña contra el hash almacenado ──
            if (passwordEncoder.matches(contrasena, hashAlmacenado)) {
                // Solo permite login si el cliente está activo
                if (Boolean.TRUE.equals(cliente.get().getActivo())) {
                    return cliente;
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Actualiza la contraseña del cliente identificado por correo.
     * ENCRIPTACIÓN BCrypt: La nueva contraseña se encripta antes de guardarse.
     * Retorna true si encontró el cliente y guardó la nueva contraseña.
     */
    public boolean actualizarContrasenaPorCorreo(String correo, String nuevaContrasena) {
        Optional<Cliente> opt = clienteRepository.findByCorreoElectronico(correo);
        if (opt.isPresent()) {
            Cliente cliente = opt.get();
            // ── BCrypt: encriptar la nueva contraseña antes de guardar ──
            cliente.setContrasenaHash(passwordEncoder.encode(nuevaContrasena));
            clienteRepository.save(cliente);
            return true;
        }
        return false;
    }
}