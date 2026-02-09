package com.Robbinson.ComRobinson.repositorio;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Robbinson.ComRobinson.modelo.Compra;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {

    Optional<Compra> findByNumeroCompra(String numeroCompra);

    List<Compra> findByProveedorIdProveedor(Long idProveedor);

    List<Compra> findByEstado(Compra.EstadoCompra estado);

    List<Compra> findByFechaCompraBetween(LocalDateTime inicio, LocalDateTime fin);

    boolean existsByNumeroCompra(String numeroCompra);

    List<Compra> findAllByOrderByFechaCompraDesc();
}