-- ====================================================================
-- INSERTS PARA PRODUCTOS - ELECTRODOMÉSTICOS
-- Datos extraídos de: /static/js/electrodomesticos.js
-- Total: 15 productos
-- ====================================================================

USE robbinson_db;

-- Refrigeración
INSERT INTO productos (codigo_producto, nombre_producto, descripcion, precio_venta, precio_compra, stock_actual, stock_minimo, imagen_principal, etiqueta) VALUES
('ELEC-REF-001', 'Refrigerador 420L', 'Eficiencia A++', 2899.00, 2300.00, 15, 3, '/img/refrigerador.webp', NULL),
('ELEC-REF-002', 'Refrigerador 690L', 'Dispensador de agua y hielo', 5599.00, 4500.00, 8, 2, '/img/Refrigueradora_Samsung_690L.jpg', 'NUEVO'),
('ELEC-REF-003', 'Aire Acondicionado Split 18000 BTU', 'Inverter silencioso', 2199.00, 1800.00, 12, 3, '/img/Aire Acondicionado Split 18000 BTU.jpg', NULL);

-- TV y Audio
INSERT INTO productos (codigo_producto, nombre_producto, descripcion, precio_venta, precio_compra, stock_actual, stock_minimo, imagen_principal, etiqueta) VALUES
('ELEC-TV-001', 'Televisor SAMSUNG QLED 75"', 'UHD 4K Smart TV QN75Q60DAGXPE', 1899.00, 1500.00, 10, 2, '/img/Televisor SAMSUNG QLED  UHD 75 4K.jpg', 'OFERTA'),
('ELEC-TV-002', 'Televisor LG LED 43" HD', 'Smart TV Modelo 43LM6300PLA', 2999.00, 2400.00, 18, 4, '/img/Televisor LG LED 43.jpg', NULL),
('ELEC-TV-003', 'Smart TV OLED 55"', '4K UHD + HDR', 4499.00, 3600.00, 7, 2, '/img/tv_oled_55.avif', 'PREMIUM'),
('ELEC-TV-004', 'Smart TV 65" QLED', 'Quantum Dot, 120Hz', 5999.00, 4800.00, 5, 2, '/img/Samsung TV 65 OLED.jpg', 'NUEVO'),
('ELEC-TV-005', 'Soundbar 5.1 Dolby Atmos', 'Audio envolvente', 999.00, 750.00, 20, 5, '/img/Soundbar Dolby Atmos.jpg', NULL);

-- Lavado
INSERT INTO productos (codigo_producto, nombre_producto, descripcion, precio_venta, precio_compra, stock_actual, stock_minimo, imagen_principal, etiqueta) VALUES
('ELEC-LAV-001', 'Lavadora Secadora 10kg', 'Silenciosa y eficiente', 1799.00, 1400.00, 12, 3, '/img/lavadora_inverter.webp', NULL),
('ELEC-LAV-002', 'Secadora de Ropa 10kg', 'Sensor de humedad', 1499.00, 1200.00, 14, 3, '/img/Secadora de ropa 10kg.jpg', NULL);

-- Cocina
INSERT INTO productos (codigo_producto, nombre_producto, descripcion, precio_venta, precio_compra, stock_actual, stock_minimo, imagen_principal, etiqueta) VALUES
('ELEC-COC-001', 'Cocina a Gas 6 Hornillas Indurama', 'Acero inoxidable', 1299.00, 1000.00, 10, 2, '/img/Cocina a Gas 6 Hornillas Indurama.jpg', NULL),
('ELEC-COC-002', 'Horno Microondas 28L', 'Fácil modo de uso', 499.00, 380.00, 25, 5, '/img/Horno Microondas 28L.jpg', NULL),
('ELEC-COC-003', 'Licuadora Industrial 2L Jhumy', '1200W de potencia', 349.00, 270.00, 30, 8, '/img/Licuadora Industrial 2L Jhumy.jpg', 'OFERTA'),
('ELEC-COC-004', 'Campana Extractora 90cm Sole', '3 velocidades', 799.00, 600.00, 15, 3, '/img/Campana Extractora 90cm Sole.jpg', NULL),
('ELEC-COC-005', 'Horno Eléctrico 60L', 'Empotrable, convección', 899.00, 700.00, 12, 3, '/img/Horno Eléctrico 60L.jpg', NULL),
('ELEC-COC-006', 'Lavavajillas 14 Servicios', '6 programas de lavado', 1999.00, 1600.00, 8, 2, '/img/Lavavajillas 14 Servicios.jpg', 'NUEVO');

-- Mensaje de confirmación
SELECT 'Se han insertado 15 productos de electrodomésticos' AS Resultado;
