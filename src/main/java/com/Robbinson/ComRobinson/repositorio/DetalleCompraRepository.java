package com.Robbinson.ComRobinson.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Robbinson.ComRobinson.modelo.DetalleCompra;

@Repository
public interface DetalleCompraRepository extends JpaRepository<DetalleCompra, Long> {

    List<DetalleCompra> findByCompraIdCompra(Long idCompra);

    List<DetalleCompra> findByProductoIdProducto(Long idProducto);

    void deleteByCompraIdCompra(Long idCompra);
}