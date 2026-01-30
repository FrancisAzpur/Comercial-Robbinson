-- ====================================================================
-- INSERTS PARA PRODUCTOS - HOGAR
-- Datos extraídos de: /static/js/hogar.js
-- Total: 16 productos
-- ====================================================================

USE robbinson_db;

-- Menaje - Sábanas y Textiles
INSERT INTO productos (codigo_producto, nombre_producto, descripcion, precio_venta, precio_compra, stock_actual, stock_minimo, imagen_principal, etiqueta) VALUES
('HOGAR-MEN-001', 'Juego de sábanas 200 hilos | 1 plaza', 'Sábanas de algodón suave', 89.00, 65.00, 40, 10, '/img/sabanas1.webp', NULL),
('HOGAR-MEN-002', 'Juego de sábanas 200 hilos | 2 plazas', 'Ideal para cama matrimonial', 120.00, 90.00, 35, 10, '/img/sabana2.jpg', NULL),
('HOGAR-MEN-003', 'Edredón reversible cama matrimonial', 'Diseño elegante y moderno', 200.00, 150.00, 25, 5, '/img/sabanas3.webp', NULL),
('HOGAR-MEN-004', 'Almohada hotelera premium (unidad)', 'Máximo confort', 60.00, 45.00, 50, 15, '/img/almohada.avif', 'PREMIUM');

-- Menaje - Toallas
INSERT INTO productos (codigo_producto, nombre_producto, descripcion, precio_venta, precio_compra, stock_actual, stock_minimo, imagen_principal, etiqueta) VALUES
('HOGAR-MEN-005', 'Juego de toallas 4 piezas algodón', 'Suaves y absorbentes', 140.00, 105.00, 30, 8, '/img/toallas1.webp', NULL),
('HOGAR-MEN-006', 'Set x4 Toallas Mano/Baño Roberta Allen Lollipop', 'Diseño exclusivo', 80.00, 60.00, 35, 10, '/img/toallas2.avif', 'OFERTA'),
('HOGAR-MEN-007', 'Toalla Clásica Baño', 'Algodón 100%', 75.00, 55.00, 45, 12, '/img/toallas3.avif', NULL),
('HOGAR-MEN-008', 'Toalla Premium Baño', 'Extra absorbente', 99.00, 75.00, 40, 10, '/img/toallas4.avif', NULL);

-- Cocina - Vajillas
INSERT INTO productos (codigo_producto, nombre_producto, descripcion, precio_venta, precio_compra, stock_actual, stock_minimo, imagen_principal, etiqueta) VALUES
('HOGAR-VAJ-001', 'Juego de Vajilla Porcelana Combo 60 Piezas', 'Juego completo para 12 personas', 249.90, 190.00, 15, 3, '/img/vajilla1.webp', 'PREMIUM'),
('HOGAR-VAJ-002', 'Juego de Vajilla Porcelana 30 Piezas Paula', 'Diseño elegante Paula', 199.90, 150.00, 18, 4, '/img/vajilla2.webp', NULL),
('HOGAR-VAJ-003', 'Vajilla x16 Piezas Porcelana con Textura', 'Textura moderna', 99.90, 75.00, 25, 6, '/img/vajilla3.webp', NULL),
('HOGAR-VAJ-004', 'Set Vajilla Decal Rosa 16 Piezas', 'Delicado diseño rosa', 49.90, 35.00, 30, 8, '/img/vajilla4.jpg', 'OFERTA');

-- Decoración - Plantas
INSERT INTO productos (codigo_producto, nombre_producto, descripcion, precio_venta, precio_compra, stock_actual, stock_minimo, imagen_principal, etiqueta) VALUES
('HOGAR-DEC-001', 'Planta Olivo Artificial 30×132 cm', 'Planta artificial grande', 129.90, 95.00, 20, 5, '/img/planta1.webp', NULL),
('HOGAR-DEC-002', 'Planta Grande Eucalipto 120 cm', 'Eucalipto decorativo', 99.90, 75.00, 22, 5, '/img/planta2.webp', NULL),
('HOGAR-DEC-003', 'Planta Ficus artificial 154 cm', 'Ficus realista', 179.90, 135.00, 15, 4, '/img/planta3.avif', 'NUEVO'),
('HOGAR-DEC-004', 'Planta Sansevieria Artificial con maceta', 'Perfecta para interiores', 83.70, 62.00, 28, 6, '/img/planta4.avif', NULL);

-- Mensaje de confirmación
SELECT 'Se han insertado 16 productos de hogar' AS Resultado;
