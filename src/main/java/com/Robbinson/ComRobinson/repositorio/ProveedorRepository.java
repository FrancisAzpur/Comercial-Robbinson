package com.Robbinson.ComRobinson.repositorio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Robbinson.ComRobinson.modelo.Proveedor;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {

    Optional<Proveedor> findByRuc(String ruc);

    List<Proveedor> findByNombreEmpresaContainingIgnoreCase(String nombre);

    List<Proveedor> findByActivo(Boolean activo);

    boolean existsByRuc(String ruc);
}