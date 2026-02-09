-- Base de Datos Comercial Robinson - Versión Actualizada
-- Creada: 2026
-- Sistema de gestión para tienda de electrodomésticos y hogar
-- Nota: Tabla de categorías eliminada - categorías se manejan en frontend

CREATE DATABASE IF NOT EXISTS robbinson_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;
USE robbinson_db;

-- ====================================================================
-- 1) TABLA CLIENTES
-- ====================================================================
CREATE TABLE clientes (
    id_cliente BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID único del cliente',
    nombre_completo VARCHAR(100) NOT NULL COMMENT 'Nombre completo del cliente',
    correo_electronico VARCHAR(100) NOT NULL UNIQUE COMMENT 'Email único del cliente',
    contrasena_hash VARCHAR(255) NOT NULL COMMENT 'Contraseña hasheada (bcrypt)',
    telefono VARCHAR(15) COMMENT 'Teléfono de contacto',
    tipo_documento ENUM('DNI', 'RUC', 'PASAPORTE') DEFAULT 'DNI' COMMENT 'Tipo de documento de identidad',
    documento_identidad VARCHAR(20) UNIQUE COMMENT 'Número de documento único',
    activo BOOLEAN NOT NULL DEFAULT TRUE COMMENT 'Cliente activo o inactivo',
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de registro del cliente',
    ultima_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Última actualización de datos',
    
    INDEX idx_cliente_correo (correo_electronico),
    INDEX idx_cliente_documento (documento_identidad),
    INDEX idx_cliente_activo (activo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci 
COMMENT='Tabla de clientes del sistema';

-- ====================================================================
-- 2) TABLA DIRECCIONES DEL CLIENTE
-- ====================================================================
CREATE TABLE direcciones_cliente (
    id_direccion BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID único de la dirección',
    id_cliente BIGINT NOT NULL COMMENT 'Referencia al cliente',
    alias VARCHAR(50) COMMENT 'Nombre corto (ej: Casa, Trabajo, Oficina)',
    direccion VARCHAR(255) NOT NULL COMMENT 'Dirección completa',
    referencia VARCHAR(255) COMMENT 'Puntos de referencia adicionales',
    distrito VARCHAR(100) COMMENT 'Distrito',
    provincia VARCHAR(100) COMMENT 'Provincia',
    departamento VARCHAR(100) COMMENT 'Departamento/Región',
    codigo_postal VARCHAR(10) COMMENT 'Código postal',
    es_principal BOOLEAN NOT NULL DEFAULT FALSE COMMENT 'Dirección principal del cliente',
    
    FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente) ON DELETE CASCADE,
    INDEX idx_cliente_dir (id_cliente),
    INDEX idx_principal (es_principal)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT='Direcciones de envío de los clientes';

-- ====================================================================
-- 3) TABLA PRODUCTOS
-- ====================================================================
CREATE TABLE productos (
    id_producto BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID único del producto',
    codigo_producto VARCHAR(50) NOT NULL UNIQUE COMMENT 'Código interno del producto (SKU)',
    nombre_producto VARCHAR(150) NOT NULL COMMENT 'Nombre del producto',
    descripcion TEXT COMMENT 'Descripción detallada del producto',
    precio_venta DECIMAL(10,2) NOT NULL COMMENT 'Precio de venta al público',
    precio_compra DECIMAL(10,2) NOT NULL COMMENT 'Costo de compra al proveedor',
    stock_actual INT NOT NULL DEFAULT 0 COMMENT 'Cantidad disponible en stock',
    stock_minimo INT NOT NULL DEFAULT 5 COMMENT 'Stock mínimo para alertas de reorden',
    activo BOOLEAN NOT NULL DEFAULT TRUE COMMENT 'Producto activo para venta',
    imagen_principal VARCHAR(255) COMMENT 'Ruta de la imagen principal del producto',
    etiqueta VARCHAR(50) COMMENT 'Etiqueta especial (OFERTA, NUEVO, DESCUENTO, etc)',
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de registro del producto',
    ultima_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_codigo_producto (codigo_producto),
    INDEX idx_nombre_producto (nombre_producto),
    INDEX idx_activo (activo),
    INDEX idx_etiqueta (etiqueta),
    INDEX idx_precio (precio_venta)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT='Catálogo de productos (electrodomésticos y hogar)';

-- ====================================================================
-- 4) TABLA PEDIDOS (ÓRDENES DE VENTA)
-- ====================================================================
CREATE TABLE pedidos (
    id_pedido BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID único del pedido',
    numero_pedido VARCHAR(20) NOT NULL UNIQUE COMMENT 'Número de referencia del pedido (ej: PED-2026-001)',
    id_cliente BIGINT NOT NULL COMMENT 'Cliente que realiza el pedido',
    id_direccion BIGINT COMMENT 'Dirección de envío',
    fecha_pedido TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha y hora del pedido',
    subtotal DECIMAL(10,2) NOT NULL COMMENT 'Subtotal sin impuestos',
    impuesto DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT 'Impuesto (IGV 18%)',
    costo_envio DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT 'Costo de envío',
    total DECIMAL(10,2) NOT NULL COMMENT 'Total a pagar',
    metodo_pago ENUM('EFECTIVO', 'TARJETA_CREDITO', 'TARJETA_DEBITO', 'TRANSFERENCIA', 'YAPE', 'PLIN') 
                NOT NULL DEFAULT 'EFECTIVO' COMMENT 'Método de pago utilizado',
    estado ENUM('PENDIENTE', 'PAGADO', 'PROCESANDO', 'ENVIADO', 'ENTREGADO', 'CANCELADO') 
           NOT NULL DEFAULT 'PENDIENTE' COMMENT 'Estado del pedido',
    observaciones TEXT COMMENT 'Observaciones del pedido',
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente) ON DELETE CASCADE,
    FOREIGN KEY (id_direccion) REFERENCES direcciones_cliente(id_direccion) ON DELETE SET NULL,
    INDEX idx_cliente_pedido (id_cliente),
    INDEX idx_fecha_pedido (fecha_pedido),
    INDEX idx_estado (estado),
    INDEX idx_numero_pedido (numero_pedido)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT='Órdenes de compra de los clientes';

