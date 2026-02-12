package com.Robbinson.ComRobinson.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Robbinson.ComRobinson.modelo.Proveedor;
import com.Robbinson.ComRobinson.repositorio.ProveedorRepository;

/**
 * =========================================================================
 * SERVICIO DE PROVEEDORES - Lógica de negocio para la tabla 'proveedores'
 * =========================================================================
 * PUNTO DE EVALUACIÓN: CRUD completo con conexión a BDD
 * 
 * Gestiona los proveedores de productos del sistema.
 * Se usa junto con CompraService para el flujo de abastecimiento.
 * =========================================================================
 */
@Service
@Transactional
public class ProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepository;

    public Proveedor guardarProveedor(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    public List<Proveedor> obtenerTodosLosProveedores() {
        return proveedorRepository.findAll();
    }

    public Optional<Proveedor> obtenerProveedorPorId(Long id) {
        return proveedorRepository.findById(id);
    }

    public Optional<Proveedor> buscarPorRuc(String ruc) {
        return proveedorRepository.findByRuc(ruc);
    }

    public List<Proveedor> buscarPorNombre(String nombre) {
        return proveedorRepository.findByNombreEmpresaContainingIgnoreCase(nombre);
    }

    public List<Proveedor> obtenerProveedoresActivos() {
        return proveedorRepository.findByActivo(true);
    }

    public boolean rucExiste(String ruc) {
        return proveedorRepository.existsByRuc(ruc);
    }

    public Proveedor actualizarProveedor(Long id, Proveedor proveedorActualizado) {
        return proveedorRepository.findById(id)
                .map(proveedor -> {
                    proveedor.setNombreEmpresa(proveedorActualizado.getNombreEmpresa());
                    proveedor.setRuc(proveedorActualizado.getRuc());
                    proveedor.setContactoNombre(proveedorActualizado.getContactoNombre());
                    proveedor.setContactoTelefono(proveedorActualizado.getContactoTelefono());
                    proveedor.setContactoEmail(proveedorActualizado.getContactoEmail());
                    proveedor.setDireccion(proveedorActualizado.getDireccion());
                    proveedor.setCiudad(proveedorActualizado.getCiudad());
                    proveedor.setPais(proveedorActualizado.getPais());
                    proveedor.setActivo(proveedorActualizado.getActivo());
                    return proveedorRepository.save(proveedor);
                })
                .orElse(null);
    }

    public boolean eliminarProveedor(Long id) {
        if (proveedorRepository.existsById(id)) {
            proveedorRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public long contarProveedores() {
        return proveedorRepository.count();
    }
}