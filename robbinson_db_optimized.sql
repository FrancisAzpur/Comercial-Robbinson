-- Base de Datos Comercial Robinson - Versión Optimizada
-- Creada: 28/01/2026
-- Se han optimizado campos, índices y tipos de datos

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
    contrasena_hash VARCHAR(255) NOT NULL COMMENT 'Contraseña hasheada (no guardar en texto plano)',
    telefono VARCHAR(15) COMMENT 'Teléfono de contacto',
    tipo_documento ENUM('DNI', 'RUC', 'PASAPORTE') DEFAULT 'DNI' COMMENT 'Tipo de documento de identidad',
    documento_identidad VARCHAR(20) UNIQUE COMMENT 'Número de documento único',
    activo BOOLEAN NOT NULL DEFAULT TRUE COMMENT 'Cliente activo o inactivo',
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de registro del cliente',
    
    INDEX idx_cliente_correo (correo_electronico),
    INDEX idx_cliente_documento (documento_identidad),
    INDEX idx_cliente_activo (activo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ====================================================================
-- 2) TABLA DIRECCIONES DEL CLIENTE
-- ====================================================================
CREATE TABLE direcciones_cliente (
    id_direccion BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID único de la dirección',
    id_cliente BIGINT NOT NULL COMMENT 'Referencia al cliente',
    alias VARCHAR(50) COMMENT 'Nombre corto (ej: Casa, Trabajo)',
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ====================================================================
-- 3) TABLA CATEGORÍAS
-- ====================================================================
CREATE TABLE categorias (
    id_categoria BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID único de la categoría',
    nombre_categoria VARCHAR(50) NOT NULL UNIQUE COMMENT 'Nombre de la categoría',
    descripcion VARCHAR(255) COMMENT 'Descripción de la categoría',
    icono VARCHAR(50) COMMENT 'Ícono Font Awesome (ej: fa-blender)',
    imagen VARCHAR(255) COMMENT 'Imagen representativa de la categoría',
    activo BOOLEAN NOT NULL DEFAULT TRUE COMMENT 'Categoría activa o inactiva',
    
    INDEX idx_nombre_categoria (nombre_categoria),
    INDEX idx_activo (activo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ====================================================================
-- 4) TABLA PRODUCTOS
-- ====================================================================
CREATE TABLE productos (
    id_producto BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID único del producto',
    codigo_producto VARCHAR(50) NOT NULL UNIQUE COMMENT 'Código interno del producto',
    nombre_producto VARCHAR(150) NOT NULL COMMENT 'Nombre del producto',
    descripcion TEXT COMMENT 'Descripción detallada del producto',
    id_categoria BIGINT NOT NULL COMMENT 'Categoría a la que pertenece',
    precio_venta DECIMAL(10,2) NOT NULL COMMENT 'Precio de venta al público',
    precio_compra DECIMAL(10,2) NOT NULL COMMENT 'Costo de compra al proveedor',
    stock_actual INT NOT NULL DEFAULT 0 COMMENT 'Cantidad disponible en stock',
    stock_minimo INT NOT NULL DEFAULT 5 COMMENT 'Stock mínimo para alertas de reorden',
    activo BOOLEAN NOT NULL DEFAULT TRUE COMMENT 'Producto activo para venta',
    imagen_principal VARCHAR(255) COMMENT 'Imagen principal del producto',
    etiqueta VARCHAR(50) COMMENT 'Etiqueta especial (ej: OFERTA, NUEVO, etc)',
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de registro del producto',
    
    FOREIGN KEY (id_categoria) REFERENCES categorias(id_categoria) ON DELETE RESTRICT,
    INDEX idx_codigo_producto (codigo_producto),
    INDEX idx_nombre_producto (nombre_producto),
    INDEX idx_categoria_producto (id_categoria),
    INDEX idx_activo (activo),
    INDEX idx_etiqueta (etiqueta)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ====================================================================
-- 5) TABLA PEDIDOS (ÓRDENES DE VENTA)
-- ====================================================================
CREATE TABLE pedidos (
    id_pedido BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID único del pedido',
    numero_pedido VARCHAR(20) NOT NULL UNIQUE COMMENT 'Número de referencia del pedido (ej: PED-2026-001)',
    id_cliente BIGINT NOT NULL COMMENT 'Cliente que realiza el pedido',
    id_direccion BIGINT COMMENT 'Dirección de envío',
    fecha_pedido TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha y hora del pedido',
    subtotal DECIMAL(10,2) NOT NULL COMMENT 'Subtotal sin impuestos',
    impuesto DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT 'Impuesto (IGV)',
    costo_envio DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT 'Costo de envío',
    total DECIMAL(10,2) NOT NULL COMMENT 'Total a pagar',
    metodo_pago VARCHAR(30) NOT NULL COMMENT 'Método de pago utilizado',
    estado ENUM('PENDIENTE', 'PAGADO', 'PROCESANDO', 'ENVIADO', 'ENTREGADO', 'CANCELADO') 
           NOT NULL DEFAULT 'PENDIENTE' COMMENT 'Estado del pedido',
    
    FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente) ON DELETE CASCADE,
    FOREIGN KEY (id_direccion) REFERENCES direcciones_cliente(id_direccion) ON DELETE SET NULL,
    INDEX idx_cliente_pedido (id_cliente),
    INDEX idx_fecha_pedido (fecha_pedido),
    INDEX idx_estado (estado),
    INDEX idx_numero_pedido (numero_pedido)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ====================================================================
-- 6) TABLA DETALLE DE PEDIDOS
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
    INDEX idx_producto_detalle (id_producto)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ====================================================================
