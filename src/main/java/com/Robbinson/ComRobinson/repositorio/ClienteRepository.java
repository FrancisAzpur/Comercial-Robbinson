package com.Robbinson.ComRobinson.repositorio;

import com.Robbinson.ComRobinson.modelo.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad Cliente
 * Proporciona operaciones CRUD y consultas personalizadas
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // Buscar cliente por correo electrónico
    Optional<Cliente> findByCorreoElectronico(String correoElectronico);

    // Buscar cliente por documento de identidad
    Optional<Cliente> findByDocumentoIdentidad(String documentoIdentidad);

    // Buscar clientes por nombre (búsqueda parcial, ignorando mayúsculas)
    List<Cliente> findByNombreCompletoContainingIgnoreCase(String nombre);

    // Obtener todos los clientes activos
    List<Cliente> findByActivoTrue();

    // Obtener todos los clientes inactivos
    List<Cliente> findByActivoFalse();

    // Verificar si existe un cliente con el correo dado
    boolean existsByCorreoElectronico(String correoElectronico);

    // Verificar si existe un cliente con el documento dado
    boolean existsByDocumentoIdentidad(String documentoIdentidad);

    // Consulta nativa para obtener clientes activos
    @Query(value = "SELECT * FROM clientes WHERE activo = true ORDER BY nombre_completo", nativeQuery = true)
    List<Cliente> obtenerClientesActivosOrdenados();

    // Consulta JPQL para buscar por tipo de documento
    @Query("SELECT c FROM Cliente c WHERE c.tipoDocumento = :tipo AND c.activo = true")
    List<Cliente> findByTipoDocumentoAndActivo(@Param("tipo") Cliente.TipoDocumento tipo);

    // Contar clientes activos
    long countByActivoTrue();
}
