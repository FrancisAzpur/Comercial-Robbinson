package com.Robbinson.ComRobinson.servicios;

import com.Robbinson.ComRobinson.modelo.Proveedor;
import com.Robbinson.ComRobinson.repositorio.ProveedorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar operaciones de Proveedores
 * Utiliza JPA Repository para interactuar con la base de datos
 */
@Service
@Transactional
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;

    public ProveedorService(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    /**
     * Guardar o actualizar un proveedor
     */
    public Proveedor guardarProveedor(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    /**
     * Obtener todos los proveedores
     */
    @Transactional(readOnly = true)
    public List<Proveedor> obtenerTodosLosProveedores() {
        return proveedorRepository.findAll();
    }

    /**
     * Obtener proveedor por ID
     */
    @Transactional(readOnly = true)
    public Optional<Proveedor> obtenerProveedorPorId(Long id) {
        return proveedorRepository.findById(id);
    }

    /**
     * Buscar proveedor por RUC
     */
    @Transactional(readOnly = true)
    public Optional<Proveedor> buscarPorRuc(String ruc) {
        return proveedorRepository.findByRuc(ruc);
    }

    /**
     * Buscar proveedores por nombre
     */
    @Transactional(readOnly = true)
    public List<Proveedor> buscarPorNombre(String nombre) {
        return proveedorRepository.findByNombreEmpresaContainingIgnoreCase(nombre);
    }

    /**
     * Obtener proveedores activos
     */
    @Transactional(readOnly = true)
    public List<Proveedor> obtenerProveedoresActivos() {
        return proveedorRepository.findByActivoTrue();
    }

    /**
     * Eliminar un proveedor por ID
     */
    public boolean eliminarProveedor(Long id) {
        if (proveedorRepository.existsById(id)) {
            proveedorRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Contar proveedores activos
     */
    @Transactional(readOnly = true)
    public long contarProveedoresActivos() {
        return proveedorRepository.countByActivoTrue();
    }
}
