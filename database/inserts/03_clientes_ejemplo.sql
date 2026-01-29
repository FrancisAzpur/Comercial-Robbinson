-- ====================================================================
-- INSERTS DE EJEMPLO - CLIENTES
-- Datos de prueba para testing del sistema
-- ====================================================================

USE robbinson_db;

-- Clientes de ejemplo
INSERT INTO clientes (nombre_completo, correo_electronico, contrasena_hash, telefono, tipo_documento, documento_identidad, activo) VALUES
('Juan Carlos Pérez López', 'juan.perez@gmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '987654321', 'DNI', '12345678', TRUE),
('María Fernanda Rodríguez Silva', 'maria.rodriguez@hotmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '998765432', 'DNI', '87654321', TRUE),
('Roberto Carlos Sánchez Torres', 'roberto.sanchez@gmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '912345678', 'DNI', '23456789', TRUE),
('Ana Lucía Gómez Vargas', 'ana.gomez@yahoo.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '956789012', 'DNI', '34567890', TRUE),
('Comercial Robinson SAC', 'contacto@robinson.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '014567890', 'RUC', '20123456789', TRUE);

-- Direcciones de clientes
INSERT INTO direcciones_cliente (id_cliente, alias, direccion, referencia, distrito, provincia, departamento, codigo_postal, es_principal) VALUES
(1, 'Casa', 'Av. Larco 1234', 'Frente al parque Kennedy', 'Miraflores', 'Lima', 'Lima', '15074', TRUE),
(1, 'Trabajo', 'Jr. de la Unión 456', 'Cerca a Plaza San Martín', 'Cercado de Lima', 'Lima', 'Lima', '15001', FALSE),
(2, 'Casa', 'Calle Los Olivos 789', 'Al lado de la municipalidad', 'San Isidro', 'Lima', 'Lima', '15073', TRUE),
(3, 'Casa', 'Av. Brasil 2020', 'Cerca al hospital', 'Jesús María', 'Lima', 'Lima', '15072', TRUE),
(4, 'Casa', 'Jr. Cusco 321', 'A dos cuadras del mercado', 'Breña', 'Lima', 'Lima', '15082', TRUE),
(5, 'Oficina Principal', 'Av. Javier Prado 1500', 'Torre Empresarial, Piso 10', 'San Isidro', 'Lima', 'Lima', '15073', TRUE);

-- Mensaje de confirmación
SELECT 'Se han insertado 5 clientes de ejemplo y 6 direcciones' AS Resultado;
