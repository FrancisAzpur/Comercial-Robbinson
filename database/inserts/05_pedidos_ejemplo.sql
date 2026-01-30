-- ====================================================================
-- INSERTS DE EJEMPLO - PEDIDOS Y VENTAS
-- Ejemplos de transacciones de ventas
-- ====================================================================

USE robbinson_db;

-- Pedido 1: Juan Carlos Pérez López
INSERT INTO pedidos (numero_pedido, id_cliente, id_direccion, subtotal, impuesto, costo_envio, total, metodo_pago, estado) VALUES
('PED-2026-001', 1, 1, 2899.00, 521.82, 50.00, 3470.82, 'TARJETA_CREDITO', 'ENTREGADO');

INSERT INTO detalle_pedidos (id_pedido, id_producto, cantidad, precio_unitario) VALUES
(1, 1, 1, 2899.00); -- Refrigerador 420L

-- Pedido 2: María Fernanda Rodríguez Silva
INSERT INTO pedidos (numero_pedido, id_cliente, id_direccion, subtotal, impuesto, costo_envio, total, metodo_pago, estado) VALUES
('PED-2026-002', 2, 3, 5498.00, 989.64, 80.00, 6567.64, 'TRANSFERENCIA', 'ENVIADO');

INSERT INTO detalle_pedidos (id_pedido, id_producto, cantidad, precio_unitario) VALUES
(2, 3, 1, 4499.00), -- Smart TV OLED 55"
(2, 8, 1, 999.00);  -- Soundbar

-- Pedido 3: Roberto Carlos Sánchez Torres
INSERT INTO pedidos (numero_pedido, id_cliente, id_direccion, subtotal, impuesto, costo_envio, total, metodo_pago, estado) VALUES
('PED-2026-003', 3, 4, 2698.00, 485.64, 60.00, 3243.64, 'YAPE', 'PROCESANDO');

INSERT INTO detalle_pedidos (id_pedido, id_producto, cantidad, precio_unitario) VALUES
(3, 9, 1, 1799.00), -- Lavadora Secadora 10kg
(3, 11, 1, 499.00), -- Horno Microondas 28L
(3, 16, 2, 200.00); -- Juego de sábanas 2 plazas (x2)

-- Pedido 4: Ana Lucía Gómez Vargas
INSERT INTO pedidos (numero_pedido, id_cliente, id_direccion, subtotal, impuesto, costo_envio, total, metodo_pago, estado) VALUES
('PED-2026-004', 4, 5, 829.50, 149.31, 30.00, 1008.81, 'EFECTIVO', 'PAGADO');

INSERT INTO detalle_pedidos (id_pedido, id_producto, cantidad, precio_unitario) VALUES
(4, 25, 1, 249.90), -- Juego de Vajilla 60 Piezas
(4, 19, 3, 89.00),  -- Juego de sábanas 1 plaza (x3)
(4, 29, 2, 99.90);  -- Planta Grande Eucalipto (x2)

-- Pedido 5: Juan Carlos Pérez López (segundo pedido)
INSERT INTO pedidos (numero_pedido, id_cliente, id_direccion, subtotal, impuesto, costo_envio, total, metodo_pago, estado) VALUES
('PED-2026-005', 1, 2, 1648.00, 296.64, 40.00, 1984.64, 'TARJETA_DEBITO', 'ENTREGADO');

INSERT INTO detalle_pedidos (id_pedido, id_producto, cantidad, precio_unitario) VALUES
(5, 12, 1, 349.00),  -- Licuadora Industrial
(5, 13, 1, 799.00),  -- Campana Extractora
(5, 23, 2, 140.00),  -- Juego de toallas algodón (x2)
(5, 22, 1, 60.00),   -- Almohada hotelera (x1)
(5, 28, 2, 129.90);  -- Planta Olivo Artificial (x2)

-- Mensaje de confirmación
SELECT 'Se han insertado 5 pedidos de ejemplo con sus detalles' AS Resultado;
SELECT COUNT(*) AS Total_Pedidos FROM pedidos;
SELECT SUM(total) AS Ventas_Totales FROM pedidos WHERE estado IN ('PAGADO', 'ENVIADO', 'ENTREGADO');
