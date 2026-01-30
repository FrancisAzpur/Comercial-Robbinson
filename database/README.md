# 📋 Guía de Instalación de la Base de Datos - ComRobinson

## 🎯 Descripción
Sistema de base de datos optimizado para la tienda ComRobinson. Incluye gestión de productos (electrodomésticos y hogar), clientes, pedidos, proveedores y compras.

**Importante:** Esta versión **NO** incluye la tabla `categorias`. Las categorías se manejan únicamente en el frontend para filtrado de productos.

---

## 📂 Estructura de Archivos

```
database/
├── robbinson_db_schema.sql          # ⚙️ Schema completo (tablas, vistas, triggers)
└── inserts/
    ├── 01_productos_electrodomesticos.sql  # 📺 15 productos de electrodomésticos
    ├── 02_productos_hogar.sql              # 🏠 16 productos de hogar
    ├── 03_clientes_ejemplo.sql             # 👤 5 clientes + 6 direcciones
    ├── 04_proveedores_ejemplo.sql          # 🏭 8 proveedores
    ├── 05_pedidos_ejemplo.sql              # 🛒 5 pedidos de ejemplo
    └── 06_compras_ejemplo.sql              # 📦 5 compras a proveedores
```

---

## 🚀 Instalación Completa

### Paso 1: Crear la base de datos y tablas
```bash
mysql -u root -p < robbinson_db_schema.sql
```

### Paso 2: Insertar datos de productos
```bash
mysql -u root -p robbinson_db < inserts/01_productos_electrodomesticos.sql
mysql -u root -p robbinson_db < inserts/02_productos_hogar.sql
```

### Paso 3: (Opcional) Insertar datos de ejemplo
```bash
mysql -u root -p robbinson_db < inserts/03_clientes_ejemplo.sql
mysql -u root -p robbinson_db < inserts/04_proveedores_ejemplo.sql
mysql -u root -p robbinson_db < inserts/05_pedidos_ejemplo.sql
mysql -u root -p robbinson_db < inserts/06_compras_ejemplo.sql
```

---

## 🔄 Instalación Rápida (Un solo comando)

### Linux/Mac:
```bash
cat robbinson_db_schema.sql inserts/*.sql | mysql -u root -p
```

### Windows PowerShell:
```powershell
Get-Content robbinson_db_schema.sql, inserts\*.sql | mysql -u root -p
```

---

## 📊 Tablas Creadas

| Tabla | Descripción | Registros |
|-------|-------------|-----------|
| `productos` | Catálogo completo de productos | 31 |
| `clientes` | Clientes registrados | 5 |
| `direcciones_cliente` | Direcciones de envío | 6 |
| `pedidos` | Órdenes de venta | 5 |
| `detalle_pedidos` | Detalle de productos vendidos | Variable |
| `proveedores` | Proveedores de productos | 8 |
| `compras` | Órdenes de compra | 5 |
| `detalle_compras` | Detalle de productos comprados | Variable |

---

## 👁️ Vistas Disponibles

1. **`vista_productos_stock`** - Productos con información de stock y estado
2. **`vista_pedidos_completos`** - Pedidos con información del cliente
3. **`vista_ventas_por_producto`** - Estadísticas de ventas por producto
4. **`vista_compras_proveedores`** - Compras con información del proveedor

### Ejemplos de uso:
```sql
-- Ver productos con stock bajo
SELECT * FROM vista_productos_stock WHERE estado_stock = 'STOCK BAJO';

-- Ver todos los pedidos con información de cliente
SELECT * FROM vista_pedidos_completos ORDER BY fecha_pedido DESC;

-- Top 5 productos más vendidos
SELECT * FROM vista_ventas_por_producto ORDER BY total_vendido DESC LIMIT 5;
```

---

## ⚡ Triggers Automáticos

### Gestión de Stock:
- **`actualizar_stock_venta`** - Reduce stock al crear detalle de pedido
- **`actualizar_stock_compra`** - Aumenta stock al crear detalle de compra

### Cálculos Automáticos:
- **`calcular_subtotal_detalle_pedido`** - Calcula subtotal en ventas
- **`calcular_subtotal_detalle_compra`** - Calcula subtotal en compras

---

## 🔍 Consultas Útiles

### Verificar instalación:
```sql
-- Contar productos por categoría (usando datos JavaScript)
SELECT 
    CASE 
        WHEN codigo_producto LIKE 'ELEC-%' THEN 'Electrodomésticos'
        WHEN codigo_producto LIKE 'HOGAR-%' THEN 'Hogar'
        ELSE 'Otros'
    END AS categoria,
    COUNT(*) AS total
FROM productos
GROUP BY categoria;

-- Ver productos con stock crítico
SELECT nombre_producto, stock_actual, stock_minimo
FROM productos
WHERE stock_actual <= stock_minimo
ORDER BY stock_actual ASC;

-- Resumen de ventas
SELECT 
    estado,
    COUNT(*) AS cantidad_pedidos,
    SUM(total) AS monto_total
FROM pedidos
GROUP BY estado;
```

---

## ⚙️ Configuración de Spring Boot

Actualiza tu `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/robbinson_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=tu_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

---

## 🧪 Verificación de Datos

```sql
-- Total de productos
SELECT COUNT(*) AS total_productos FROM productos;
-- Resultado esperado: 31

-- Productos de electrodomésticos
SELECT COUNT(*) FROM productos WHERE codigo_producto LIKE 'ELEC-%';
-- Resultado esperado: 15

-- Productos de hogar
SELECT COUNT(*) FROM productos WHERE codigo_producto LIKE 'HOGAR-%';
-- Resultado esperado: 16

-- Total de clientes
SELECT COUNT(*) FROM clientes;
-- Resultado esperado: 5

-- Total de proveedores
SELECT COUNT(*) FROM proveedores;
-- Resultado esperado: 8
```

---

## ❗ Notas Importantes

1. **Categorías eliminadas**: La tabla `categorias` y la columna `id_categoria` en `productos` han sido eliminadas. Las categorías ahora se manejan exclusivamente en el frontend mediante JavaScript.

2. **Contraseñas de ejemplo**: Las contraseñas hasheadas en `03_clientes_ejemplo.sql` son solo para testing. En producción, implementa un sistema seguro de autenticación.

3. **IDs de productos**: Los IDs en los archivos INSERT corresponden a los IDs de los productos en los archivos JavaScript (`electrodomesticos.js` y `hogar.js`).

4. **Stock inicial**: Los valores de stock son estimados. Ajusta según tus necesidades reales.

---

## 🔧 Solución de Problemas

### Error: "Access denied for user"
```bash
# Verifica tus credenciales
mysql -u root -p
```

### Error: "Database exists"
```sql
-- Elimina la base de datos existente
DROP DATABASE IF EXISTS robbinson_db;
```

### Error: "Table doesn't exist"
```bash
# Ejecuta primero el schema
mysql -u root -p < robbinson_db_schema.sql
```

---

## 📞 Soporte

Para problemas o preguntas sobre la base de datos, revisa:
1. Los archivos SQL en `database/inserts/`
2. El schema completo en `robbinson_db_schema.sql`
3. Los datos de productos en `static/js/electrodomesticos.js` y `static/js/hogar.js`

---

**Versión:** 2026  
**Última actualización:** Enero 2026  
**Estado:** ✅ Producción
