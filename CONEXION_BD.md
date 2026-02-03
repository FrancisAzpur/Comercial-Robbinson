# 🔌 Conexión a la Base de Datos - Comercial Robinson

## Requisitos Previos

1. **MySQL Server** instalado (versión 8.0 o superior recomendada)
2. **MySQL Workbench** (opcional, para administración visual)
3. **Java JDK 21** instalado
4. **Maven** instalado

---

## 📋 Pasos para Configurar la Base de Datos

### 1. Crear la Base de Datos

Abre MySQL Workbench o la terminal de MySQL y ejecuta:

```sql
CREATE DATABASE robbinson_db;
```

### 2. Ejecutar el Script de Esquema

Ejecuta el archivo `robbinson_db_schema.sql` que está en la raíz del proyecto:

```sql
USE robbinson_db;
SOURCE robbinson_db_schema.sql;
```

O desde MySQL Workbench:
- Abre el archivo `robbinson_db_schema.sql`
- Presiona el botón ⚡ "Execute" para ejecutar todo el script

### 3. Insertar Datos de Ejemplo (Opcional)

Los scripts de inserción están en la carpeta `database/inserts/`. Ejecutar en orden:

```sql
SOURCE database/inserts/01_productos_electrodomesticos.sql;
SOURCE database/inserts/02_productos_hogar.sql;
SOURCE database/inserts/03_clientes_ejemplo.sql;
SOURCE database/inserts/04_proveedores_ejemplo.sql;
SOURCE database/inserts/05_pedidos_ejemplo.sql;
SOURCE database/inserts/06_compras_ejemplo.sql;
```

---

## ⚙️ Configuración de application.properties

El archivo de configuración se encuentra en:
```
src/main/resources/application.properties
```

### Propiedades a Modificar

```properties
# ==================== Configuración de Base de Datos ====================
spring.datasource.url=jdbc:mysql://localhost:3306/robbinson_db
spring.datasource.username=root
spring.datasource.password=TU_CONTRASEÑA_AQUÍ
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

### 🔐 Cambiar la Contraseña

1. Abre el archivo `src/main/resources/application.properties`
2. Busca la línea: `spring.datasource.password=password123`
3. Reemplaza `password123` con **tu contraseña de MySQL Workbench**

**Ejemplo:**
```properties
# Si tu contraseña de MySQL es "miContraseñaSegura2024"
spring.datasource.password=miContraseñaSegura2024
```

### Puerto del Servidor (Opcional)

Por defecto la aplicación corre en el puerto `8083`. Si necesitas cambiarlo:
```properties
server.port=8080
```

---

## 🔄 Configuración de JPA/Hibernate

```properties
# ==================== Configuración de JPA/Hibernate ====================
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

### Opciones de `ddl-auto`:

| Valor | Descripción |
|-------|-------------|
| `update` | Actualiza el esquema automáticamente (recomendado para desarrollo) |
| `create` | Crea el esquema destruyendo datos previos |
| `create-drop` | Crea y elimina el esquema al cerrar la aplicación |
| `validate` | Solo valida el esquema, no hace cambios |
| `none` | Desactiva la gestión automática del esquema |

⚠️ **Importante**: En producción usar `validate` o `none`

---

## 🚀 Ejecutar la Aplicación

### Usando Maven (Terminal)

```bash
cd c:\Users\franc\OneDrive\Documentos\GitHub\ComRobinson
mvn spring-boot:run
```

### Usando Maven Wrapper (Windows)

```bash
.\mvnw spring-boot:run
```

### Verificar Conexión

1. Abre el navegador
2. Ve a: `http://localhost:8083`
3. Si ves la página de inicio, ¡la conexión funciona!

---

## 🔍 Verificar Tablas Creadas

Después de ejecutar la aplicación por primera vez, verifica en MySQL:

```sql
USE robbinson_db;
SHOW TABLES;
```

Deberías ver estas tablas:
- `clientes`
- `productos`
- `pedidos`
- `detalle_pedidos`
- `proveedores`
- `compras`
- `detalle_compras`
- `direcciones_cliente`

---

## ❌ Solución de Problemas Comunes

### Error: "Access denied for user 'root'@'localhost'"

**Solución**: La contraseña es incorrecta. Verifica tu contraseña de MySQL.

### Error: "Unknown database 'robbinson_db'"

**Solución**: La base de datos no existe. Créala primero:
```sql
CREATE DATABASE robbinson_db;
```

### Error: "Communications link failure"

**Solución**: MySQL no está corriendo. Inicia el servicio:
- Windows: `net start MySQL80`
- O desde Servicios de Windows

### Error: "The server time zone value is unrecognized"

**Solución**: Agrega esto a la URL de conexión:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/robbinson_db?serverTimezone=America/Lima
```

---

## 📁 Estructura de Archivos Relevantes

```
ComRobinson/
├── src/
│   └── main/
│       ├── java/com/Robbinson/ComRobinson/
│       │   ├── modelo/           # Entidades JPA
│       │   ├── repositorio/      # Interfaces JpaRepository
│       │   ├── servicios/        # Lógica de negocio
│       │   └── controladores/    # Controladores MVC
│       └── resources/
│           └── application.properties  # ⬅️ Archivo de configuración
├── database/
│   ├── inserts/                  # Scripts SQL de inserción
│   └── README.md
├── robbinson_db_schema.sql       # Esquema de la BD
└── CONEXION_BD.md               # Este archivo
```

---

## 📞 Contacto

Si tienes problemas con la conexión, verifica:
1. MySQL está instalado y corriendo
2. La contraseña es correcta
3. El puerto 3306 está disponible
4. La base de datos existe

---

*Última actualización: Febrero 2026*
