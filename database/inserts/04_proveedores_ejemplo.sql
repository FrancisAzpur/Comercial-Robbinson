-- ====================================================================
-- INSERTS DE EJEMPLO - PROVEEDORES
-- Proveedores de productos para el sistema
-- ====================================================================

USE robbinson_db;

-- Proveedores
INSERT INTO proveedores (nombre_empresa, ruc, contacto_nombre, contacto_telefono, contacto_email, direccion, ciudad, pais, activo) VALUES
('Samsung Electronics Perú SAC', '20501234567', 'Carlos Mendoza', '014561234', 'ventas@samsung.pe', 'Av. República de Panamá 3535', 'Lima', 'Perú', TRUE),
('LG Electronics Perú SA', '20502345678', 'Patricia Quispe', '014562345', 'contacto@lg.com.pe', 'Av. Caminos del Inca 257', 'Lima', 'Perú', TRUE),
('Indurama Perú SA', '20503456789', 'Miguel Ramírez', '014563456', 'ventas@indurama.com.pe', 'Av. Argentina 2833', 'Lima', 'Perú', TRUE),
('Sole Perú SAC', '20504567890', 'Lucía Fernández', '014564567', 'info@soleperu.com', 'Jr. Huaraz 234', 'Lima', 'Perú', TRUE),
('Oster Perú Importaciones SA', '20505678901', 'Jorge Castro', '014565678', 'ventas@oster.pe', 'Av. Universitaria 1801', 'Lima', 'Perú', TRUE),
('Textiles Hogar SAC', '20506789012', 'Rosa Villanueva', '014566789', 'contacto@textilhogar.com', 'Av. Colonial 1250', 'Lima', 'Perú', TRUE),
('Vajillas Premium SAC', '20507890123', 'Antonio Chávez', '014567890', 'ventas@vajillaspremium.pe', 'Jr. Puno 456', 'Lima', 'Perú', TRUE),
('Decoración Total EIRL', '20508901234', 'Elena Morales', '014568901', 'info@decoraciontotal.com', 'Av. Petit Thouars 3050', 'Lima', 'Perú', TRUE);

-- Mensaje de confirmación
SELECT 'Se han insertado 8 proveedores de ejemplo' AS Resultado;
