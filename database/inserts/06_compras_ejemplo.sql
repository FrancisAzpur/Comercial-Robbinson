-- ====================================================================
-- INSERTS DE EJEMPLO - COMPRAS A PROVEEDORES
-- Ejemplos de órdenes de compra a proveedores
-- ====================================================================

USE robbinson_db;

-- Compra 1: Samsung Electronics - Refrigeradores y TVs
INSERT INTO compras (numero_compra, id_proveedor, total, estado, observaciones) VALUES
('COM-2026-001', 1, 58000.00, 'RECIBIDA', 'Compra mensual de electrodomésticos Samsung');

INSERT INTO detalle_compras (id_compra, id_producto, cantidad, precio_unitario) VALUES
(1, 1, 10, 2300.00),  -- Refrigerador 420L
(1, 2, 5, 4500.00),   -- Refrigerador 690L
(1, 4, 8, 1500.00);   -- Televisor SAMSUNG QLED 75"

-- Compra 2: LG Electronics - TVs
INSERT INTO compras (numero_compra, id_proveedor, total, estado, observaciones) VALUES
('COM-2026-002', 2, 48000.00, 'RECIBIDA', 'Restock de televisores LG');

INSERT INTO detalle_compras (id_compra, id_producto, cantidad, precio_unitario) VALUES
(2, 5, 20, 2400.00);  -- Televisor LG LED 43"

-- Compra 3: Indurama - Cocinas
INSERT INTO compras (numero_compra, id_proveedor, total, estado, observaciones) VALUES
('COM-2026-003', 3, 20000.00, 'PAGADA', 'Cocinas a gas para temporada alta');

INSERT INTO detalle_compras (id_compra, id_producto, cantidad, precio_unitario) VALUES
(3, 10, 20, 1000.00); -- Cocina a Gas 6 Hornillas

-- Compra 4: Textiles Hogar - Sábanas y Toallas
INSERT INTO compras (numero_compra, id_proveedor, total, estado, observaciones) VALUES
('COM-2026-004', 6, 12250.00, 'RECIBIDA', 'Restock de textiles para hogar');

INSERT INTO detalle_compras (id_compra, id_producto, cantidad, precio_unitario) VALUES
(4, 19, 50, 65.00),   -- Juego de sábanas 1 plaza
(4, 20, 50, 90.00),   -- Juego de sábanas 2 plazas
(4, 23, 40, 105.00);  -- Juego de toallas algodón

-- Compra 5: Vajillas Premium - Vajillas
INSERT INTO compras (numero_compra, id_proveedor, total, estado, observaciones) VALUES
('COM-2026-005', 7, 8275.00, 'PENDIENTE', 'Vajillas para temporada de fiestas');

INSERT INTO detalle_compras (id_compra, id_producto, cantidad, precio_unitario) VALUES
(5, 25, 20, 190.00),  -- Juego de Vajilla 60 Piezas
(5, 26, 25, 150.00),  -- Juego de Vajilla 30 Piezas Paula
(5, 27, 30, 75.00);   -- Vajilla 16 Piezas con Textura

-- Mensaje de confirmación
SELECT 'Se han insertado 5 compras de ejemplo con sus detalles' AS Resultado;
SELECT COUNT(*) AS Total_Compras FROM compras;
SELECT SUM(total) AS Compras_Totales FROM compras WHERE estado IN ('RECIBIDA', 'PAGADA');