-- 7) TABLA PROVEEDORES
-- ====================================================================
CREATE TABLE proveedores (
    id_proveedor BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID único del proveedor',
    nombre_empresa VARCHAR(100) NOT NULL COMMENT 'Nombre legal de la empresa',
    ruc VARCHAR(11) UNIQUE NOT NULL COMMENT 'RUC (Registro Único de Contribuyente)',
    contacto_nombre VARCHAR(100) COMMENT 'Nombre de la persona de contacto',
    contacto_telefono VARCHAR(15) COMMENT 'Teléfono del proveedor',
    contacto_email VARCHAR(100) COMMENT 'Email del proveedor',
    direccion VARCHAR(255) COMMENT 'Dirección del proveedor',
    activo BOOLEAN NOT NULL DEFAULT TRUE COMMENT 'Proveedor activo',
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de registro',
    
    INDEX idx_ruc_proveedor (ruc),
    INDEX idx_nombre_proveedor (nombre_empresa),
    INDEX idx_activo (activo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ====================================================================
-- 8) TABLA COMPRAS (ÓRDENES DE COMPRA)
-- ====================================================================
CREATE TABLE compras (
    id_compra BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID único de la compra',
    numero_compra VARCHAR(20) NOT NULL UNIQUE COMMENT 'Número de referencia de compra (ej: COM-2026-001)',
    id_proveedor BIGINT NOT NULL COMMENT 'Proveedor de la compra',
    fecha_compra TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de la compra',
    subtotal DECIMAL(10,2) NOT NULL COMMENT 'Subtotal sin impuestos',
    impuesto DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT 'IGV o impuesto aplicable',
    total DECIMAL(10,2) NOT NULL COMMENT 'Total a pagar al proveedor',
    estado ENUM('PENDIENTE', 'RECIBIDA', 'FACTURADA', 'PAGADA', 'CANCELADA') 
           NOT NULL DEFAULT 'PENDIENTE' COMMENT 'Estado de la compra',
    observaciones TEXT COMMENT 'Notas adicionales sobre la compra',
    
    FOREIGN KEY (id_proveedor) REFERENCES proveedores(id_proveedor) ON DELETE RESTRICT,
    INDEX idx_numero_compra (numero_compra),
    INDEX idx_fecha_compra (fecha_compra),
    INDEX idx_proveedor_compra (id_proveedor),
    INDEX idx_estado (estado)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ====================================================================
-- 9) TABLA DETALLE DE COMPRAS
-- ====================================================================
CREATE TABLE detalle_compras (
    id_detalle_compra BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID único del detalle',
    id_compra BIGINT NOT NULL COMMENT 'Referencia a la compra',
    id_producto BIGINT NOT NULL COMMENT 'Producto comprado',
    cantidad INT NOT NULL COMMENT 'Cantidad comprada',
    precio_unitario DECIMAL(10,2) NOT NULL COMMENT 'Precio unitario de compra',
    subtotal DECIMAL(10,2) NOT NULL COMMENT 'Subtotal de la línea',
    
    FOREIGN KEY (id_compra) REFERENCES compras(id_compra) ON DELETE CASCADE,
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto) ON DELETE RESTRICT,
    INDEX idx_compra_detalle (id_compra),
    INDEX idx_producto_compra (id_producto)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ====================================================================
-- 10) TABLA CARRITO (OPCIONAL - Para persistencia de carrito)
-- ====================================================================
CREATE TABLE carrito_temporal (
    id_carrito BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID único del carrito',
    id_cliente BIGINT COMMENT 'Cliente propietario del carrito (opcional)',
    sesion_id VARCHAR(255) COMMENT 'ID de sesión del navegador',
    id_producto BIGINT NOT NULL COMMENT 'Producto en el carrito',
    cantidad INT NOT NULL DEFAULT 1 COMMENT 'Cantidad del producto',
    fecha_agregado TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Cuándo se agregó',
    
    FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente) ON DELETE CASCADE,
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto) ON DELETE CASCADE,
    INDEX idx_cliente_carrito (id_cliente),
    INDEX idx_sesion (sesion_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ====================================================================
-- INSERCIÓN DE DATOS INICIALES - CATEGORÍAS
-- ====================================================================
INSERT INTO categorias (nombre_categoria, descripcion, icono, activo) VALUES
('Electrodomésticos', 'Toda la tecnología para tu cocina y hogar', 'fa-blender', TRUE),
('Hogar', 'Artículos de decoración y confort para el hogar', 'fa-couch', TRUE),
('Refrigeración', 'Refrigeradoras, congeladores y equipos de frío', 'fa-snowflake', TRUE),
('Lavado', 'Lavadoras, secadoras y equipos de lavandería', 'fa-soap', TRUE);

-- ====================================================================
-- VISTAS ÚTILES PARA REPORTES
-- ====================================================================

-- Vista: Productos con información de categoría y disponibilidad
CREATE OR REPLACE VIEW v_productos_disponibles AS
SELECT 
    p.id_producto,
    p.codigo_producto,
    p.nombre_producto,
    p.descripcion,
    c.nombre_categoria,
    p.precio_venta,
    p.precio_compra,
    p.stock_actual,
    CASE 
        WHEN p.stock_actual = 0 THEN 'Agotado'
        WHEN p.stock_actual < p.stock_minimo THEN 'Bajo Stock'
        ELSE 'Disponible'
    END as estado_stock,
    (p.precio_venta - p.precio_compra) as margen_ganancia,
    ((p.precio_venta - p.precio_compra) / p.precio_compra * 100) as margen_porcentaje,
    p.etiqueta,
    p.fecha_registro
FROM productos p
INNER JOIN categorias c ON p.id_categoria = c.id_categoria
WHERE p.activo = TRUE
ORDER BY c.nombre_categoria, p.nombre_producto;

-- Vista: Resumen de ventas por cliente
CREATE OR REPLACE VIEW v_ventas_por_cliente AS
SELECT 
    cl.id_cliente,
    cl.nombre_completo,
    cl.correo_electronico,
    COUNT(p.id_pedido) as total_pedidos,
    SUM(p.total) as monto_total_gastado,
    AVG(p.total) as ticket_promedio,
    MAX(p.fecha_pedido) as ultimo_pedido,
    COUNT(CASE WHEN p.estado = 'ENTREGADO' THEN 1 END) as pedidos_entregados
FROM clientes cl
LEFT JOIN pedidos p ON cl.id_cliente = p.id_cliente
WHERE cl.activo = TRUE
GROUP BY cl.id_cliente, cl.nombre_completo, cl.correo_electronico
ORDER BY monto_total_gastado DESC;

-- Vista: Stock bajo y productos para reorden
CREATE OR REPLACE VIEW v_productos_reorden AS
SELECT 
    p.id_producto,
    p.codigo_producto,
    p.nombre_producto,
    c.nombre_categoria,
    p.stock_actual,
    p.stock_minimo,
    (p.stock_minimo - p.stock_actual) as cantidad_falta,
    p.precio_compra,
    (p.stock_minimo - p.stock_actual) * p.precio_compra as monto_aproximado
FROM productos p
INNER JOIN categorias c ON p.id_categoria = c.id_categoria
WHERE p.stock_actual < p.stock_minimo
  AND p.activo = TRUE
ORDER BY cantidad_falta DESC;

-- ====================================================================
-- NOTA IMPORTANTE SOBRE SEGURIDAD
-- ====================================================================
-- 1. NUNCA guardes contraseñas en texto plano - usa BCRYPT o ARGON2
-- 2. Implementa restricciones de acceso a nivel de aplicación
-- 3. Usa transacciones para operaciones críticas
-- 4. Realiza backups regularmente
-- 5. Implementa auditoría para cambios en datos sensibles
-- 6. Usa SSL/TLS para transmisión de datos
-- 7. Implementa validación de entrada en la aplicación
