package com.Robbinson.ComRobinson.servicios;

import java.math.BigDecimal;
import java.time.Year;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Robbinson.ComRobinson.modelo.Compra;
import com.Robbinson.ComRobinson.modelo.DetalleCompra;
import com.Robbinson.ComRobinson.repositorio.CompraRepository;
import com.Robbinson.ComRobinson.repositorio.DetalleCompraRepository;

@Service
@Transactional
public class CompraService {

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private DetalleCompraRepository detalleCompraRepository;

    /**
     * Genera un número de compra único con formato COM-YYYY-NNN
     */
    public String generarNumeroCompra() {
        long count = compraRepository.count() + 1;
        return String.format("COM-%d-%03d", Year.now().getValue(), count);
    }

    /**
     * Crear compra completa.
     * NOTA: Al insertar cada DetalleCompra, el TRIGGER de MySQL
     * 'actualizar_stock_compra' se ejecuta automáticamente y SUMA el stock.
     * El TRIGGER 'calcular_subtotal_detalle_compra' calcula el subtotal de cada línea.
     */
    public Compra crearCompra(Compra compra) {
        if (compra.getNumeroCompra() == null || compra.getNumeroCompra().isEmpty()) {
            compra.setNumeroCompra(generarNumeroCompra());
        }

        BigDecimal total = BigDecimal.ZERO;
        for (DetalleCompra detalle : compra.getDetalles()) {
            BigDecimal lineaSubtotal = detalle.getPrecioUnitario()
                    .multiply(BigDecimal.valueOf(detalle.getCantidad()));
            total = total.add(lineaSubtotal);
            detalle.setSubtotal(lineaSubtotal);
            detalle.setCompra(compra);
        }

        compra.setTotal(total);

        // Guardar compra (cascade guarda los detalles)
        // Al insertar cada detalle, MySQL ejecuta:
        // 1. TRIGGER calcular_subtotal_detalle_compra (BEFORE INSERT)
        // 2. TRIGGER actualizar_stock_compra (AFTER INSERT) → suma stock
        return compraRepository.save(compra);
    }

    public List<Compra> obtenerTodasLasCompras() {
        return compraRepository.findAllByOrderByFechaCompraDesc();
    }

    public Optional<Compra> obtenerCompraPorId(Long id) {
        return compraRepository.findById(id);
    }

    public Optional<Compra> buscarPorNumeroCompra(String numeroCompra) {
        return compraRepository.findByNumeroCompra(numeroCompra);
    }

    public List<Compra> obtenerComprasPorProveedor(Long idProveedor) {
        return compraRepository.findByProveedorIdProveedor(idProveedor);
    }

    public List<Compra> obtenerComprasPorEstado(Compra.EstadoCompra estado) {
        return compraRepository.findByEstado(estado);
    }

    public Compra actualizarEstado(Long id, Compra.EstadoCompra nuevoEstado) {
        return compraRepository.findById(id)
                .map(compra -> {
                    compra.setEstado(nuevoEstado);
                    return compraRepository.save(compra);
                })
                .orElse(null);
    }

    public boolean eliminarCompra(Long id) {
        if (compraRepository.existsById(id)) {
            compraRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public long contarCompras() {
        return compraRepository.count();
    }

    public List<DetalleCompra> obtenerDetallesPorCompra(Long idCompra) {
        return detalleCompraRepository.findByCompraIdCompra(idCompra);
    }
}