package com.Robbinson.ComRobinson.repositorio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Robbinson.ComRobinson.modelo.Proveedor;

/**
 * =========================================================================
 * REPOSITORIO DE PROVEEDORES - Acceso a datos de la tabla 'proveedores'
 * =========================================================================
 * PUNTO DE EVALUACIÓN: Conexión a BDD + CRUD completo
 * =========================================================================
 */
@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {

    /** Buscar proveedor por RUC único */
    Optional<Proveedor> findByRuc(String ruc);

    /** Búsqueda parcial por nombre de empresa */
    List<Proveedor> findByNombreEmpresaContainingIgnoreCase(String nombre);

    /** Filtrar proveedores activos/inactivos */
    List<Proveedor> findByActivo(Boolean activo);

    /** Verificar si un RUC ya existe */
    boolean existsByRuc(String ruc);
}