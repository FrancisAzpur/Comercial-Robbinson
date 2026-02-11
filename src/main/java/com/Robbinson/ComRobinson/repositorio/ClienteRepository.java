package com.Robbinson.ComRobinson.repositorio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Robbinson.ComRobinson.modelo.Cliente;

/**
 * =========================================================================
 * REPOSITORIO DE CLIENTES - Acceso a datos de la tabla 'clientes'
 * =========================================================================
 * PUNTO DE EVALUACIÓN: Conexión a BDD + Consultas personalizadas
 * 
 * Extiende JpaRepository<Cliente, Long> que proporciona automáticamente:
 *   - save()      → INSERT/UPDATE (CREATE/UPDATE del CRUD)
 *   - findById()  → SELECT por ID (READ del CRUD)
 *   - findAll()   → SELECT * (LISTADO)
 *   - deleteById()→ DELETE (DELETE del CRUD)
 *   - count()     → COUNT(*)
 * 
 * Spring Data JPA genera las consultas SQL automáticamente
 * a partir del nombre del método (Derived Query Methods).
 * =========================================================================
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    /** Buscar cliente por correo electrónico - para autenticación/login */
    Optional<Cliente> findByCorreoElectronico(String correoElectronico);

    /** Buscar cliente por documento de identidad */
    Optional<Cliente> findByDocumentoIdentidad(String documentoIdentidad);

    /** Búsqueda parcial por nombre (LIKE %nombre%) - para el buscador de clientes */
    List<Cliente> findByNombreCompletoContainingIgnoreCase(String nombre);

    /** Filtrar clientes activos/inactivos */
    List<Cliente> findByActivo(Boolean activo);

    /** Verificar si un correo ya existe - para validación de registro */
    boolean existsByCorreoElectronico(String correoElectronico);

    /** Verificar si un documento ya existe - para validación de registro */
    boolean existsByDocumentoIdentidad(String documentoIdentidad);
}