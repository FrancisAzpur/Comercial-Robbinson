package com.Robbinson.ComRobinson.repositorio;

import com.Robbinson.ComRobinson.modelo.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad Proveedor
 * Proporciona operaciones CRUD y consultas personalizadas
 */
@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {

    // Buscar proveedor por RUC
    Optional<Proveedor> findByRuc(String ruc);

    // Buscar proveedores por nombre de empresa (búsqueda parcial)
    List<Proveedor> findByNombreEmpresaContainingIgnoreCase(String nombreEmpresa);

    // Obtener proveedores activos
    List<Proveedor> findByActivoTrue();

    // Obtener proveedores por ciudad
    List<Proveedor> findByCiudadIgnoreCase(String ciudad);

    // Verificar si existe un proveedor con el RUC dado
    boolean existsByRuc(String ruc);

    // Contar proveedores activos
    long countByActivoTrue();

    // Obtener proveedores ordenados por nombre
    @Query("SELECT p FROM Proveedor p WHERE p.activo = true ORDER BY p.nombreEmpresa")
    List<Proveedor> findProveedoresActivosOrdenados();
}
