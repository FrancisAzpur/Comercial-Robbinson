package com.Robbinson.ComRobinson.repositorio;

import com.Robbinson.ComRobinson.modelo.DireccionCliente;
import com.Robbinson.ComRobinson.modelo.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad DireccionCliente
 * Proporciona operaciones CRUD y consultas personalizadas
 */
@Repository
public interface DireccionClienteRepository extends JpaRepository<DireccionCliente, Long> {

    // Obtener direcciones por cliente
    List<DireccionCliente> findByCliente(Cliente cliente);

    // Obtener direcciones por ID de cliente
    List<DireccionCliente> findByClienteIdCliente(Long clienteId);

    // Obtener dirección principal de un cliente
    Optional<DireccionCliente> findByClienteIdClienteAndEsPrincipalTrue(Long clienteId);

    // Buscar direcciones por distrito
    List<DireccionCliente> findByDistritoIgnoreCase(String distrito);

    // Eliminar direcciones de un cliente
    void deleteByCliente(Cliente cliente);

    // Contar direcciones de un cliente
    long countByClienteIdCliente(Long clienteId);

    // Obtener direcciones principales
    @Query("SELECT d FROM DireccionCliente d WHERE d.esPrincipal = true")
    List<DireccionCliente> findDireccionesPrincipales();
}
