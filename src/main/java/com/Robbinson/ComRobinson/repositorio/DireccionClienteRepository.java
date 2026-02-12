package com.Robbinson.ComRobinson.repositorio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Robbinson.ComRobinson.modelo.DireccionCliente;

/**
 * =========================================================================
 * REPOSITORIO DE DIRECCIONES - Acceso a datos de 'direcciones_cliente'
 * =========================================================================
 * PUNTO DE EVALUACIÓN: Consultas multi-tabla (Dirección → Cliente)
 * 
 * Permite buscar direcciones por cliente y obtener la dirección principal.
 * =========================================================================
 */
@Repository
public interface DireccionClienteRepository extends JpaRepository<DireccionCliente, Long> {

    /** Obtener todas las direcciones de un cliente (MULTI-TABLA) */
    List<DireccionCliente> findByClienteIdCliente(Long idCliente);

    /** Obtener la dirección principal de un cliente - para el checkout */
    Optional<DireccionCliente> findByClienteIdClienteAndEsPrincipal(Long idCliente, Boolean esPrincipal);

    /** Eliminar todas las direcciones de un cliente (en cascada lógica) */
    void deleteByClienteIdCliente(Long idCliente);
}