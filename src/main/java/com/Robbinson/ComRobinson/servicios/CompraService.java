package com.Robbinson.ComRobinson.servicios;

import com.Robbinson.ComRobinson.modelo.Compra;
import com.Robbinson.ComRobinson.repositorio.CompraRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar operaciones de Compras
 * Utiliza JPA Repository para interactuar con la base de datos
 */
@Service
@Transactional
public class CompraService {

    private final CompraRepository compraRepository;

    public CompraService(CompraRepository compraRepository) {
        this.compraRepository = compraRepository;
    }

    /**
     * Guardar o actualizar una compra
     */
    public Compra guardarCompra(Compra compra) {
        return compraRepository.save(compra);
    }

    /**
     * Obtener todas las compras
     */
    @Transactional(readOnly = true)
    public List<Compra> obtenerTodasLasCompras() {
        return compraRepository.findAll();
    }

    /**
     * Obtener compra por ID
     */
    @Transactional(readOnly = true)
    public Optional<Compra> obtenerCompraPorId(Long id) {
        return compraRepository.findById(id);
    }

    /**
     * Buscar compra por número
     */
    @Transactional(readOnly = true)
    public Optional<Compra> buscarPorNumeroCompra(String numeroCompra) {
        return compraRepository.findByNumeroCompra(numeroCompra);
    }

    /**
     * Obtener compras por proveedor
     */
    @Transactional(readOnly = true)
    public List<Compra> obtenerComprasPorProveedor(Long proveedorId) {
        return compraRepository.findByProveedorIdProveedor(proveedorId);
    }

    /**
     * Obtener compras por estado
     */
    @Transactional(readOnly = true)
    public List<Compra> obtenerComprasPorEstado(Compra.EstadoCompra estado) {
        return compraRepository.findByEstado(estado);
    }

    /**
     * Obtener compras en rango de fechas
     */
    @Transactional(readOnly = true)
    public List<Compra> obtenerComprasPorFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return compraRepository.findByFechaCompraBetween(fechaInicio, fechaFin);
    }

    /**
     * Cambiar estado de compra
     */
    public boolean cambiarEstadoCompra(Long id, String nuevoEstado) {
        Optional<Compra> compraOpt = compraRepository.findById(id);
        if (compraOpt.isPresent()) {
            Compra compra = compraOpt.get();
            compra.setEstado(Compra.EstadoCompra.valueOf(nuevoEstado.toUpperCase()));
            compraRepository.save(compra);
            return true;
        }
        return false;
    }

    /**
     * Eliminar una compra por ID
     */
    public boolean eliminarCompra(Long id) {
        if (compraRepository.existsById(id)) {
            compraRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Calcular total de compras
     */
    @Transactional(readOnly = true)
    public BigDecimal calcularTotalCompras() {
        return compraRepository.findAll().stream()
                .map(Compra::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
