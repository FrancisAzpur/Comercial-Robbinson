package com.Robbinson.ComRobinson.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.Robbinson.ComRobinson.modelo.Cliente;

/**
 * Servicio para gestionar operaciones CRUD de Clientes
 * Maneja adiciones, listados, consultas, eliminaciones y búsquedas
 */
@Service
public class ServicioCliente {

    // Simulamos una base de datos en memoria (lista)
    private List<Cliente> clientes = new ArrayList<>();
    private Long contadorId = 1L;

    /**
     * Agregar un nuevo cliente
     * @param cliente - El cliente a agregar
     * @return - El cliente agregado con ID
     */
    public Cliente agregarCliente(Cliente cliente) {
        if (cliente.getId() == null) {
            cliente.setId(contadorId++);
        }
        clientes.add(cliente);
        return cliente;
    }

    /**
     * Obtener todos los clientes registrados
     * @return - Lista de todos los clientes
     */
    public List<Cliente> obtenerTodosLosClientes() {
        return new ArrayList<>(clientes);
    }

    /**
     * Buscar un cliente por su ID
     * @param id - ID del cliente a buscar
     * @return - Optional con el cliente si existe
     */
    public Optional<Cliente> obtenerClientePorId(Long id) {
        return clientes.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    /**
     * Buscar clientes por nombre (búsqueda parcial)
     * @param nombre - Parte del nombre a buscar
     * @return - Lista de clientes que coinciden
     */
    public List<Cliente> buscarPorNombre(String nombre) {
        return clientes.stream()
                .filter(c -> c.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Buscar clientes por email
     * @param email - Email del cliente
     * @return - Optional con el cliente si existe
     */
    public Optional<Cliente> buscarPorEmail(String email) {
        return clientes.stream()
                .filter(c -> c.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    /**
     * Actualizar los datos de un cliente existente
     * @param id - ID del cliente a actualizar
     * @param clienteActualizado - Los nuevos datos del cliente
     * @return - True si se actualizó, False si no encontró el cliente
     */
    public boolean actualizarCliente(Long id, Cliente clienteActualizado) {
        Optional<Cliente> cliente = obtenerClientePorId(id);
        if (cliente.isPresent()) {
            Cliente c = cliente.get();
            c.setNombre(clienteActualizado.getNombre());
            c.setApellido(clienteActualizado.getApellido());
            c.setEmail(clienteActualizado.getEmail());
            c.setTelefono(clienteActualizado.getTelefono());
            c.setDireccion(clienteActualizado.getDireccion());
            c.setCiudad(clienteActualizado.getCiudad());
            c.setCodigoPostal(clienteActualizado.getCodigoPostal());
            return true;
        }
        return false;
    }

    /**
     * Eliminar un cliente por su ID
     * @param id - ID del cliente a eliminar
     * @return - True si se eliminó, False si no encontró el cliente
     */
    public boolean eliminarCliente(Long id) {
        return clientes.removeIf(c -> c.getId().equals(id));
    }

    /**
     * Obtener la cantidad total de clientes registrados
     * @return - Número de clientes
     */
    public int contarClientes() {
        return clientes.size();
    }

    /**
     * Buscar clientes por ciudad
     * @param ciudad - Ciudad donde vive el cliente
     * @return - Lista de clientes de esa ciudad
     */
    public List<Cliente> buscarPorCiudad(String ciudad) {
        return clientes.stream()
                .filter(c -> c.getCiudad().equalsIgnoreCase(ciudad))
                .collect(Collectors.toList());
    }

    /**
     * Verificar si un email ya está registrado
     * @param email - Email a verificar
     * @return - True si existe, False si no
     */
    public boolean emailYaExiste(String email) {
        return buscarPorEmail(email).isPresent();
    }
}
