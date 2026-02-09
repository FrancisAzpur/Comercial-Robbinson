package com.Robbinson.ComRobinson.repositorio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Robbinson.ComRobinson.modelo.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    Optional<Cliente> findByCorreoElectronico(String correoElectronico);
    
    Optional<Cliente> findByDocumentoIdentidad(String documentoIdentidad);
    
    List<Cliente> findByNombreCompletoContainingIgnoreCase(String nombre);
    
    List<Cliente> findByActivo(Boolean activo);
    
    boolean existsByCorreoElectronico(String correoElectronico);
    
    boolean existsByDocumentoIdentidad(String documentoIdentidad);
}