-- ====================================================================
-- 5) TABLA DETALLE DE PEDIDOS
-- ====================================================================
CREATE TABLE detalle_pedidos (
    id_detalle BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID único del detalle',
    id_pedido BIGINT NOT NULL COMMENT 'Referencia al pedido',
    id_producto BIGINT NOT NULL COMMENT 'Producto en el pedido',
    cantidad INT NOT NULL COMMENT 'Cantidad del producto',
    precio_unitario DECIMAL(10,2) NOT NULL COMMENT 'Precio unitario al momento de la compra',
    subtotal DECIMAL(10,2) NOT NULL COMMENT 'Subtotal de la línea (cantidad × precio)',
    
    FOREIGN KEY (id_pedido) REFERENCES pedidos(id_pedido) ON DELETE CASCADE,
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto) ON DELETE RESTRICT,
    INDEX idx_pedido_detalle (id_pedido),
    INDEX idx_producto_detalle (id_producto),
    
    CONSTRAINT chk_cantidad_positiva CHECK (cantidad > 0),
    CONSTRAINT chk_precio_positivo CHECK (precio_unitario > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT='Detalle de productos incluidos en cada pedido';

-- ====================================================================
-- 6) TABLA PROVEEDORES
-- ====================================================================
CREATE TABLE proveedores (
    id_proveedor BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID único del proveedor',
    nombre_empresa VARCHAR(100) NOT NULL COMMENT 'Nombre legal de la empresa',
    ruc VARCHAR(11) UNIQUE NOT NULL COMMENT 'RUC (Registro Único de Contribuyente)',
    contacto_nombre VARCHAR(100) COMMENT 'Nombre de la persona de contacto',
    contacto_telefono VARCHAR(15) COMMENT 'Teléfono del proveedor',
    contacto_email VARCHAR(100) COMMENT 'Email del proveedor',
    direccion VARCHAR(255) COMMENT 'Dirección del proveedor',
    ciudad VARCHAR(100) COMMENT 'Ciudad del proveedor',
    pais VARCHAR(50) DEFAULT 'Perú' COMMENT 'País del proveedor',
    activo BOOLEAN NOT NULL DEFAULT TRUE COMMENT 'Proveedor activo',
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de registro',
    ultima_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_ruc_proveedor (ruc),
    INDEX idx_nombre_proveedor (nombre_empresa),
    INDEX idx_activo (activo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT='Proveedores de productos';

-- ====================================================================
-- 7) TABLA COMPRAS (ÓRDENES DE COMPRA)
-- ====================================================================
CREATE TABLE compras (
    id_compra BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID único de la compra',
    numero_compra VARCHAR(20) NOT NULL UNIQUE COMMENT 'Número de referencia de la compra',
    id_proveedor BIGINT NOT NULL COMMENT 'Proveedor de la compra',
    fecha_compra TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de la compra',
    total DECIMAL(10,2) NOT NULL COMMENT 'Total de la compra',
    estado ENUM('PENDIENTE', 'RECIBIDA', 'PAGADA', 'CANCELADA') 
           NOT NULL DEFAULT 'PENDIENTE' COMMENT 'Estado de la compra',
    observaciones TEXT COMMENT 'Observaciones de la compra',
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (id_proveedor) REFERENCES proveedores(id_proveedor) ON DELETE RESTRICT,
    INDEX idx_proveedor_compra (id_proveedor),
    INDEX idx_fecha_compra (fecha_compra),
    INDEX idx_estado (estado),
    INDEX idx_numero_compra (numero_compra)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT='Órdenes de compra a proveedores';

-- ====================================================================
-- 8) TABLA DETALLE DE COMPRAS
-- ====================================================================
CREATE TABLE detalle_compras (
    id_detalle BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID único del detalle',
    id_compra BIGINT NOT NULL COMMENT 'Referencia a la compra',
    id_producto BIGINT NOT NULL COMMENT 'Producto comprado',
    cantidad INT NOT NULL COMMENT 'Cantidad comprada',
    precio_unitario DECIMAL(10,2) NOT NULL COMMENT 'Precio unitario de compra',
    subtotal DECIMAL(10,2) NOT NULL COMMENT 'Subtotal de la línea',
    
    FOREIGN KEY (id_compra) REFERENCES compras(id_compra) ON DELETE CASCADE,
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto) ON DELETE RESTRICT,
    INDEX idx_compra_detalle (id_compra),
    INDEX idx_producto_compra (id_producto),
    
    CONSTRAINT chk_compra_cantidad_positiva CHECK (cantidad > 0),
    CONSTRAINT chk_compra_precio_positivo CHECK (precio_unitario > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT='Detalle de productos incluidos en cada compra';

-- ====================================================================
-- 9) VISTAS ÚTILES
-- ====================================================================

-- Vista de productos con información de stock
CREATE OR REPLACE VIEW vista_productos_stock AS
SELECT 
    p.id_producto,
    p.codigo_producto,
    p.nombre_producto,
    p.descripcion,
    p.precio_venta,
    p.precio_compra,
    p.stock_actual,
    p.stock_minimo,
    p.activo,
    p.etiqueta,
    p.imagen_principal,
    CASE 
        WHEN p.stock_actual = 0 THEN 'SIN STOCK'
        WHEN p.stock_actual <= p.stock_minimo THEN 'STOCK BAJO'
        ELSE 'DISPONIBLE'
    END AS estado_stock,
    ROUND((p.precio_venta - p.precio_compra) / p.precio_compra * 100, 2) AS margen_utilidad
FROM productos p;

-- Vista de pedidos con información del cliente
CREATE OR REPLACE VIEW vista_pedidos_completos AS
SELECT 
    p.id_pedido,
    p.numero_pedido,
    p.fecha_pedido,
    c.id_cliente,
    c.nombre_completo AS cliente,
    c.correo_electronico,
    c.telefono,
    d.direccion AS direccion_envio,
    d.distrito,
    d.provincia,
    p.subtotal,
    p.impuesto,
    p.costo_envio,
    p.total,
    p.metodo_pago,
    p.estado,
    COUNT(dp.id_detalle) AS total_items
FROM pedidos p
INNER JOIN clientes c ON p.id_cliente = c.id_cliente
LEFT JOIN direcciones_cliente d ON p.id_direccion = d.id_direccion
LEFT JOIN detalle_pedidos dp ON p.id_pedido = dp.id_pedido
GROUP BY p.id_pedido;

-- Vista de ventas por producto
CREATE OR REPLACE VIEW vista_ventas_por_producto AS
SELECT 
    pr.id_producto,
    pr.codigo_producto,
    pr.nombre_producto,
    COUNT(DISTINCT dp.id_pedido) AS total_pedidos,
    SUM(dp.cantidad) AS total_vendido,
    SUM(dp.subtotal) AS ingresos_totales,
    AVG(dp.precio_unitario) AS precio_promedio
FROM productos pr
LEFT JOIN detalle_pedidos dp ON pr.id_producto = dp.id_producto
LEFT JOIN pedidos p ON dp.id_pedido = p.id_pedido AND p.estado IN ('PAGADO', 'ENVIADO', 'ENTREGADO')
GROUP BY pr.id_producto;

-- Vista de compras a proveedores
CREATE OR REPLACE VIEW vista_compras_proveedores AS
SELECT 
    c.id_compra,
    c.numero_compra,
    c.fecha_compra,
    prov.id_proveedor,
    prov.nombre_empresa AS proveedor,
    prov.ruc,
    prov.contacto_nombre,
    prov.contacto_telefono,
    c.total,
    c.estado,
    COUNT(dc.id_detalle) AS total_items
FROM compras c
INNER JOIN proveedores prov ON c.id_proveedor = prov.id_proveedor
LEFT JOIN detalle_compras dc ON c.id_compra = dc.id_compra
GROUP BY c.id_compra;

-- ====================================================================
-- 10) TRIGGERS
-- ====================================================================

