package com.Robbinson.ComRobinson.servicios;

import com.Robbinson.ComRobinson.modelo.Producto;
import com.Robbinson.ComRobinson.repositorio.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar operaciones de Productos
 * Utiliza JPA Repository para interactuar con la base de datos
 */
@Service
@Transactional
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    /**
     * Guardar o actualizar un producto
     */
    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    /**
     * Obtener todos los productos
     */
    @Transactional(readOnly = true)
    public List<Producto> obtenerTodosLosProductos() {
        return productoRepository.findAll();
    }

    /**
     * Obtener producto por ID
     */
    @Transactional(readOnly = true)
    public Optional<Producto> obtenerProductoPorId(Long id) {
        return productoRepository.findById(id);
    }

    /**
     * Buscar producto por código
     */
    @Transactional(readOnly = true)
    public Optional<Producto> buscarPorCodigo(String codigo) {
        return productoRepository.findByCodigoProducto(codigo);
    }

    /**
     * Buscar productos por nombre
     */
    @Transactional(readOnly = true)
    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreProductoContainingIgnoreCase(nombre);
    }

    /**
     * Obtener productos activos
     */
    @Transactional(readOnly = true)
    public List<Producto> obtenerProductosActivos() {
        return productoRepository.findByActivoTrue();
    }

    /**
     * Obtener productos en oferta
     */
    @Transactional(readOnly = true)
    public List<Producto> obtenerProductosEnOferta() {
        return productoRepository.findProductosEnOferta();
    }

    /**
     * Obtener productos con stock bajo
     */
    @Transactional(readOnly = true)
    public List<Producto> obtenerProductosConStockBajo() {
        return productoRepository.findProductosConStockBajo();
    }

    /**
     * Obtener productos disponibles (con stock)
     */
    @Transactional(readOnly = true)
    public List<Producto> obtenerProductosDisponibles() {
        return productoRepository.findProductosDisponibles();
    }

    /**
     * Buscar productos en rango de precio
     */
    @Transactional(readOnly = true)
    public List<Producto> buscarPorRangoPrecio(BigDecimal precioMin, BigDecimal precioMax) {
        return productoRepository.findByPrecioVentaBetweenAndActivoTrue(precioMin, precioMax);
    }

    /**
     * Actualizar stock de un producto
     */
    public boolean actualizarStock(Long productoId, Integer cantidad) {
        Optional<Producto> productoOpt = productoRepository.findById(productoId);
        if (productoOpt.isPresent()) {
            Producto producto = productoOpt.get();
            producto.setStockActual(producto.getStockActual() + cantidad);
            productoRepository.save(producto);
            return true;
        }
        return false;
    }

    /**
     * Eliminar un producto por ID
     */
    public boolean eliminarProducto(Long id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Contar productos activos
     */
    @Transactional(readOnly = true)
    public long contarProductosActivos() {
        return productoRepository.countByActivoTrue();
    }
}
