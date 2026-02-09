package com.Robbinson.ComRobinson.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Robbinson.ComRobinson.modelo.Producto;
import com.Robbinson.ComRobinson.repositorio.ProductoRepository;

@Service
@Transactional
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    public List<Producto> obtenerTodosLosProductos() {
        return productoRepository.findAll();
    }

    public Optional<Producto> obtenerProductoPorId(Long id) {
        return productoRepository.findById(id);
    }

    public Optional<Producto> buscarPorCodigo(String codigo) {
        return productoRepository.findByCodigoProducto(codigo);
    }

    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreProductoContainingIgnoreCase(nombre);
    }

    public List<Producto> obtenerProductosActivos() {
        return productoRepository.findByActivoTrueOrderByNombreProductoAsc();
    }

    public List<Producto> obtenerPorEtiqueta(String etiqueta) {
        return productoRepository.findByEtiqueta(etiqueta);
    }

    public List<Producto> obtenerProductosConStockBajo() {
        return productoRepository.findProductosConStockBajo();
    }

    public boolean codigoExiste(String codigo) {
        return productoRepository.existsByCodigoProducto(codigo);
    }

    public Producto actualizarProducto(Long id, Producto productoActualizado) {
        return productoRepository.findById(id)
                .map(producto -> {
                    producto.setNombreProducto(productoActualizado.getNombreProducto());
                    producto.setDescripcion(productoActualizado.getDescripcion());
                    producto.setPrecioVenta(productoActualizado.getPrecioVenta());
                    producto.setPrecioCompra(productoActualizado.getPrecioCompra());
                    producto.setStockActual(productoActualizado.getStockActual());
                    producto.setStockMinimo(productoActualizado.getStockMinimo());
                    producto.setActivo(productoActualizado.getActivo());
                    producto.setImagenPrincipal(productoActualizado.getImagenPrincipal());
                    producto.setEtiqueta(productoActualizado.getEtiqueta());
                    return productoRepository.save(producto);
                })
                .orElse(null);
    }

    public boolean eliminarProducto(Long id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public long contarProductos() {
        return productoRepository.count();
    }

    public long contarProductosActivos() {
        return productoRepository.countByActivo(true);
    }
}