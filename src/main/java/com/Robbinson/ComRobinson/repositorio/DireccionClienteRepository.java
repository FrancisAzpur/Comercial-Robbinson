package com.Robbinson.ComRobinson.repositorio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Robbinson.ComRobinson.modelo.DireccionCliente;

@Repository
public interface DireccionClienteRepository extends JpaRepository<DireccionCliente, Long> {

    List<DireccionCliente> findByClienteIdCliente(Long idCliente);

    Optional<DireccionCliente> findByClienteIdClienteAndEsPrincipal(Long idCliente, Boolean esPrincipal);

    void deleteByClienteIdCliente(Long idCliente);
}