-- Trigger para actualizar stock al registrar un detalle de pedido
DELIMITER $$
CREATE TRIGGER actualizar_stock_venta
AFTER INSERT ON detalle_pedidos
FOR EACH ROW
BEGIN
    UPDATE productos 
    SET stock_actual = stock_actual - NEW.cantidad
    WHERE id_producto = NEW.id_producto;
END$$
DELIMITER ;

-- Trigger para actualizar stock al registrar un detalle de compra
DELIMITER $$
CREATE TRIGGER actualizar_stock_compra
AFTER INSERT ON detalle_compras
FOR EACH ROW
BEGIN
    UPDATE productos 
    SET stock_actual = stock_actual + NEW.cantidad
    WHERE id_producto = NEW.id_producto;
END$$
DELIMITER ;

-- Trigger para calcular subtotal en detalle de pedidos
DELIMITER $$
CREATE TRIGGER calcular_subtotal_detalle_pedido
BEFORE INSERT ON detalle_pedidos
FOR EACH ROW
BEGIN
    SET NEW.subtotal = NEW.cantidad * NEW.precio_unitario;
END$$
DELIMITER ;

-- Trigger para calcular subtotal en detalle de compras
DELIMITER $$
CREATE TRIGGER calcular_subtotal_detalle_compra
BEFORE INSERT ON detalle_compras
FOR EACH ROW
BEGIN
    SET NEW.subtotal = NEW.cantidad * NEW.precio_unitario;
END$$
DELIMITER ;

-- ====================================================================
-- FINAL DEL SCRIPT
-- ====================================================================

-- Mensaje de confirmación
SELECT 'Base de datos robbinson_db creada exitosamente' AS Mensaje;
SELECT 'Tablas: clientes, direcciones_cliente, productos, pedidos, detalle_pedidos, proveedores, compras, detalle_compras' AS Tablas_Creadas;
SELECT 'Vistas: vista_productos_stock, vista_pedidos_completos, vista_ventas_por_producto, vista_compras_proveedores' AS Vistas_Creadas;
SELECT 'Triggers: actualizar_stock_venta, actualizar_stock_compra, calcular_subtotal_detalle_pedido, calcular_subtotal_detalle_compra' AS Triggers_Creados;
